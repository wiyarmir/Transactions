package es.guillermoorellana.transactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductAggregator {
    public static List<Product> productsFromTransactions(List<Transaction> transactionsList) {
        Map<String, Product> aggregationMap = new HashMap<>();
        for (Transaction transaction : transactionsList) {
            if (aggregationMap.containsKey(transaction.sku)) {
                aggregationMap.get(transaction.sku).nTransactions++;
            } else {
                aggregationMap.put(transaction.sku, new Product(transaction.sku, 1));
            }
        }
        return new ArrayList<>(aggregationMap.values());
    }
}
