package es.guillermoorellana.transactions.model;

import java.text.NumberFormat;
import java.util.Currency;

public class ConvertedTransaction extends Transaction {
    protected String originalCurrency;
    protected float originalAmount;

    public ConvertedTransaction(String sku,
                                String currency, float amount,
                                String originalCurrency, float originalAmount) {
        super(sku, currency, amount);
        this.originalCurrency = originalCurrency;
        this.originalAmount = originalAmount;
    }

    public String getOriginalCurrency() {
        return originalCurrency;
    }

    public float getOriginalAmount() {
        return originalAmount;
    }

    public String toOriginalString() {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        nf.setCurrency(Currency.getInstance(originalCurrency));
        return nf.format(originalAmount);
    }
}
