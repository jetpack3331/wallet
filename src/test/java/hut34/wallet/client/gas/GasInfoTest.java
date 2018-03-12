package hut34.wallet.client.gas;

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class GasInfoTest {

    @Test
    public void getAveragePrice_willConvertFrom10Gwei() {
        GasInfo gasInfo = new GasInfo();
        gasInfo.setAverage(new BigDecimal("20.1"));

        assertThat(gasInfo.getAveragePrice(), is("2010000000"));
    }

    @Test
    public void getAveragePrice_willTruncateIrrelevantDigits_whenPrecisionTooLow() {
        GasInfo gasInfo = new GasInfo();
        gasInfo.setAverage(new BigDecimal("20.19999999999999999999999"));
        assertThat(gasInfo.getAveragePrice(), is("2019999999"));
    }

    @Test
    public void getAveragePrice_willReturnNull_whenSourceNull() {
        GasInfo gasInfo = new GasInfo();
        assertThat(gasInfo.getAveragePrice(), nullValue());
    }

}
