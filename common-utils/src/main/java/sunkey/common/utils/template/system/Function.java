package sunkey.common.utils.template.system;

import java.util.Map;

public interface Function extends Extension {

    char SPLITTER = ':';

    default boolean accept(String expression) {
        String functionName = getFunctionName(expression);
        return functionName != null && acceptName(functionName);
    }

    boolean acceptName(String functionName);

    default String render(String expression, Map<String, ?> context) {
        return render(getFunctionName(expression), getParamName(expression), context);
    }

    String render(String functionName, String paramName, Map<String, ?> context);

    // {urle:time}
    // urle : functionName
    // time : paramName
    static String getParamName(String expression) {
        if (expression == null || expression.isEmpty()) return null;
        int idx = expression.indexOf(Function.SPLITTER);
        if (idx <= 0) return null;
        return expression.substring(expression.indexOf(Function.SPLITTER) + 1);
    }

    static String getFunctionName(String expression) {
        if (expression == null || expression.isEmpty()) return null;
        int idx = expression.indexOf(Function.SPLITTER);
        if (idx <= 0) return null;
        return expression.substring(0, idx);
    }

}