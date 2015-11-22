package es.guillermoorellana.transactions;

import android.support.v4.util.ArrayMap;

import java.util.List;
import java.util.Map;

/**
 * Converts rates.
 * <p/>
 * Stores conversion rates in a tree emulated by using two maps. Initially only populates it with
 * the provided input data, and lazily populates the rest as needed. It does not attempt to recognise
 * if a node has already been visited nor attempt to use inverse conversions.
 * <p/>
 * Sacrificed readability and performance. Would improve readability using a dedicated weighed tree
 * class (or weighed graph) which would make method calls more meaningful. Would improve performance
 * using arraymaps and making an initial estimation of the array sizes needed so there would not be
 * that many allocs Would make it a bit more intelligent by keeping already visited nodes not to
 * revisit them and use invert conversions (1/known conversion).
 */
public class RateConverter {
    Map<String, Map<String, Float>> conversionTree;

    public RateConverter(List<Rate> rateList) {
        conversionTree = new ArrayMap<>(rateList.size() / 2);
        // initial population of the tree
        for (Rate rate : rateList) {
            Map<String, Float> leaf;
            if (!conversionTree.containsKey(rate.from)) {
                leaf = new ArrayMap<>();
                conversionTree.put(rate.from, leaf);
            } else {
                leaf = conversionTree.get(rate.from);
            }
            leaf.put(rate.to, rate.rate);
        }
    }

    public float convert(String from, String to, float amount) {
        if (from.equals(to)) { // no conversion required
            return amount;
        }
        if (!conversionTree.containsKey(from)) { // starting currency unknown
            throw new UnsupportedOperationException();
        }
        Map<String, Float> knownRates = conversionTree.get(from);
        if (knownRates.containsKey(to)) { // target currency is in our known map
            return knownRates.get(to) * amount;
        } else { // unknown path to target currency, must search
            int attempts = 0;
            while (attempts < conversionTree.size()) {
                for (Map.Entry<String, Map<String, Float>> entry : conversionTree.entrySet()) {
                    if (entry.getKey().equals(from)) {
                        continue;
                    }
                    if (knownRates.containsKey(entry.getKey())
                            && entry.getValue().containsKey(to)) {
                        float combinedRate = entry.getValue().get(to) * knownRates.get(entry.getKey());
                        knownRates.put(to, combinedRate);
                        return amount * combinedRate;
                    }
                }
                attempts++;
            }
        }
        throw new UnsupportedOperationException();
    }
}
