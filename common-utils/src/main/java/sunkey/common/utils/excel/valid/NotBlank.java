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
@Constraint(validator = NotBlank.NotBlankValidator.class)
public @interface NotBlank {

    class NotBlankValidator implements ConstraintValidator<NotBlank, Object> {

        @Override
        public void validate(Object value, NotBlank anno, ValidContext context) {
            if (StringUtils.isEmpty(value)) {
                context.reportError("不能为空");
            }
        }

    }

}
