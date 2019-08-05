package sunkey.common.utils.excel.valid;

import sunkey.common.utils.StringUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sunkey
 * @since 2019-07-31 16:58
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validator = RegExp.RegExpValidator.class, reusable = false)
public @interface RegExp {

    String value();

    class RegExpValidator implements ConstraintValidator<RegExp, String> {

        private Pattern pattern = null;

        @Override
        public void validate(String value, RegExp anno, ValidContext context) {
            if (StringUtils.isEmpty(value)) {
                return;
            }
            if (pattern == null) {
                pattern = Pattern.compile(anno.value());
            }
            Matcher matcher = pattern.matcher(value);
            if (!matcher.matches()) {
                context.reportError("规则不匹配[" + pattern.toString() + "]");
            }
        }
    }

}
