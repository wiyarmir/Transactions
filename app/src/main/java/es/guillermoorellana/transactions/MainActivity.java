package es.guillermoorellana.transactions;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.list) ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        new TransactionTask().execute();
    }

    public class TransactionTask extends AsyncTask<Void, Void, List<Product>> {
        @Override
        protected List<Product> doInBackground(Void... params) {
            Gson gson = new Gson();
            try {
                InputStreamReader in = new InputStreamReader(getAssets().open("1/transactions.json"));
                List<Transaction> trList = Arrays.asList(gson.fromJson(in, Transaction[].class));
                return ProductAggregator.productsFromTransactions(trList);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Arrays.asList(new Product[]{});
        }

        @Override
        protected void onPostExecute(List<Product> products) {
            runOnUiThread(new PopulateListRunnable(products));
        }
    }

    public class PopulateListRunnable implements Runnable {
        private final List<Product> list;

        public PopulateListRunnable(List<Product> products) {
            list = products;
        }

        @Override
        public void run() {
            ArrayAdapter<Product> adapter = new ArrayAdapter<>(
                    getApplicationContext(),
                    R.layout.item_product,
                    list
            );
            listView.setAdapter(adapter);
        }
    }
}
