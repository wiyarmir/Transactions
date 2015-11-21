package es.guillermoorellana.transactions;

import java.text.NumberFormat;
import java.util.Currency;

public class Transaction {
    protected String sku;
    protected String currency;
    protected float amount;

    @Override
    public String toString() {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        nf.setCurrency(Currency.getInstance(currency));
        return nf.format(amount);
    }

    public Transaction(String sku, String currency, float amount) {
        this.sku = sku;
        this.currency = currency;
        this.amount = amount;
    }

    public String getSku() {
        return sku;
    }

    public String getCurrency() {
        return currency;
    }

    public float getAmount() {
        return amount;
    }
}
