package hut34.wallet.util;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public final class DateTimeUtils {
    private static Clock clock = Clock.systemDefaultZone();

    private DateTimeUtils() {
        //static
    }

    public static OffsetDateTime toStartOfDay(OffsetDateTime dateTime) {
        return dateTime.truncatedTo(ChronoUnit.DAYS);
    }

    /**
     * Allows static mocking of {@link Clock} for tests without having to pollute our code with clock injections
     * everywhere. The hackiness of static in this case is better than code pollution.
     */
    public static OffsetDateTime now() {
        return OffsetDateTime.now(DateTimeUtils.clock);
    }

    public static OffsetDateTime now(ZoneId zoneId) {
        return OffsetDateTime.now(clock.withZone(zoneId));
    }

    public static void setClockTime(OffsetDateTime zonedDateTime) {
        setClockTime(zonedDateTime, ZoneId.systemDefault());
    }

    public static void setClockTime(OffsetDateTime zonedDateTime, ZoneId zoneId) {
        setClock(Clock.fixed(zonedDateTime.toInstant(), zoneId));
    }

    public static void setClock(Clock clock) {
        DateTimeUtils.clock = clock;
    }

    public static void setClockSystem() {
        DateTimeUtils.clock = Clock.systemDefaultZone();
    }

}
