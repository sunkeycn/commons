package sunkey.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author Sunkey
 * @since 2018-03-16 16:34
 **/
public class TimeParser {

    public static final long SECOND = 1000L;
    public static final long MINUTE = SECOND * 60L;
    public static final long HOUR = MINUTE * 60L;
    public static final long DAY = HOUR * 24L;

    @Deprecated
    public static final String F_TIME = "yyyy-MM-dd HH:mm:ss";
    @Deprecated
    public static final String F_DATE = "yyyy-MM-dd";

    @Deprecated
    public static Date parse(String date, String fmt) throws ParseException {
        return new SimpleDateFormat(fmt).parse(date);
    }

    @Deprecated
    public static String format(Date date, String fmt) {
        return new SimpleDateFormat(fmt).format(date);
    }

    @Deprecated
    public static Date convert(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Deprecated
    public static LocalDateTime convert(Date date) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
    }

    // Unchecked Range
    public static int charToInt(char c) {
        return c - '0';
    }

    public static boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }

    static final int DEFAULT_RADIX = 10;

    static long calculateValue(long val, String dw) {
        switch (dw) {
            case "d":// Day
            case "D":
                return val * DAY;
            case "h":// Hour
            case "H":
                return val * HOUR;
            case "m":// Minute
            case "M":
                return val * MINUTE;
            case "s":// Second
            case "S":
                return val * SECOND;
            case "ms":
            case "MS":
                return val;
            default:
                return 0;
        }
    }

    /**
     * Expression: [1d/D][1h/H][1m/M][1s/S][1[ms/MS]] 天 小时 分钟 秒 毫秒
     *
     * @author Sunkey
     */
    public static long rangeToMillis(String ex) {
        char[] crs = ex.toCharArray();
        long millis = 0;
        long currentPos = 0;
        StringBuilder currentDw = null;
        for (int i = 0; i < crs.length; i++) {
            if (isNumber(crs[i])) {
                if (currentDw != null) {
                    String currDw = currentDw.toString();
                    currentDw = null;
                    // calculate millis
                    millis += calculateValue(currentPos, currDw);
                    currentPos = 0;
                }
                currentPos = currentPos * DEFAULT_RADIX + charToInt(crs[i]);
            } else {
                if (currentDw == null) {
                    currentDw = new StringBuilder();
                }
                // clear the number
                currentDw.append(crs[i]);
            }
        }
        if (currentDw != null) {
            millis += calculateValue(currentPos, currentDw.toString());
        } else if (currentPos != 0) {
            millis += calculateValue(currentPos, "ms");
        }
        return millis;
    }

    public static int rangeToSeconds(String ex) {
        return (int) (rangeToMillis(ex) / SECOND);
    }

    public static int rangeToMinutes(String ex) {
        return (int) (rangeToMillis(ex) / MINUTE);
    }

    public static int rangeToHours(String ex) {
        return (int) (rangeToMillis(ex) / HOUR);
    }

    public static int rangeToDays(String ex) {
        return (int) (rangeToMillis(ex) / DAY);
    }

    public static TimeRange range(String exp) {
        return new TimeRange(exp);
    }

    public static class TimeRange {

        private final String exp;
        private final long millis;

        public TimeRange(String exp) {
            if (exp == null) throw new IllegalArgumentException("exp");
            this.exp = exp;
            this.millis = TimeParser.rangeToMillis(exp);
        }

        public String getExpression() {
            return exp;
        }

        public long rangeToMillis() {
            return millis;
        }

        public int rangeToSeconds() {
            return (int) (millis / TimeParser.SECOND);
        }

        public int rangeToMinutes() {
            return (int) (millis / TimeParser.MINUTE);
        }

        public int rangeToHours() {
            return (int) (millis / TimeParser.HOUR);
        }

        public int rangeToDays() {
            return (int) (millis / TimeParser.DAY);
        }

        @Override
        public String toString() {
            return exp;
        }
    }
}
