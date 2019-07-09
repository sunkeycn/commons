package sunkey.common.utils.excel.support;

import sunkey.common.utils.Template;
import sunkey.common.utils.excel.valid.ValidResult;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sunkey
 * @since 2019-06-12 14:11
 **/
public class DefaultErrorFormatter implements ErrorFormatter {

    public static final DefaultErrorFormatter INSTANCE =
            new DefaultErrorFormatter("第{lineNo}行[{fieldName}]:{message}");

    private final Template template;

    public DefaultErrorFormatter(String template) {
        this.template = Template.valueOf(template);
    }

    @Override
    public String format(ValidResult.Error error) {
        Map<String, Object> params = new HashMap<>();
        params.put("lineNo", error.getLineNo() + 1);
        params.put("fieldName", error.getFieldName());
        params.put("message", error.getMessage());
        params.put("target", error.getTarget());
        return template.render(params);
    }

}
