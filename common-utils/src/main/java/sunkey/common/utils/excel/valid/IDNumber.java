package sunkey.common.utils.excel.valid;

import sunkey.common.utils.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Sunkey
 * @since 2019-07-31 16:58
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validator = IDNumber.IDNumberValidator.class, reusable = true)
public @interface IDNumber {

    class IDNumberValidator implements ConstraintValidator<IDNumber, String> {

        @Override
        public void validate(String value, IDNumber anno, ValidContext context) {
            if (StringUtils.isEmpty(value)) {
                return;
            }
            if (value.length() != 18 && value.length() != 15) {
                context.reportError("身份证号格式错误");
                return;
            }
            char[] chars = value.toCharArray();
            for (int i = 0; i < chars.length - 1; i++) {
                if (chars[i] < '0' || chars[i] > '9') {
                    context.reportError("身份证号格式错误");
                    return;
                }
            }
            char lastChar = chars[chars.length - 1];
            if ((lastChar >= '0' && lastChar <= '9') || lastChar == 'x' || lastChar == 'X') {
                // legal
            } else {
                context.reportError("身份证号格式错误");
            }
        }
    }

}
