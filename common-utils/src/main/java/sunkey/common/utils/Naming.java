package sunkey.common.utils;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;

/**
 * @author Sunkey
 * @since 2018-04-18 上午12:02
 **/
public class Naming {

    public static final PropertyNamingStrategy.SnakeCaseStrategy SNAKE =
            (PropertyNamingStrategy.SnakeCaseStrategy) PropertyNamingStrategy.SNAKE_CASE;

    public static final PropertyNamingStrategy.KebabCaseStrategy KEBAB =
            (PropertyNamingStrategy.KebabCaseStrategy) PropertyNamingStrategy.KEBAB_CASE;

    public static final PropertyNamingStrategy.LowerCaseStrategy LOWER_CASE =
            (PropertyNamingStrategy.LowerCaseStrategy) PropertyNamingStrategy.LOWER_CASE;

    public static final PropertyNamingStrategy LOWER_CAMEL_CASE =
            PropertyNamingStrategy.LOWER_CAMEL_CASE;

    public static final PropertyNamingStrategy.UpperCamelCaseStrategy UPPER_CAMEL_CASE =
            (PropertyNamingStrategy.UpperCamelCaseStrategy) PropertyNamingStrategy.UPPER_CAMEL_CASE;


    /**
     * "testTestTest" -> "test_test_test"
     *
     * @param str
     * @return
     */
    public static String camelToSnake(String str) {
        if (str == null) return null;
        if (str.isEmpty()) return str;
        return SNAKE.translate(str);
    }


    /**
     * "testTestTest" -> "test-test-test"
     *
     * @param str
     * @return
     */
    public static String camelToKebab(String str) {
        if (str == null) return null;
        if (str.isEmpty()) return str;
        return KEBAB.translate(str);
    }

}
