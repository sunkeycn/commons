package sunkey.common.utils.excel.valid;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.annotation.Annotation;

/**
 * @author Sunkey
 * @since 2019-06-11 16:57
 **/
@Getter
@Setter
@ToString
public class ValidContext {

    private boolean success = true;
    private ValidResult result;
    private Object target;
    private Annotation annotation;
    private int lineNo;
    private String fieldName;

    public void reportError(String template) {
        success = false;
        result.addError(template, this);
    }

}
