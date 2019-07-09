package sunkey.common.utils.excel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.reflect.Field;

/**
 * @author Sunkey
 * @since 2019-06-11 11:40
 **/
@Getter
@Setter
@ToString
public class Header {

    private int index;
    private String name;
    private Field field;

    public Header(int index, String name, Field field) {
        this.index = index;
        this.name = name;
        this.field = field;
        makeAccess();
    }

    private void makeAccess() {
        field.setAccessible(true);
    }

    public Object getValue(Object target) {
        try {
            return field.get(target);
        } catch (IllegalAccessException e) {
        }
        return null;
    }

    public void setValue(Object target, Object value) {
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
        }
    }

    public Class<?> getType() {
        return field.getType();
    }

}
