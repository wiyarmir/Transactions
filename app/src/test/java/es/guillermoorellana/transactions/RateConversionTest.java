package es.guillermoorellana.transactions;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class RateConversionTest {
    @Test
    public void testConversionSelf() {
        RateConverter converter = new RateConverter(TestDataRepository.getRateList());
        assertThat(converter.convert("USD", "USD", 1), is(equalTo(1.0f)));
    }
    @Test
    public void testConversionDirect() {
        RateConverter converter = new RateConverter(TestDataRepository.getRateList());
        assertThat(converter.convert("USD", "GBP", 1), is(equalTo(0.77f)));
    }
    @Test
    public void testConversionJumpOne() {
        RateConverter converter = new RateConverter(TestDataRepository.getRateList());
        assertThat(converter.convert("CAD", "GBP", 1), is(equalTo(0.7084f)));
    }
    @Test
    public void testConversionJumpTwo() {
        RateConverter converter = new RateConverter(TestDataRepository.getRateList());
        assertThat(converter.convert("CAD", "AUD", 1), is(equalTo(0.587972f)));
    }
}