package sunkey.common.exception;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Sunkey
 * @since 2018-03-20 18:45
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(BusinessExceptionHandler.class)
public @interface EnableBusinessExceptionHandler {

    /**
     * Business Exception
     */
    PrintType bizType() default PrintType.NONE;

    /**
     * Business Exception Print Level
     */
    PrintLevel bizLevel() default PrintLevel.INFO;

    /**
     * Uncaught Exception
     */
    PrintType sysType() default PrintType.MESSAGE_AND_STACKTRACE;

    /**
     * Uncaught Exception Print Level
     */
    PrintLevel sysLevel() default PrintLevel.ERROR;

    enum PrintType {
        NONE,
        MESSAGE,
        MESSAGE_AND_STACKTRACE
    }

    enum PrintLevel {
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

}
