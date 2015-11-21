package es.guillermoorellana.transactions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionsAggregator {
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

    public static List<Transaction> transactionsForSku(String sku, List<Transaction> transactionList) {
        List<Transaction> returnList = new ArrayList<>();

        for (Transaction transaction : transactionList) {
            if (transaction.sku.equals(sku)) {
                returnList.add(transaction);
            }
        }

        return returnList;
    }
}
