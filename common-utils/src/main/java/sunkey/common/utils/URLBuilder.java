package sunkey.common.utils;

import lombok.Getter;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author Sunkey
 * @since 2018-04-22 下午9:44
 **/
public class URLBuilder {

    public static final char PARAMETER_SIGN = '?';
    public static final char PARAMETER_SPLIT_CHAR = '&';

    @Getter
    private String url;
    @Getter
    private TreeMap<String, String> params;

    public URLBuilder(String url) {
        this(url, null);
    }

    public URLBuilder(String url, Map<String, ?> params) {
        this(url, params, null);
    }

    public URLBuilder(String url, Map<String, ?> params, Comparator<String> comparator) {
        Assert.notNull(url, "url");
        this.url = url;
        this.params = convertParams(params, comparator);
    }

    public URLBuilder addParam(String name, Object value) {
        if (value != null) {
            params.put(name, value == null ? "" : String.valueOf(value));
        }
        return this;
    }

    public URLBuilder encode() {
        return encode(false);
    }

    public URLBuilder encode(boolean encodeKey) {
        TreeMap<String, String> _params = new TreeMap<>();
        for (Map.Entry<String, String> entry : this.params.entrySet()) {
            _params.put(
                    encodeKey ? urlEncode(entry.getKey()) : entry.getKey(),
                    urlEncode(entry.getValue()));
        }
        this.params = _params;
        return this;
    }

    public static String urlEncode(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new Error();
        }
    }

    public String getParam(String name) {
        return params.get(name);
    }

    public String getParam(String name, String def) {
        String val = getParam(name);
        if (val == null) return def;
        return val;
    }

    public String getParamNotBlank(String name, String def) {
        String val = getParam(name);
        if (val == null || val.isEmpty()) return def;
        return val;
    }

    public static Map<String, String> fromQueryString(String queryString) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.hasText(queryString)) {
            if (queryString.charAt(0) == PARAMETER_SIGN) {
                queryString = queryString.substring(1);
            }
            List<String> split = sunkey.common.utils.StringUtils.split(queryString,
                    String.valueOf(PARAMETER_SPLIT_CHAR));
            for (String str : split) {
                int index1 = str.indexOf('=');
                if (index1 > -1) {
                    map.put(str.substring(0, index1), str.substring(index1 + 1));
                } else {
                    map.put(str, null);
                }
            }
        }
        return map;
    }

    /**
     * 如果url包含'?' 则parse url:params
     * 如果url不包含'?' 则parse url
     *
     * @param url
     * @return
     */
    public static URLBuilder parse(String url) {
        Assert.notNull(url, "url");
        int index = url.indexOf(PARAMETER_SIGN);
        if (index > -1) {
            String urlPre = url.substring(0, index);
            String urlPost = url.substring(index + 1);
            return new URLBuilder(urlPre, fromQueryString(urlPost));
        } else {
            return new URLBuilder(url);
        }
    }

    public String toString() {
        return toString(null);
    }

    public String toString(String prefix) {
        return toString(prefix, null);
    }

    public String toString(String prefix, String suffix) {
        StringBuilder sb = prefix == null ?
                new StringBuilder(url) : new StringBuilder(prefix).append(prefix);
        if (params != null && !params.isEmpty()) {
            boolean isStart = false;
            for (Map.Entry<String, String> e : params.entrySet()) {
                if (!isStart) {
                    isStart = true;
                    sb.append(PARAMETER_SIGN);
                } else {
                    sb.append(PARAMETER_SPLIT_CHAR);
                }
                sb.append(e.getKey()).append("=").append(e.getValue());
            }
        }
        if (suffix != null) {
            sb.append(suffix);
        }
        return sb.toString();
    }

    private TreeMap<String, String> convertParams(Map<String, ?> params,
                                                  Comparator<String> comparator) {
        TreeMap<String, String> result = comparator == null ?
                new TreeMap<>() : new TreeMap<>(comparator);
        if (params == null) return result;
        for (Map.Entry<String, ?> e : params.entrySet()) {
            String key = e.getKey();
            Object value = e.getValue();
            if (value != null) {
                result.put(key, String.valueOf(value));
            }
        }
        return result;
    }

}
