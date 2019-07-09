package sunkey.common.utils;

import org.springframework.util.StreamUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sunkey
 * @since 2019-04-11 11:25
 **/
public class ServletUtils {

    public static ServletRequestAttributes currentRequestAttributes() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes)
            return (ServletRequestAttributes) requestAttributes;
        return null;
    }

    public static HttpServletRequest currentRequest() {
        return currentRequestAttributes().getRequest();
    }

    public static HttpServletResponse currentResponse() {
        return currentRequestAttributes().getResponse();
    }

    public static Map<String, String> getParametersAsMap(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Enumeration<String> e = request.getParameterNames();
        while (e.hasMoreElements()) {
            String key = e.nextElement();
            params.put(key, request.getParameter(key));
        }
        return params;
    }

    public static String readStreamAsString(HttpServletRequest request) throws IOException {
        return StreamUtils.copyToString(request.getInputStream(),
                Charset.forName(request.getCharacterEncoding()));
    }

}
