package sunkey.common.utils.excel.valid;

import sunkey.common.utils.Template;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

/**
 * @author Sunkey
 * @since 2019-06-11 16:51
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Constraint(validator = In.InValidator.class)
public @interface In {

    String[] value();

    class InValidator implements ConstraintValidator<In, String> {

        private static final Template template = Template.valueOf("只能是{array}");

        @Override
        public void validate(String value, In anno, ValidContext context) {
            if (value == null) return;
            String[] array = anno.value();
            boolean contains = false;
            for (int i = 0; i < array.length; i++) {
                if (array[i].equals(value)) {
                    contains = true;
                }
            }
            if (!contains) {
                context.reportError(template.format(Arrays.toString(array)));
            }
        }

    }

}
