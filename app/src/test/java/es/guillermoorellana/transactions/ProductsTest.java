package es.guillermoorellana.transactions;

import org.junit.Test;

import java.util.List;

public class ProductsTest {

    @Test
    public void testProductList() {
        List<Product> list = ProductAggregator.productsFromTransactions(TestDataRepository.getTransactionsList());
        System.out.println(list);
    }
}
