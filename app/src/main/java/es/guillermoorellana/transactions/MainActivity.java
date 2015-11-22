package es.guillermoorellana.transactions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import es.guillermoorellana.transactions.io.DataRepository;
import es.guillermoorellana.transactions.model.Product;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_SKU = "sku";
    @Bind(R.id.list) ListView listView;
    @Bind(android.R.id.empty) TextView empty;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        adapter = new ProductAdapter(this);
        listView.setAdapter(adapter);
        listView.setEmptyView(empty);

        DataRepository.getTransactions()
                .groupBy(transaction -> transaction.getSku())
                .flatMap(grouped -> grouped.count().map(integer -> new Product(grouped.getKey(), integer)))
                .toList()
                .doOnNext(adapter::addAll)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }


    @OnItemClick(R.id.list)
    public void onListItemClick(int position, AdapterView<ArrayAdapter<Product>> adapterView) {
        Product product = (Product) adapterView.getItemAtPosition(position);
        Intent intent = new Intent(this, TransactionsActivity.class);
        intent.putExtra(EXTRA_SKU, product.getSku());
        startActivity(intent);
    }

    public class ProductAdapter extends ArrayAdapter<Product> {
        public ProductAdapter(Context context) {
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

            Product item = getItem(position);
            viewHolder.text1.setText(item.getSku());
            viewHolder.text2.setText(item.transactionCount() + " transactions");

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
