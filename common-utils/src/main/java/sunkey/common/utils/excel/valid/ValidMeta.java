package sunkey.common.utils.excel.valid;

import sunkey.common.utils.ReflectUtils;
import sunkey.common.utils.excel.Headers;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sunkey
 * @since 2019-06-11 17:17
 **/
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

    public static Map<Annotation, ConstraintValidator> findConstraintAnnotations(AnnotatedElement ae) {
        Map<Annotation, ConstraintValidator> result = new HashMap<>();
        for (Annotation annotation : ae.getAnnotations()) {
            Constraint anno = annotation.annotationType().getAnnotation(Constraint.class);
            if (anno != null) {
                result.put(annotation, Validator.getConstraintValidator(anno.validator()));
            }
        }
        return result;
    }

}
