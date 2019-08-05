package sunkey.common.utils.excel.valid;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sunkey
 * @since 2019-06-11 17:16
 **/
public class Validator {

    private static final Map<Class, ConstraintValidator> instances = new HashMap<>();

    public static <T extends ConstraintValidator> T getConstraintValidator(Class<T> type, boolean reusable) {
        if (reusable) {
            return (T) instances.computeIfAbsent(type, ty -> {
                try {
                    return (T) ty.newInstance();
                } catch (Exception e) {
                    throw new IllegalStateException(e.getMessage(), e);
                }
            });
        } else {
            try {
                return type.newInstance();
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }

    public static boolean needValidate(Class<?> type) {
        return type.isAnnotationPresent(Valid.class);
    }

    public static void validate(int lineNo, Object target, ValidResult result) {
        ValidMeta validMeta = ValidMeta.forClass(target.getClass());
        if (validMeta.isNeedValidate()) {
            for (ValidMeta.FieldMeta field : validMeta.getFields()) {
                if (field.isBean()) {
                    validate(lineNo, field.getValue(target), result);
                } else {
                    field.getValidators().forEach((k, v) -> {
                        Object value = field.getValue(target);
                        ValidContext ctx = new ValidContext();
                        ctx.setTarget(value);
                        ctx.setResult(result);
                        ctx.setFieldName(field.getFieldName());
                        ctx.setLineNo(lineNo);
                        ctx.setAnnotation(k);
                        v.validate(value, k, ctx);
                    });
                }
            }
        }
    }

}
