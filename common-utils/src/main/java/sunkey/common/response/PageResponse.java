package sunkey.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import sunkey.common.exception.ErrorType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sunkey
 * @since 2018-03-20 18:25
 **/
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResponse<T> extends Response<List<T>> {

    private int totalCount;

    public PageResponse() {
    }

    public PageResponse(int code, String msg, List<T> data, int totalCount) {
        super(code, msg, data == null ? new ArrayList<>() : data);
        this.totalCount = totalCount;
    }

    public PageResponse(int code, String msg) {
        super(code, msg, null);
    }

    public PageResponse<T> setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        return this;
    }

    public static <T> PageResponse<T> pageError(String errorMsg) {
        return new PageResponse<>(ErrorType.SYSTEM_ERROR.getCode(), errorMsg);
    }

    public static <T> PageResponse<T> success(List<T> data, int totalCount) {
        return new PageResponse<>(ErrorType.SUCCESS.getCode(), ErrorType.SUCCESS.getMsg(), data, totalCount);
    }

}
