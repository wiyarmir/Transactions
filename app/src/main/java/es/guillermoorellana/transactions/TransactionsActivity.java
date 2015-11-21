package es.guillermoorellana.transactions;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class TransactionsActivity extends AppCompatActivity {

    @Bind(R.id.list) ListView listView;
    @Bind(R.id.text) TextView textView;
    @Bind(android.R.id.empty) TextView empty;

    private String sku;
    private final static String PRESENTATION_CURRENCY = "GBP";
    private Observable transactionsObservable;
    private RateConverter rateConverter;
    private ConvertedTransactionAdapter adapter;
    private Subscriber<ConvertedTransaction> subscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        ButterKnife.bind(this);

        sku = getIntent().getStringExtra(MainActivity.EXTRA_SKU);

        getSupportActionBar().setTitle("Transactions for " + sku);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new ConvertedTransactionAdapter(this);
        listView.setAdapter(adapter);
        listView.setEmptyView(empty);

        subscriber = new Subscriber<ConvertedTransaction>() {
            private ArrayList<ConvertedTransaction> list = new ArrayList<>();
            private float sum;

            @Override
            public void onCompleted() {
                textView.setText(String.format("Total: %.2f %s", sum, PRESENTATION_CURRENCY));
                adapter.addAll(list);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(ConvertedTransaction convertedTransaction) {
                sum += convertedTransaction.amount;
                list.add(convertedTransaction);
            }
        };

        DataRepository.getRates()
                .toList()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Action1<List<Rate>>() {
                    @Override
                    public void call(List<Rate> rates) {
                        rateConverter = new RateConverter(rates);
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        transactionsObservable
                                .subscribeOn(AndroidSchedulers.mainThread())
                                .subscribe(subscriber);
                    }
                })
                .subscribe();

        transactionsObservable = DataRepository.getTransactions()
                .filter(new Func1<Transaction, Boolean>() {
                    @Override
                    public Boolean call(Transaction transaction) {
                        return transaction.sku.equals(sku);
                    }
                })
                .map(new Func1<Transaction, ConvertedTransaction>() {
                    @Override
                    public ConvertedTransaction call(Transaction transaction) {
                        ConvertedTransaction ct = new ConvertedTransaction();
                        ct.sku = transaction.sku;
                        ct.originalCurrency = transaction.currency;
                        ct.originalAmount = transaction.amount;
                        ct.currency = PRESENTATION_CURRENCY;
                        ct.amount = rateConverter.convert(transaction.currency, PRESENTATION_CURRENCY, transaction.amount);
                        return ct;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class ConvertedTransactionAdapter extends ArrayAdapter<ConvertedTransaction> {
        public ConvertedTransactionAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_2);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder viewHolder;
            if (convertView == null) {
                view = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, parent, false);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }

            ConvertedTransaction item = getItem(position);
            viewHolder.text1.setText(String.format("%.2f %s", item.amount, item.currency));
            viewHolder.text2.setText(String.format("%.2f %s", item.originalAmount, item.originalCurrency));

            return view;
        }

        class ViewHolder {
            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }

            @Bind(android.R.id.text1) TextView text1;
            @Bind(android.R.id.text2) TextView text2;
        }
    }
}
