package es.guillermoorellana.transactions;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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

        new TransactionsTask().execute(sku);
    }

    public class TransactionsTask extends AsyncTask<String, Void, List<Transaction>> {
        @Override
        protected List<Transaction> doInBackground(String... params) {
            Gson gson = new Gson();
            try {
                InputStreamReader in = new InputStreamReader(getAssets().open("1/transactions.json"));
                List<Transaction> trList = Arrays.asList(gson.fromJson(in, Transaction[].class));
                return TransactionsAggregator.transactionsForSku(sku, trList);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Arrays.asList(new Transaction[]{});
        }

        @Override
        protected void onPostExecute(List<Transaction> transactionList) {
            runOnUiThread(new PopulateListRunnable(transactionList));
        }
    }

    private class PopulateListRunnable implements Runnable {
        private final List<Transaction> transactions;

        public PopulateListRunnable(List<Transaction> transactionList) {
            transactions = transactionList;
        }

        @Override
        public void run() {
            listView.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.item_product, transactions));
        }
    }
}
