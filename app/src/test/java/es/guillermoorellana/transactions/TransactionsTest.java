package es.guillermoorellana.transactions;

import org.junit.Test;

public class TransactionsTest {

    @Test
    public void testTransactionsAreRead() {
        TestDataRepository.getTransactionsList();
    }
}
