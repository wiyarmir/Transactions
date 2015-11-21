package es.guillermoorellana.transactions;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class TransactionsActivity extends AppCompatActivity {

    @Bind(R.id.list) ListView listView;
    @Bind(R.id.text) TextView textView;

    private String sku;
    private final static String PRESENTATION_CURRENCY = "GBP";
    private Observable transactionsObservable;
    private RateConverter rateConverter;
    private float sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        ButterKnife.bind(this);

        sku = getIntent().getStringExtra(MainActivity.EXTRA_SKU);

        getSupportActionBar().setTitle("Transactions for " + sku);

        DataRepository.getRates()
                .toList()
                .doOnNext(new Action1<List<Rate>>() {
                    @Override
                    public void call(List<Rate> rates) {
                        rateConverter = new RateConverter(rates);
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        transactionsObservable.subscribe();
                    }
                })
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
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
                        ct.amount = rateConverter.convert(transaction.currency, PRESENTATION_CURRENCY, transaction.amount);
                        ct.originalCurrency = ct.currency;
                        ct.currency = PRESENTATION_CURRENCY;
                        return ct;
                    }
                })
                .doOnNext(new Action1<ConvertedTransaction>() {
                    @Override
                    public void call(ConvertedTransaction convertedTransaction) {
                        sum += convertedTransaction.amount;
                    }
                })
                .toList()
                .doOnNext(new Action1<List<ConvertedTransaction>>() {
                    @Override
                    public void call(List<ConvertedTransaction> transactionList) {
                        listView.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.item_product, transactionList));
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        textView.setText(String.format("Total: %.2f %s", sum, PRESENTATION_CURRENCY));
                    }
                })
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread());
    }
}
