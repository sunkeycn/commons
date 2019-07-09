package sunkey.common.utils.template.system;

import java.util.Map;

public interface Block {
    String getExpression();

    String render(Map<String, ?> data);
}
