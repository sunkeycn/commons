package sunkey.common.utils.excel.valid;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;
import sunkey.common.utils.Assert;
import sunkey.common.utils.ReflectUtils;
import sunkey.common.utils.excel.Headers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sunkey
 * @since 2019-06-11 17:17
 **/

@Slf4j
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ValidMeta {

    private static final Map<Class, ValidMeta> caches = new HashMap<>();

    public static ValidMeta forClass(Class<?> type) {
        return caches.computeIfAbsent(type, ValidMeta::forClass0);
    }

    private final boolean needValidate;
    private final List<FieldMeta> fields = new ArrayList<>();

    @Getter
    @Setter
    @ToString
    public static class FieldMeta {
        private String fieldName;
        private final Field field;
        private final Map<Annotation, ConstraintValidator> validators;
        private final boolean bean;

        public FieldMeta(Field field, Map<Annotation, ConstraintValidator> validators, boolean bean) {
            this.field = field;
            this.validators = validators;
            this.bean = bean;
            this.field.setAccessible(true);
            this.fieldName = Headers.getFieldName(field);
            if (fieldName == null) {
                fieldName = field.getName();
            }
        }

        public Object getValue(Object target) {
            try {
                return field.get(target);
            } catch (IllegalAccessException e) {
                return null;
            }
        }

    }

    private static ValidMeta forClass0(Class<?> type) {
        ValidMeta meta = new ValidMeta(Validator.needValidate(type));
        if (meta.needValidate) {
            for (Field field : ReflectUtils.getAllFields(type).values()) {
                Map<Annotation, ConstraintValidator> annos = findConstraintAnnotations(field);
                if (!annos.isEmpty()) {
                    meta.fields.add(new FieldMeta(field, annos, false));
                } else if (Validator.needValidate(field.getType())) {
                    meta.fields.add(new FieldMeta(field, annos, true));
                }
            }
        }
        return meta;
    }

    public static Map<Annotation, ConstraintValidator> findConstraintAnnotations(Field field) {
        Map<Annotation, ConstraintValidator> result = new HashMap<>();
        for (Annotation annotation : field.getAnnotations()) {
            Class<? extends Annotation> annType = annotation.annotationType();
            Constraint ann = annType.getAnnotation(Constraint.class);
            if (ann != null) {
                ConstraintValidator validator = Validator.getConstraintValidator(ann.validator(), ann.reusable());
                try {
                    validateTypes(validator, annType, field);
                    result.put(annotation, validator);
                } catch (Exception ex) {
                    // Ignore type mismatch errors, don't add validator.
                    log.error(ex.getMessage(), ex);
                }
            }
        }
        return result;
    }

    private static void validateTypes(ConstraintValidator validator, Class annType, Field field) {
        ResolvableType type = ResolvableType.forInstance(validator).as(ConstraintValidator.class);
        Class<?> oAnnType = type.getGeneric(0).resolve();
        Class<?> oValueType = type.getGeneric(1).resolve();
        Assert.state(oAnnType == annType,
                "annotation type mismatch on @" + annType.getSimpleName() + " " + toString(field));
        Assert.state(oValueType.isAssignableFrom(field.getType()),
                "value type mismatch on @" + annType.getSimpleName() + " " + toString(field));
    }

    private static String toString(Field field) {
        if (field == null) return null;
        return field.getDeclaringClass().getSimpleName() + "." +
                field.getName() + "(" + field.getType().getSimpleName() + ")";
    }

}
