package sunkey.common.utils.template.system;

import java.util.Map;

public interface Extension {

    boolean accept(String expression);

    String render(String expression, Map<String, ?> context);

}