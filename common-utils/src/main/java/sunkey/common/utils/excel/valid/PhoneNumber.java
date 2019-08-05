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
@Constraint(validator = PhoneNumber.PhoneNumberValidator.class, reusable = true)
public @interface PhoneNumber {

    int length() default 11;

    String[] prefix() default {"+86", "086", "86"};

    class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

        @Override
        public void validate(String value, PhoneNumber anno, ValidContext context) {
            if (StringUtils.isEmpty(value)) {
                return;
            }
            if (value.length() != anno.length()) {
                for (String prefix : anno.prefix()) {
                    if (value.startsWith(prefix)) {
                        value = value.substring(prefix.length());
                        break;
                    }
                }
            }
            if (value.length() != anno.length()) {
                context.reportError("手机号格式错误");
                return;
            }
            for (char c : value.toCharArray()) {
                if (c < '0' || c > '9') {
                    context.reportError("手机号格式错误");
                    return;
                }
            }
        }
    }

}
