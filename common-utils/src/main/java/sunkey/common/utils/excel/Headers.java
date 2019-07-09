package sunkey.common.utils.excel;

import sunkey.common.utils.ReflectUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sunkey
 * @since 2019-06-11 11:40
 **/
@Getter
@Setter
@ToString
public class Headers<T> {

    private Class<T> type;
    private Header[] headers;
    private Field lineNo;

    public Headers(List<String> headerNames, Class<T> type) {
        this.headers = new Header[headerNames.size()];
        this.type = type;
        resolveHeaders(headerNames);
    }

    public Header getHeader(int index) {
        if (index >= headers.length) {
            return null;
        }
        return headers[index];
    }

    public int length() {
        return headers.length;
    }

    public void setLineNo(Object target, int lineNo) {
        if (this.lineNo != null) {
            try {
                this.lineNo.set(target, lineNo);
            } catch (IllegalAccessException e) {
            }
        }
    }

    private void resolveHeaders(List<String> headerNames) {
        Map<String, Field> namedMap = new HashMap<>();
        for (Field field : ReflectUtils.getAllFields(type).values()) {
            String fieldName = getFieldName(field);
            if (fieldName != null) {
                Field origin = namedMap.put(fieldName, field);
                if (origin != null) {
                    throw new IllegalStateException("duplicate @ExcelField named '" + fieldName + "'");
                }
            }
            if (field.isAnnotationPresent(LineNo.class)) {
                if (this.lineNo != null) {
                    throw new IllegalStateException("duplicate Field @LineNo");
                }
                if (field.getType() != Integer.TYPE) {
                    throw new IllegalStateException("@LineNo should int");
                }
                field.setAccessible(true);
                this.lineNo = field;
            }
        }
        for (int i = 0; i < headerNames.size(); i++) {
            String headerName = headerNames.get(i);
            Field field = namedMap.get(headerName);
            if (field != null) {
                headers[i] = new Header(i, headerName, field);
            }
        }
    }

    public static String getFieldName(Field field) {
        ExcelField anno = field.getAnnotation(ExcelField.class);
        if (anno != null) {
            return anno.value();
        }
        return null;
    }

}
