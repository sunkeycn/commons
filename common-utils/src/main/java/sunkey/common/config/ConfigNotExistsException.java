package sunkey.common.config;

/**
 * @author Sunkey
 * @since 2018-04-14 上午11:10
 **/
public class ConfigNotExistsException extends RuntimeException {

    public ConfigNotExistsException() {
    }

    public ConfigNotExistsException(String message) {
        super(message);
    }

    public ConfigNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigNotExistsException(Throwable cause) {
        super(cause);
    }

    public ConfigNotExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
