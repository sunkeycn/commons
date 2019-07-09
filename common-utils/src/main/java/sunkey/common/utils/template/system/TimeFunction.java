package sunkey.common.utils.template.system;

import sunkey.common.utils.Dates;
import sunkey.common.utils.StringUtils;

import java.util.Map;

/**
 * @author Sunkey
 * @since 2019-04-10 18:34
 **/
public class TimeFunction implements Function {

    @Override
    public boolean acceptName(String functionName) {
        return functionName.startsWith("time-");
    }

    @Override
    public String render(String functionName, String paramName, Map<String, ?> context) {
        long time = 0L;
        if (paramName.isEmpty()) {
            time = System.currentTimeMillis();
        } else {
            Object val = context.get(paramName);
            if (val == null || !(val instanceof Long)) {
                return null;
            }
            time = (Long) val;
        }
        String fmt = StringUtils.substringAfter(functionName, "time-");
        return Dates.format(time, fmt);
    }

}
