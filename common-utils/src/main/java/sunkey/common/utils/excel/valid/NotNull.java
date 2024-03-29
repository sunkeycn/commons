package sunkey.common.utils.excel.valid;

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
@Constraint(validator = NotNull.NotNullValidator.class, reusable = true)
public @interface NotNull {

    class NotNullValidator implements ConstraintValidator<NotNull, Object> {

        @Override
        public void validate(Object value, NotNull anno, ValidContext context) {
            if (value == null) {
                context.reportError("不能为空");
            }
        }

    }

}
