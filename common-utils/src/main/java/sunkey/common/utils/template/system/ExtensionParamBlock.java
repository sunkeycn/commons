package sunkey.common.utils.template.system;

import java.util.Map;

public class ExtensionParamBlock extends ParamBlock {

    private final Extension extension;

    public ExtensionParamBlock(String str, Extension extension) {
        super(str);
        this.extension = extension;
    }

    @Override
    public String render(Map<String, ?> data) {
        if (extension != null) {
            String result = extension.render(expression, data);
            if (result != null) return result;
            for (String alias : aliases) {
                result = extension.render(expression, data);
                if (result != null) return result;
            }
        }
        return super.render(data);
    }
}