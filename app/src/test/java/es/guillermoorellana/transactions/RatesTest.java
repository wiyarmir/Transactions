package es.guillermoorellana.transactions;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class RatesTest {

    private final String json = "[{\"from\":\"USD\",\"rate\":\"0.77\",\"to\":\"GBP\"},{\"from\":\"GBP\",\"rate\":\"1.3\",\"to\":\"USD\"},{\"from\":\"USD\",\"rate\":\"1.09\",\"to\":\"CAD\"},{\"from\":\"CAD\",\"rate\":\"0.92\",\"to\":\"USD\"},{\"from\":\"GBP\",\"rate\":\"0.83\",\"to\":\"AUD\"},{\"from\":\"AUD\",\"rate\":\"1.2\",\"to\":\"GBP\"}]";
    private Gson gson;

    @Before
    public void setUp() throws Exception {
        gson = new Gson();
    }

    @Test
    public void testRatesAreRead() {
        getRateList();
    }

    private List<Rate> getRateList() {
        Rate[] array = gson.fromJson(json, Rate[].class);
        return Arrays.asList(array);
    }

    @Test
    public void testConversion() {
        RateConverter converter = new RateConverter(getRateList());
        System.out.println(converter.convert("USD", "GBP", 1)); // direct
        System.out.println(converter.convert("CAD", "GBP", 1)); // one jump
        System.out.println(converter.convert("CAD", "AUD", 1)); // two jumps
    }
}
