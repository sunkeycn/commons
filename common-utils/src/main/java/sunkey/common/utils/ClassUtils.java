package sunkey.common.utils;

/**
 * @author Sunkey
 * @since 2019-05-20 16:46
 **/
public class ClassUtils {

    public static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

}
