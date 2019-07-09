package sunkey.common.utils.dev;

import sunkey.common.utils.Naming;
import sunkey.common.utils.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sunkey
 * @since 2018-03-22 17:00
 **/
public class BeanPrintUtils {

    public static String setters(Class<?> type, String paramName) {
        StringBuilder sb = new StringBuilder();
        for (Method m : type.getMethods()) {
            if (m.getName().startsWith("set") && m.getParameterCount() == 1) {
                sb.append(paramName + "." + m.getName() + "();" + System.lineSeparator());
            }
        }
        return sb.toString();
    }

    public static String getters(Class<?> type, String paramName) {
        StringBuilder sb = new StringBuilder();
        for (Method m : type.getMethods()) {
            if (m.getName().startsWith("get") && !m.getName().equals("getClass") && m.getParameterCount() == 0) {
                sb.append(paramName + "." + m.getName() + "();" + System.lineSeparator());
            }
        }
        return sb.toString();
    }

    public static String gen(Class<?> srcType, Class<?> destType, String srcParamName, String destParamName) {

        Map<String, String> srcMethods = new HashMap<>();
        Map<String, String> srcUnderlineMethods = new HashMap<>();

        for (Method m : srcType.getMethods()) {
            if (m.getName().startsWith("get") && !m.getName().equals("getClass") && m.getParameterCount() == 0) {
                String val = srcParamName + "." + m.getName() + "()";
                srcMethods.put(m.getName().substring(3).toLowerCase(), val);
                String name = StringUtils.firstToLowerCase(m.getName().substring(3));
                String snakeName = Naming.camelToSnake(name).toLowerCase();
                srcUnderlineMethods.put(snakeName, val);
            }
        }
        List<String> noParamMethods = new ArrayList<>();
        StringBuilder result = new StringBuilder();
        for (Method m : destType.getMethods()) {
            if (m.getName().startsWith("set") && m.getParameterCount() == 1) {
                String key = m.getName().substring(3).toLowerCase();
                String val = srcMethods.get(key);
                if (val != null) {
                    result.append(destParamName + "." + m.getName() + "(" + val + ");").append(System.lineSeparator());
                } else {
                    val = srcUnderlineMethods.get(key);
                    if (val != null) {
                        result.append(destParamName + "." + m.getName() + "(" + val + ");").append(System.lineSeparator());
                    } else {
                        key = Naming.camelToSnake(StringUtils.firstToLowerCase(m.getName().substring(3)));

                        val = srcMethods.get(key);
                        if (val != null) {
                            result.append(destParamName + "." + m.getName() + "(" + val + ");").append(System.lineSeparator());
                        } else {
                            val = srcUnderlineMethods.get(key);
                            if (val != null) {
                                result.append(destParamName + "." + m.getName() + "(" + val + ");").append(System.lineSeparator());
                            } else {
                                noParamMethods.add(destParamName + "." + m.getName() + "();");
                            }
                        }
                    }
                }
            }
        }
        for (String str : noParamMethods) {
            result.append(str).append(System.lineSeparator());
        }
        return result.toString();
    }

}
