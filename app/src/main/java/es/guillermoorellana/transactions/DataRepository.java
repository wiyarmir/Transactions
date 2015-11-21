package es.guillermoorellana.transactions;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import rx.Observable;

public class DataRepository {
    private static Gson gson = new Gson();
    private static String basePath = "1/";
    private static List<Transaction> transactions;
    private static List<Rate> rates;

    public static List<Transaction> getTransactionList() {
        if (transactions != null) {
            return transactions;
        }
        try {
            InputStreamReader in = new InputStreamReader(App.getInstance().getAssets().open(basePath + "transactions.json"));
            transactions = Arrays.asList(gson.fromJson(in, Transaction[].class));
            return transactions;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Arrays.asList(new Transaction[]{});
    }

    public static List<Rate> getRateList() {
        if (rates != null) {
            return rates;
        }
        try {
            InputStreamReader in = new InputStreamReader(App.getInstance().getAssets().open(basePath + "rates.json"));
            rates = Arrays.asList(gson.fromJson(in, Rate[].class));
            return rates;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Arrays.asList(new Rate[]{});
    }

    public static Observable<Transaction> getTransactions() {
        return Observable.from(getTransactionList());
    }

    public static Observable<Rate> getRates() {
        return Observable.from(getRateList());
    }
}
