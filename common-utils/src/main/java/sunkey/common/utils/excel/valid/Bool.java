package sunkey.common.utils.excel.valid;

import sunkey.common.utils.StringUtils;

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
@Constraint(validator = Bool.BoolValidator.class, reusable = true)
public @interface Bool {

    class BoolValidator implements ConstraintValidator<Bool, String> {

        @Override
        public void validate(String value, Bool anno, ValidContext context) {
            if (StringUtils.isEmpty(value)) {
                return;
            }
            if (!value.equals("是") && !value.equals("否")) {
                context.reportError("只能是[是,否]");
            }
        }

    }

}
