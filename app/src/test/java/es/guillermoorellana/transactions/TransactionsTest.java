package es.guillermoorellana.transactions;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TransactionsTest {
    String json = "[{\"amount\":\"25.2\",\"sku\":\"J4064\",\"currency\":\"CAD\"},{\"amount\":\"19.5\",\"sku\":\"R9704\",\"currency\":\"USD\"},{\"amount\":\"25.4\",\"sku\":\"C7156\",\"currency\":\"GBP\"},{\"amount\":\"30.1\",\"sku\":\"X1893\",\"currency\":\"GBP\"},{\"amount\":\"33.0\",\"sku\":\"R9704\",\"currency\":\"USD\"},{\"amount\":\"18.5\",\"sku\":\"A8964\",\"currency\":\"AUD\"},{\"amount\":\"16.8\",\"sku\":\"X1893\",\"currency\":\"GBP\"},{\"amount\":\"17.2\",\"sku\":\"A8964\",\"currency\":\"USD\"},{\"amount\":\"30.5\",\"sku\":\"N6308\",\"currency\":\"CAD\"},{\"amount\":\"22.9\",\"sku\":\"A8964\",\"currency\":\"AUD\"},{\"amount\":\"18.3\",\"sku\":\"N6308\",\"currency\":\"CAD\"},{\"amount\":\"21.5\",\"sku\":\"O7730\",\"currency\":\"USD\"},{\"amount\":\"17.7\",\"sku\":\"M3474\",\"currency\":\"GBP\"},{\"amount\":\"26.1\",\"sku\":\"J4064\",\"currency\":\"CAD\"},{\"amount\":\"30.0\",\"sku\":\"C7156\",\"currency\":\"AUD\"},{\"amount\":\"30.2\",\"sku\":\"A0911\",\"currency\":\"USD\"},{\"amount\":\"29.8\",\"sku\":\"N6308\",\"currency\":\"GBP\"},{\"amount\":\"32.0\",\"sku\":\"A8964\",\"currency\":\"GBP\"},{\"amount\":\"29.2\",\"sku\":\"X1893\",\"currency\":\"AUD\"},{\"amount\":\"26.1\",\"sku\":\"O7730\",\"currency\":\"GBP\"},{\"amount\":\"23.8\",\"sku\":\"J4064\",\"currency\":\"CAD\"},{\"amount\":\"31.7\",\"sku\":\"V5239\",\"currency\":\"GBP\"},{\"amount\":\"19.6\",\"sku\":\"X1893\",\"currency\":\"GBP\"},{\"amount\":\"21.4\",\"sku\":\"R9704\",\"currency\":\"USD\"},{\"amount\":\"33.5\",\"sku\":\"A8964\",\"currency\":\"USD\"},{\"amount\":\"15.3\",\"sku\":\"J4064\",\"currency\":\"CAD\"},{\"amount\":\"27.8\",\"sku\":\"O7730\",\"currency\":\"AUD\"},{\"amount\":\"20.1\",\"sku\":\"A8964\",\"currency\":\"USD\"},{\"amount\":\"29.4\",\"sku\":\"G7340\",\"currency\":\"USD\"},{\"amount\":\"28.8\",\"sku\":\"V5239\",\"currency\":\"AUD\"},{\"amount\":\"31.2\",\"sku\":\"W9806\",\"currency\":\"GBP\"},{\"amount\":\"33.0\",\"sku\":\"R9704\",\"currency\":\"GBP\"},{\"amount\":\"19.7\",\"sku\":\"A0911\",\"currency\":\"GBP\"},{\"amount\":\"27.6\",\"sku\":\"M3474\",\"currency\":\"USD\"},{\"amount\":\"24.3\",\"sku\":\"M3474\",\"currency\":\"CAD\"},{\"amount\":\"16.6\",\"sku\":\"N6308\",\"currency\":\"GBP\"},{\"amount\":\"25.8\",\"sku\":\"A0911\",\"currency\":\"USD\"},{\"amount\":\"18.2\",\"sku\":\"V5239\",\"currency\":\"USD\"},{\"amount\":\"22.2\",\"sku\":\"V5239\",\"currency\":\"AUD\"},{\"amount\":\"28.5\",\"sku\":\"G7340\",\"currency\":\"CAD\"},{\"amount\":\"16.9\",\"sku\":\"X1893\",\"currency\":\"AUD\"},{\"amount\":\"32.4\",\"sku\":\"G7340\",\"currency\":\"CAD\"},{\"amount\":\"19.9\",\"sku\":\"R9704\",\"currency\":\"AUD\"},{\"amount\":\"25.9\",\"sku\":\"A0911\",\"currency\":\"USD\"},{\"amount\":\"30.1\",\"sku\":\"W9806\",\"currency\":\"USD\"},{\"amount\":\"31.0\",\"sku\":\"J4064\",\"currency\":\"CAD\"},{\"amount\":\"20.8\",\"sku\":\"N6308\",\"currency\":\"GBP\"},{\"amount\":\"15.0\",\"sku\":\"O7730\",\"currency\":\"USD\"},{\"amount\":\"16.6\",\"sku\":\"A0911\",\"currency\":\"AUD\"},{\"amount\":\"18.3\",\"sku\":\"O7730\",\"currency\":\"CAD\"},{\"amount\":\"27.3\",\"sku\":\"O7730\",\"currency\":\"AUD\"},{\"amount\":\"23.6\",\"sku\":\"R9704\",\"currency\":\"AUD\"},{\"amount\":\"33.2\",\"sku\":\"W9806\",\"currency\":\"USD\"},{\"amount\":\"23.0\",\"sku\":\"X1893\",\"currency\":\"CAD\"},{\"amount\":\"20.4\",\"sku\":\"X1893\",\"currency\":\"USD\"},{\"amount\":\"30.6\",\"sku\":\"V5239\",\"currency\":\"USD\"},{\"amount\":\"16.2\",\"sku\":\"O7730\",\"currency\":\"GBP\"},{\"amount\":\"20.3\",\"sku\":\"C7156\",\"currency\":\"USD\"},{\"amount\":\"17.3\",\"sku\":\"G7340\",\"currency\":\"USD\"},{\"amount\":\"16.4\",\"sku\":\"J4064\",\"currency\":\"USD\"},{\"amount\":\"21.7\",\"sku\":\"G7340\",\"currency\":\"AUD\"},{\"amount\":\"23.9\",\"sku\":\"V5239\",\"currency\":\"GBP\"},{\"amount\":\"15.3\",\"sku\":\"C7156\",\"currency\":\"AUD\"},{\"amount\":\"25.0\",\"sku\":\"X1893\",\"currency\":\"AUD\"},{\"amount\":\"24.9\",\"sku\":\"C7156\",\"currency\":\"USD\"},{\"amount\":\"33.7\",\"sku\":\"W9806\",\"currency\":\"USD\"},{\"amount\":\"16.8\",\"sku\":\"O7730\",\"currency\":\"USD\"},{\"amount\":\"32.8\",\"sku\":\"J4064\",\"currency\":\"CAD\"},{\"amount\":\"22.0\",\"sku\":\"A0911\",\"currency\":\"GBP\"},{\"amount\":\"26.9\",\"sku\":\"V5239\",\"currency\":\"GBP\"},{\"amount\":\"34.8\",\"sku\":\"R9704\",\"currency\":\"GBP\"},{\"amount\":\"26.9\",\"sku\":\"W9806\",\"currency\":\"USD\"},{\"amount\":\"31.0\",\"sku\":\"R9704\",\"currency\":\"GBP\"},{\"amount\":\"18.0\",\"sku\":\"C7156\",\"currency\":\"GBP\"},{\"amount\":\"23.5\",\"sku\":\"A8964\",\"currency\":\"AUD\"},{\"amount\":\"30.7\",\"sku\":\"A0911\",\"currency\":\"CAD\"},{\"amount\":\"20.4\",\"sku\":\"W9806\",\"currency\":\"GBP\"},{\"amount\":\"32.5\",\"sku\":\"N6308\",\"currency\":\"AUD\"},{\"amount\":\"33.3\",\"sku\":\"R9704\",\"currency\":\"USD\"}]";
    private Gson gson;

    @Before
    public void setUp() throws Exception {
        gson = new Gson();
    }

    @Test
    public void testTransactionsAreRead() {
        getTransactionsList();
    }

    private List<Transaction> getTransactionsList() {
        Transaction[] array = gson.fromJson(json, Transaction[].class);
        return Arrays.asList(array);
    }
}
