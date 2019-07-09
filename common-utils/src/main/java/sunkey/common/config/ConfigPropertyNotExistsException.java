package sunkey.common.config;

/**
 * @author Sunkey
 * @since 2018-04-14 上午11:11
 **/
public class ConfigPropertyNotExistsException  extends RuntimeException{

    public ConfigPropertyNotExistsException() {
    }

    public ConfigPropertyNotExistsException(String message) {
        super(message);
    }

    public ConfigPropertyNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigPropertyNotExistsException(Throwable cause) {
        super(cause);
    }

    public ConfigPropertyNotExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
