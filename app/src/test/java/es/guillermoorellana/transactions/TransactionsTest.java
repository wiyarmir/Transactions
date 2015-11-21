package es.guillermoorellana.transactions;

import org.junit.Test;

import java.util.List;

public class TransactionsTest {

    @Test
    public void testTransactionsAreRead() {
        TestDataRepository.getTransactionsList();
    }

    @Test
    public void testProductList() {
        List<Product> list = TransactionsAggregator.productsFromTransactions(TestDataRepository.getTransactionsList());
    }

    @Test
    public void testTransactionsBySku() {
        List<Transaction> list = TransactionsAggregator.transactionsForSku("M3474", TestDataRepository.getTransactionsList());
        System.out.println(list);
    }
}
