package es.guillermoorellana.transactions;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class RatesTest {
    @Test
    public void testConversion() {
        RateConverter converter = new RateConverter(TestDataRepository.getRateList());
        System.out.println(converter.convert("USD", "GBP", 1)); // direct
        System.out.println(converter.convert("CAD", "GBP", 1)); // one jump
        System.out.println(converter.convert("CAD", "AUD", 1)); // two jumps
    }
}
