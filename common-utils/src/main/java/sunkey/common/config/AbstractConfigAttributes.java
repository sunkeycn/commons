package sunkey.common.config;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

/**
 * @author Sunkey
 * @since 2018-04-14 上午11:16
 **/
public abstract class AbstractConfigAttributes implements ConfigAttributes {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    protected abstract <T> T getConfigAs(String key, Class<T> requireType);

    protected final <T> T getConfig(String key, Class<T> requireType) {
        T result = getConfigAs(key, requireType);
        if (result == null) {
            throw new ConfigPropertyNotExistsException(getName() + "[" + key + "]");
        }
        return result;
    }

    protected final <T> T getConfig(String key, Class<T> requireType, T def) {
        T result = getConfigAs(key, requireType);
        if (result != null) return result;
        return def;
    }

    @Override
    public int getInt(String key) throws ConfigPropertyNotExistsException {
        return getConfig(key, Integer.class);
    }

    @Override
    public Integer getInteger(String key, Integer def) {
        return getConfig(key, Integer.class, def);
    }

    @Override
    public BigInteger getBigInteger(String key) throws ConfigPropertyNotExistsException {
        return getConfig(key, BigInteger.class);
    }

    @Override
    public BigInteger getBigInteger(String key, BigInteger def) {
        return getConfig(key, BigInteger.class, def);
    }

    @Override
    public BigDecimal getBigDecimal(String key) throws ConfigPropertyNotExistsException {
        return getConfig(key, BigDecimal.class);
    }

    @Override
    public BigDecimal getBigDecimal(String key, BigDecimal def) {
        return getConfig(key, BigDecimal.class, def);
    }

    @Override
    public byte getByte(String key) throws ConfigPropertyNotExistsException {
        return getConfig(key, Byte.class);
    }

    @Override
    public Byte getByte(String key, Byte def) {
        return getConfig(key, Byte.class, def);
    }

    @Override
    public short getShort(String key) throws ConfigPropertyNotExistsException {
        return getConfig(key, Short.class);
    }

    @Override
    public Short getShort(String key, Short def) {
        return getConfig(key, Short.class, def);
    }

    @Override
    public long getLong(String key) throws ConfigPropertyNotExistsException {
        return getConfig(key, Long.class);
    }

    @Override
    public Long getLong(String key, Long def) {
        return getConfig(key, Long.class, def);
    }

    @Override
    public boolean getBoolean(String key) throws ConfigPropertyNotExistsException {
        return getConfig(key, Boolean.class);
    }

    @Override
    public Boolean getBoolean(String key, Boolean def) {
        return getConfig(key, Boolean.class, def);
    }

    @Override
    public float getFloat(String key) throws ConfigPropertyNotExistsException {
        return getConfig(key, Float.class);
    }

    @Override
    public Float getFloat(String key, Float def) {
        return getConfig(key, Float.class, def);
    }

    @Override
    public double getDouble(String key) throws ConfigPropertyNotExistsException {
        return getConfig(key, Double.class);
    }

    @Override
    public Double getDouble(String key, Double def) {
        return getConfig(key, Double.class, def);
    }

    @Override
    public String getString(String key) throws ConfigPropertyNotExistsException {
        return getConfig(key, String.class);
    }

    @Override
    public String getString(String key, String def) {
        return getConfig(key, String.class, def);
    }

    @Override
    public Date getDate(String key) throws ConfigPropertyNotExistsException {
        return getDate(key, DEFAULT_DATE_FORMAT);
    }

    @Override
    public Date getDate(String key, String format) throws ConfigPropertyNotExistsException {
        String config = getConfig(key, String.class);
        try {
            return new SimpleDateFormat(format).parse(config);
        } catch (ParseException e) {
            throw new ConfigPropertyNotExistsException(getName() + "[" + key + "](cannot cast)");
        }
    }

    @Override
    public Date getDate(String key, Date def) {
        return getDate(key, DEFAULT_DATE_FORMAT, def);
    }

    @Override
    public Date getDate(String key, String format, Date def) {
        try {
            return getDate(key, format);
        } catch (ConfigPropertyNotExistsException ex) {
            return def;
        }
    }

    @Override
    public Duration getDuration(String key) throws ConfigPropertyNotExistsException {
        String config = getConfig(key, String.class);
        return Duration.parse(config);
    }

    @Override
    public Duration getDuration(String key, String def) {
        String config = getConfig(key, String.class, def);
        if (config != null) return Duration.parse(config);
        return null;
    }

}
