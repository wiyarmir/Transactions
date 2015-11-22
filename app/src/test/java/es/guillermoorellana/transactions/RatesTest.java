package es.guillermoorellana.transactions;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class RatesTest {
    @Test
    public void testConversion() {
        RateConverter converter = new RateConverter(TestDataRepository.getRateList());
        assertThat(converter.convert("USD", "USD", 1), is(equalTo(1.0f))); // own
        assertThat(converter.convert("USD", "GBP", 1), is(equalTo(0.77f))); // direct
        assertThat(converter.convert("CAD", "GBP", 1), is(equalTo(0.7084f))); // one jump
        assertThat(converter.convert("CAD", "AUD", 1), is(equalTo(0.587972f))); // two jumps
    }
}