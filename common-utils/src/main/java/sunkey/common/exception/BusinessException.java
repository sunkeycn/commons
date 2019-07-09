package sunkey.common.exception;

/**
 * @author Sunkey
 * @since 2018-03-20 18:25
 **/
public class BusinessException extends RuntimeException {

    private final ErrorType errorType;

    public BusinessException(ErrorType errorType) {
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    @Override
    public String getMessage() {
        return "code=" + errorType.getCode() + ", msg=" + errorType.getMsg();
    }

    @Override
    public String toString() {
        return "BusinessException [" + getMessage() + "]";
    }
}
