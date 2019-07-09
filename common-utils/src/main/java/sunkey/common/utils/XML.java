package sunkey.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sunkey
 * @since 2018-12-29 下午7:50
 **/
public class XML {

    private static final XmlMapper xmlMapper = new XmlMapper();

    static {
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        xmlMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        xmlMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
        xmlMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS.NON_NULL);
        xmlMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public static <T> T parseObject(String xml, Class<T> type) {
        try {
            return xmlMapper.readValue(xml, type);
        } catch (Throwable var3) {
            throw new ParseException(var3.getMessage(), var3);
        }
    }

    public static <T> List<T> parseList(String text, Class<T> type) {
        return (List) parseObject(text, xmlMapper.getTypeFactory().constructCollectionType(ArrayList.class, type));
    }

    public static Map<String, Object> parseMap(String text) {
        return parseMap(text, String.class, Object.class);
    }

    public static <K, V> Map<K, V> parseMap(String text, Class<K> k, Class<V> v) {
        return (Map) parseObject(text, xmlMapper.getTypeFactory().constructMapType(HashMap.class, k, v));
    }

    public static <T> T parseObject(String text, JavaType type) throws JSON.JSONException {
        try {
            return xmlMapper.readValue(text, type);
        } catch (Throwable var3) {
            throw new ParseException(var3.getMessage(), var3);
        }
    }

    public static String toXML(Object object) {
        try {
            return xmlMapper.writeValueAsString(object);
        } catch (Throwable var2) {
            throw new ParseException(var2.getMessage(), var2);
        }
    }

    public static void writeXML(OutputStream out, Object object) {
        try {
            xmlMapper.writeValue(out, object);
        } catch (Throwable var3) {
            throw new ParseException(var3.getMessage(), var3);
        }
    }

    public static class ParseException extends RuntimeException {
        public ParseException() {
        }

        public ParseException(String message) {
            super(message);
        }

        public ParseException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
