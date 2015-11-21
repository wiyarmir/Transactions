package es.guillermoorellana.transactions;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class DataRepository {
    private static Gson gson = new Gson();
    private static String basePath = "1/";

    public static List<Transaction> getTransactionList() {
        InputStreamReader in = null;
        try {
            in = new InputStreamReader(App.getInstance().getAssets().open(basePath + "transactions.json"));
            return Arrays.asList(gson.fromJson(in, Transaction[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Arrays.asList(new Transaction[]{});
    }

    public static List<Rate> getRateList() {
        InputStreamReader in = null;
        try {
            in = new InputStreamReader(App.getInstance().getAssets().open(basePath + "rates.json"));
            return Arrays.asList(gson.fromJson(in, Rate[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Arrays.asList(new Rate[]{});
    }
}
