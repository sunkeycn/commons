package sunkey.common.utils.excel.valid;

import sunkey.common.utils.StringUtils;
import sunkey.common.utils.Template;

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
@Constraint(validator = Length.LengthValidator.class, reusable = true)
public @interface Length {

    int value() default -1;

    int min() default Integer.MIN_VALUE;

    int max() default Integer.MAX_VALUE;

    class LengthValidator implements ConstraintValidator<Length, String> {

        private static final Template template = Template.valueOf("应该介于{min}和{max}之间");

        @Override
        public void validate(String value, Length anno, ValidContext context) {
            if (StringUtils.isEmpty(value)) {
                return;
            }
            int min = anno.value() != -1 ? anno.value() : anno.min();
            int max = anno.value() != -1 ? anno.value() : anno.max();
            int length = value.length();
            if (length < min || length > max) {
                context.reportError(template.format(min, max));
            }
        }

    }

}
