package es.guillermoorellana.transactions;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Converts rates.
 * <p/>
 * Stores conversion rates in a tree emulated by using two maps. Initially only populates it with
 * the provided input data, and lazily populates the rest as needed.
 * <p/>
 * Sacrificed readability and performance. Would improve readability using a dedicated weighed tree
 * class (or weighed graph) which would make method calls more meaningful. Would improve performance
 * not using HashMaps but ArrayMaps (gives StackOverflow, must be some impl. detail of ArrayMap).
 */
public class RateConverter {
    Map<String, Map<String, Float>> conversionTree;

    public RateConverter(List<Rate> rateList) {
        conversionTree = new HashMap<>(rateList.size() / 2);
        // initial population of the tree
        for (Rate rate : rateList) {
            Map<String, Float> leaf;
            if (!conversionTree.containsKey(rate.from)) {
                leaf = new HashMap<>();
                conversionTree.put(rate.from, leaf);
            } else {
                leaf = conversionTree.get(rate.from);
            }
            leaf.put(rate.to, rate.rate);
        }
    }

    public float convert(String from, String to, float amount) {
        return amount * conversionRate(from, to);
    }

    private float conversionRate(String from, String to) {
        if (from.equals(to)) { // no conversion required
            return 1;
        }
        if (!conversionTree.containsKey(from)) { // starting currency unknown
            throw new UnsupportedOperationException();
        }
        Map<String, Float> fromRates = conversionTree.get(from);
        if (fromRates.containsKey(to)) { // target currency is in our known map
            return fromRates.get(to);
        } else { // unknown path to target currency, must search
            for (Map.Entry<String, Map<String, Float>> midEntry : conversionTree.entrySet()) {
                String midCurrency = midEntry.getKey();
                Map<String, Float> midRates = midEntry.getValue();
                if (midCurrency.equals(from)) {// skip ourselves
                    continue;
                }
                if (fromRates.containsKey(midCurrency)) { // we know how to get there
                    if (!midRates.containsKey(to)) { // but we are not there yet
                        return fromRates.get(midCurrency) * conversionRate(midCurrency, to); // keep looking
                    } else { // found!
                        float midRate = fromRates.get(midCurrency) * midRates.get(to);
                        fromRates.put(midCurrency, midRate); // cache search
                        return midRate;
                    }
                }
            }
        }
        throw new UnsupportedOperationException(); // we tried!
    }
}
