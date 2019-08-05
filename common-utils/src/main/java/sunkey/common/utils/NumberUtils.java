package sunkey.common.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author Sunkey
 * @since 2018-04-10 下午3:33
 **/
public class NumberUtils {

    public static Long parseLong(Object text) {
        Assert.notNull(text, "text");
        return Long.parseLong(String.valueOf(text));
    }

    public static Long parseLong(Object text, Long def) {
        if (text == null) return def;
        try {
            return Long.parseLong(String.valueOf(text));
        } catch (NumberFormatException ex) {
            return def;
        }
    }

    public static Byte parseByte(Object text) {
        Assert.notNull(text, "text");
        return Byte.parseByte(String.valueOf(text));
    }

    public static Byte parseByte(Object text, Byte def) {
        if (text == null) return def;
        try {
            return Byte.parseByte(String.valueOf(text));
        } catch (NumberFormatException ex) {
            return def;
        }
    }

    public static <T> T convertTo(String str, Class<? extends Number> type) {
        if (type == Integer.class || type == Integer.TYPE) {
            return (T) (Integer) Integer.parseInt(str);
        }
        if (type == Long.class || type == Long.TYPE) {
            return (T) (Long) Long.parseLong(str);
        }
        if (type == Float.class || type == Float.TYPE) {
            return (T) (Float) Float.parseFloat(str);
        }
        if (type == Double.class || type == Double.TYPE) {
            return (T) (Double) Double.parseDouble(str);
        }
        if (type == BigDecimal.class) {
            return (T) new BigDecimal(str);
        }
        if (type == BigInteger.class) {
            return (T) new BigInteger(str);
        }
        if (type == Short.class || type == Short.TYPE) {
            return (T) (Short) Short.parseShort(str);
        }
        if (type == Byte.class || type == Byte.TYPE) {
            return (T) (Byte) Byte.parseByte(str);
        }
        return null;
    }

    public static <T> T convertTo(Number number, Class<? extends Number> type) {
        if (number.getClass() == type) {
            return (T) number;
        }
        if (type == Integer.class || type == Integer.TYPE) {
            return (T) (Integer) number.intValue();
        }
        if (type == Long.class || type == Long.TYPE) {
            return (T) (Long) number.longValue();
        }
        if (type == Float.class || type == Float.TYPE) {
            return (T) (Float) number.floatValue();
        }
        if (type == Double.class || type == Double.TYPE) {
            return (T) (Double) number.doubleValue();
        }
        if (type == BigDecimal.class) {
            return (T) new BigDecimal(number.toString());
        }
        if (type == BigInteger.class) {
            return (T) new BigInteger(number.toString());
        }
        if (type == Short.class || type == Short.TYPE) {
            return (T) (Short) number.shortValue();
        }
        if (type == Byte.class || type == Byte.TYPE) {
            return (T) (Byte) number.byteValue();
        }
        return null;
    }


}
