package sunkey.common.utils.excel.valid;

import sunkey.common.utils.Template;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Sunkey
 * @since 2019-06-11 16:51
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validator = Range.RangeValidator.class)
public @interface Range {

    int min() default 0;

    int max() default Integer.MAX_VALUE;

    class RangeValidator implements ConstraintValidator<Range, Number> {

        private static final Template template = Template.valueOf("应该介于{min}和{max}之间");

        @Override
        public void validate(Number value, Range anno, ValidContext context) {
            if (value == null) return;
            if (anno.min() > value.intValue() || anno.max() < value.intValue()) {
                context.reportError(template.format(anno.min(), anno.max()));
            }
        }

    }

}
