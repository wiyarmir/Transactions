package es.guillermoorellana.transactions.model;

public class Product {
    private String sku;
    private int nTransactions;

    public Product(String sku, int nTransactions) {
        this.sku = sku;
        this.nTransactions = nTransactions;
    }

    @Override
    public String toString() {
        return sku + ": " + nTransactions;
    }

    public String getSku() {
        return sku;
    }

    public int transactionCount() {
        return nTransactions;
    }

    public void addTransaction() {
        nTransactions++;
    }
}
