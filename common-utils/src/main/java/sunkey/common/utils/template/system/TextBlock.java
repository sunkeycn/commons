package sunkey.common.utils.template.system;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
public class TextBlock implements Block {
    @Getter
    protected String expression;

    public String render(Map<String, ?> data) {
        return expression;
    }

    @Override
    public String toString() {
        return expression;
    }
}