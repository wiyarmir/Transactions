package es.guillermoorellana.transactions;

public class Product {
    public String sku;
    public int nTransactions;

    public Product(String sku, int nTransactions) {
        this.sku = sku;
        this.nTransactions = nTransactions;
    }

    @Override
    public String toString() {
        return sku + ": " + nTransactions;
    }
}
