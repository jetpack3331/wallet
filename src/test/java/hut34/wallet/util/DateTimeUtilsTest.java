package hut34.wallet.util;

import org.junit.After;
import org.junit.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class DateTimeUtilsTest {

    @After
    public void after() {
        DateTimeUtils.setClockSystem();
    }

    @Test
    public void toStartOfDay_willRemoveTime() {
        OffsetDateTime original = OffsetDateTime.parse("2017-06-01T11:12:13+10:00");

        OffsetDateTime result = DateTimeUtils.toStartOfDay(original);

        assertThat(result.toString()).isEqualTo("2017-06-01T00:00+10:00");
    }

    @Test
    public void now_willReturnFixedTime_whenClockSetFixed() {
        OffsetDateTime fixedNow = OffsetDateTime.parse("2017-06-01T11:12:13+10:00");
        DateTimeUtils.setClockTime(fixedNow);

        assertThat(DateTimeUtils.now()).isEqualTo(fixedNow);
    }

}
