package sunkey.common.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sunkey
 * @since 2019-02-20 下午1:34
 **/
public class Dates {

    private static final ZoneOffset defaultOffset = OffsetDateTime.now().getOffset();

    private static final ConcurrentHashMap<String, DateTimeFormatter> caches = new ConcurrentHashMap<>();

    public static LocalDateTime parse(String content, String format) {
        Assert.notNull(content, "content");
        Assert.notNull(format, "format");
        return LocalDateTime.from(getFormatter(format).parse(content));
    }

    public static DateTimeFormatter getFormatter(String format) {
        return caches.computeIfAbsent(format, DateTimeFormatter::ofPattern);
    }

    public static LocalDateTime startOfDay() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
    }

    public static LocalDateTime startOfDay(long millis) {
        return LocalDateTime.of(LocalDate.from(Dates.datetime(millis)), LocalTime.MIN);
    }

    public static long toMillis(LocalDateTime time) {
        Assert.notNull(time, "time");
        return time.toInstant(defaultOffset).toEpochMilli();
    }

    public static long toMillis(ZonedDateTime time) {
        Assert.notNull(time, "time");
        return time.toInstant().toEpochMilli();
    }

    public static long toMillis(TemporalAccessor time) {
        Assert.notNull(time, "time");
        if (time instanceof Instant) {
            return ((Instant) time).toEpochMilli();
        }
        if (time instanceof LocalDateTime) {
            return toMillis((LocalDateTime) time);
        }
        if (time instanceof ZonedDateTime) {
            return toMillis((ZonedDateTime) time);
        }
        return toMillis(LocalDateTime.from(time));
    }

    public static LocalDateTime endOfDay() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    }

    public static LocalDateTime endOfDay(long millis) {
        return LocalDateTime.of(LocalDate.from(Dates.datetime(millis)), LocalTime.MAX);
    }

    public static ZonedDateTime datetime(long millis) {
        return Instant.ofEpochMilli(millis).atZone(defaultOffset);
    }

    public static LocalDateTime localDateTime(long millis) {
        return LocalDateTime.from(datetime(millis));
    }

    public static String format(Date date, String format) {
        Assert.notNull(date, "date");
        Assert.notNull(format, "format");
        return getFormatter(format).format(date.toInstant().atZone(defaultOffset));
    }

    public static String format(long millis, String format) {
        Assert.notNull(format, "format");
        return getFormatter(format).format(datetime(millis));
    }

    public static String format(TemporalAccessor date, String format) {
        Assert.notNull(date, "date");
        Assert.notNull(format, "format");
        return getFormatter(format).format(date);
    }

    public static long millisFromStartOfDay(long millis) {
        return millis - Dates.toMillis(Dates.startOfDay(millis));
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static long parseAsMillis(String content, String format) {
        Assert.notNull(content, "content");
        Assert.notNull(format, "format");
        return toMillis(parse(content, format));
    }

    public static Date parseAsDate(String content, String format) {
        Assert.notNull(content, "content");
        Assert.notNull(format, "format");
        return new Date(parseAsMillis(content, format));
    }

    public static Date toClassicDate(TemporalAccessor date) {
        Assert.notNull(date, "date");
        return new Date(toMillis(date));
    }

    public static LocalDateTime toDate(Date classic) {
        Assert.notNull(classic, "classic");
        return localDateTime(classic.getTime());
    }

}
