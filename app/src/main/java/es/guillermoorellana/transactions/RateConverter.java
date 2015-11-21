package es.guillermoorellana.transactions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        }
        throw new UnsupportedOperationException();
    }
}
