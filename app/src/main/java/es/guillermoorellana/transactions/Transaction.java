package es.guillermoorellana.transactions;

public class Transaction {
    public String sku;
    public String currency;
    public float amount;

    @Override
    public String toString() {
        return String.format("%.2f %s", amount, currency);
    }
}
