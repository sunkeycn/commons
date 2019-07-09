package sunkey.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @author Sunkey
 * @since 2019-04-12 10:34
 **/
public class URLs {

    public static String encode(String str) {
        return encode(str, "UTF-8");
    }

    public static String decode(String str) {
        return decode(str, "UTF-8");
    }

    public static String encode(String str, String charset) {
        try {
            return URLEncoder.encode(str, charset);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("unknown charset " + charset);
        }
    }

    public static String decode(String str, String charset) {
        try {
            return URLDecoder.decode(str, charset);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("unknown charset " + charset);
        }
    }

}
