package sunkey.common.exception;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Sunkey
 * @since 2018-03-20 18:25
 * <p>
 * 系统级错误码范围：1 - 1000
 * 应用级错误码范围：1001 - 9999
 **/
public interface ErrorType {

    /**
     * Standard Errors
     */
    ErrorType SUCCESS = new Default(200, "响应成功");
    ErrorType UNAUTHORIZED = new Default(401, "请求未授权");
    ErrorType SYSTEM_ERROR = new Default(500, "系统内部错误[%s]");
    ErrorType SERVICE_ERROR = new Default(500, "服务调用失败[%s]");
    ErrorType PARAM_REQUIRED = new Default(600, "缺少必要参数[%s]");
    ErrorType INVALID_PARAM = new Default(602, "参数错误[%s]");
    ErrorType ILLEGAL_PAGE_SIZE = new Default(610, "分页参数非法");

    int getCode();

    String getMsg();

    default boolean is(ErrorType other) {
        if (other == null) return false;
        return getCode() == other.getCode();
    }

    @Getter
    @ToString
    class Default implements ErrorType {

        private final int code;
        private final String msg;
        private Object context;

        public Default(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public Default(int code, String msg, Object context) {
            this.code = code;
            this.msg = msg;
            this.context = context;
        }

        public Default setContext(Object context) {
            this.context = context;
            return this;
        }

    }

    default ErrorType params(Object... params) {
        Object context = null;
        if (this instanceof Default) {
            context = ((Default) this).getContext();
        }
        return new Default(getCode(), String.format(getMsg(), params), context);
    }

    default ErrorType context(Object context) {
        return new Default(getCode(), getMsg(), context);
    }

}
