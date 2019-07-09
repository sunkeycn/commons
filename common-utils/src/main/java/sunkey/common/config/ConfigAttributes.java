package sunkey.common.config;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.util.Date;

/**
 * @author Sunkey
 * @since 2018-04-14 上午11:09
 **/
public interface ConfigAttributes {

    String getName();

    int getInt(String key) throws ConfigPropertyNotExistsException;

    Integer getInteger(String key, Integer def);

    BigInteger getBigInteger(String key) throws ConfigPropertyNotExistsException;

    BigInteger getBigInteger(String key, BigInteger def);

    BigDecimal getBigDecimal(String key) throws ConfigPropertyNotExistsException;

    BigDecimal getBigDecimal(String key, BigDecimal def);

    byte getByte(String key) throws ConfigPropertyNotExistsException;

    Byte getByte(String key, Byte def);

    short getShort(String key) throws ConfigPropertyNotExistsException;

    Short getShort(String key, Short def);

    long getLong(String key) throws ConfigPropertyNotExistsException;

    Long getLong(String key, Long def);

    boolean getBoolean(String key) throws ConfigPropertyNotExistsException;

    Boolean getBoolean(String key, Boolean def);

    float getFloat(String key) throws ConfigPropertyNotExistsException;

    Float getFloat(String key, Float def);

    double getDouble(String key) throws ConfigPropertyNotExistsException;

    Double getDouble(String key, Double def);

    String getString(String key) throws ConfigPropertyNotExistsException;

    String getString(String key, String def);

    Date getDate(String key) throws ConfigPropertyNotExistsException;

    Date getDate(String key, String format) throws ConfigPropertyNotExistsException;

    Date getDate(String key, Date def);

    Date getDate(String key, String format, Date def);

    Duration getDuration(String key) throws ConfigPropertyNotExistsException;

    Duration getDuration(String key, String def);

}
