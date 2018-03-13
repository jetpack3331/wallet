package hut34.wallet.client.gas;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.contrib.gae.util.Nulls;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.*;

// Full details here: https://ethgasstation.info/json/ethgasAPI.json
// Only added fields we need for now. Prices seem to come back in gwei.
public class GasInfo {
    @JsonProperty(access = WRITE_ONLY)
    private BigDecimal average;
    @JsonProperty(access = WRITE_ONLY)
    private BigDecimal safeLow;
    @JsonProperty(access = WRITE_ONLY)
    private BigDecimal fast;
    @JsonProperty(access = WRITE_ONLY)
    private BigDecimal fastest;

    public BigDecimal getAverage() {
        return average;
    }

    public String getAveragePrice() {
        return toWei(average);
    }

    public void setAverage(BigDecimal average) {
        this.average = average;
    }

    public BigDecimal getSafeLow() {
        return safeLow;
    }

    public String getSafeLowPrice() {
        return toWei(safeLow);
    }

    public void setSafeLow(BigDecimal safeLow) {
        this.safeLow = safeLow;
    }

    public BigDecimal getFast() {
        return fast;
    }

    public String getFastPrice() {
        return toWei(fast);
    }

    public void setFast(BigDecimal fast) {
        this.fast = fast;
    }

    public BigDecimal getFastest() {
        return fastest;
    }

    public void setFastest(BigDecimal fastest) {
        this.fastest = fastest;
    }

    public String getFastestPrice() {
        return toWei(fastest);
    }

    // For some unknown reason the gas api returns with units of 1/10 gwei https://github.com/ethgasstation/ethgasstation-backend/issues/5
    private String toWei(BigDecimal source) {
        return Nulls.ifNotNull(source, s -> s.scaleByPowerOfTen(8).setScale(0, RoundingMode.FLOOR).toString());
    }

}
