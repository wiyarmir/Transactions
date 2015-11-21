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
 * class which would make method calls more meaningful. Would improve performance using arraymaps
 * and making an initial estimation of the array sizes needed so there would not be that many allocs
 */
public class RateConverter {
    Map<String, Map<String, Float>> conversionTree;

    public RateConverter(List<Rate> rateList) {
        conversionTree = new HashMap<>();
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
        if (!conversionTree.containsKey(from)) {
            throw new UnsupportedOperationException();
        }
        Map<String, Float> fromRates = conversionTree.get(from);
        if (fromRates.containsKey(to)) {
            return fromRates.get(to) * amount;
        } else {
            int attempts = 0;
            while (attempts < conversionTree.size()) {
                for (Map.Entry<String, Map<String, Float>> entry : conversionTree.entrySet()) {
                    if (fromRates.containsKey(entry.getKey())
                            && entry.getValue().containsKey(to)) {
                        float combinedRate = entry.getValue().get(to) * fromRates.get(entry.getKey());
                        fromRates.put(to, combinedRate);
                        return amount * combinedRate;
                    }
                }
                attempts++;
            }
        }
        throw new UnsupportedOperationException();
    }
}
