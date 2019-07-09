package sunkey.common.utils;

import sunkey.common.exception.BusinessException;
import sunkey.common.exception.ErrorType;
import sunkey.common.response.Response;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sunkey
 * @since 2018-07-05 下午4:24
 **/

@Slf4j
public class Wrapper {

    public static <T> T unwrap(Response<T> response) {
        if (response == null) {
            throw new BusinessException(ErrorType.SERVICE_ERROR);
        }
        if (!isSuccess(response)) {
            throw new BusinessException(new ErrorType.Default(response.getCode(), response.getMsg()));
        }
        return response.getData();
    }

    public static <T> T unwrapSafety(Response<T> response) {
        try {
            return unwrap(response);
        } catch (Throwable ex) {
            log.warn("调用服务失败:{}", ex.getMessage(), ex);
            return null;
        }
    }

    public static boolean isSuccess(Response response) {
        return response != null &&
                (response.getCode() == 0 || response.getCode() == 200);
    }

}
