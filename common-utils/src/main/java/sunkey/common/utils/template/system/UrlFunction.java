package sunkey.common.utils.template.system;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

public class UrlFunction implements Function {

    private String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private String urlDecode(String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Override
    public boolean acceptName(String functionName) {
        return "urle".equals(functionName) || "urld".equals(functionName);
    }

    @Override
    public String render(String functionName, String paramName, Map<String, ?> context) {
        Object value = context.get(paramName);
        if (value == null) return null;
        return "urle".equals(functionName) ?
                urlEncode(value.toString()) : urlDecode(value.toString());
    }

}