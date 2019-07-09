package sunkey.common.utils.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Sunkey
 * @since 2019-06-11 11:36
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelField {

    /**
     * Header for field
     *
     * @return
     */
    String value();

    /**
     * Export Format
     * <p>
     * e.g.
     * Date "yyyy-MM-dd HH:mm:ss"
     * Number "#00.000"
     *
     * @return
     */
    String format() default "";

}
