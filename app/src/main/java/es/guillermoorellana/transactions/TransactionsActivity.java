package es.guillermoorellana.transactions;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;

import butterknife.Bind;
import butterknife.ButterKnife;
import es.guillermoorellana.transactions.io.DataRepository;
import es.guillermoorellana.transactions.model.ConvertedTransaction;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TransactionsActivity extends AppCompatActivity {

    @Bind(R.id.list) ListView listView;
    @Bind(R.id.text) TextView textView;
    @Bind(android.R.id.empty) TextView empty;

    private String sku;
    private final static String PRESENTATION_CURRENCY = "GBP";
    private Observable<ConvertedTransaction> transactionsObservable;
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
                NumberFormat nf = NumberFormat.getCurrencyInstance();
                nf.setCurrency(Currency.getInstance(PRESENTATION_CURRENCY));
                textView.setText("Total: " + nf.format(sum));
                adapter.addAll(list);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(ConvertedTransaction convertedTransaction) {
                sum += convertedTransaction.getAmount();
                list.add(convertedTransaction);
            }
        };

        DataRepository.getRates()
                .toList()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnNext(rates -> rateConverter = new RateConverter(rates))
                .doOnCompleted(() -> transactionsObservable
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(subscriber))
                .subscribe();

        transactionsObservable = DataRepository.getTransactions()
                .filter(transaction -> transaction.getSku().equals(sku))
                .map(transaction -> new ConvertedTransaction(
                                transaction.getSku(),
                                PRESENTATION_CURRENCY,
                                rateConverter.convert(transaction.getCurrency(), PRESENTATION_CURRENCY, transaction.getAmount()),
                                transaction.getCurrency(),
                                transaction.getAmount()
                        )
                );
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

            viewHolder.text1.setText(getItem(position).toString());
            viewHolder.text2.setText(getItem(position).toOriginalString());

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
