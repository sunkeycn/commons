package sunkey.common.utils.excel.support;

/**
 * @author Sunkey
 * @since 2019-07-03 15:09
 **/
public class WriterException extends RuntimeException {


    public WriterException() {
    }

    public WriterException(String message) {
        super(message);
    }

    public WriterException(String message, Throwable cause) {
        super(message, cause);
    }

    public WriterException(Throwable cause) {
        super(cause);
    }


}
