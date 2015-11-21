package es.guillermoorellana.transactions;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class TransactionsActivity extends AppCompatActivity {

    @Bind(R.id.list) ListView listView;
    private String sku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        ButterKnife.bind(this);

        sku = getIntent().getStringExtra(MainActivity.EXTRA_SKU);

        getSupportActionBar().setTitle("Transactions for " + sku);

        DataRepository.getTransactions()
                .filter(new Func1<Transaction, Boolean>() {
                    @Override
                    public Boolean call(Transaction transaction) {
                        return transaction.sku.equals(sku);
                    }
                })
                .toList()
                .doOnNext(new Action1<List<Transaction>>() {
                    @Override
                    public void call(List<Transaction> transactionList) {
                        listView.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.item_product, transactionList));
                    }
                })
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
