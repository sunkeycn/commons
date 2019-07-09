package sunkey.common.utils;

import sunkey.common.consts.PageConstants;
import sunkey.common.exception.BusinessException;
import sunkey.common.exception.ErrorType;
import org.springframework.validation.BindingResult;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Sunkey
 * @since 2018-04-10 下午3:18
 **/
public class Assert {

    public static void valid(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorStr = getErrorInfo(bindingResult);
            throw new BusinessException(ErrorType.INVALID_PARAM.params(errorStr));
        }
    }

    public static String getErrorInfo(BindingResult bindingResult) {
        if (bindingResult == null) return null;
        return bindingResult.getFieldErrors()
                .stream().map(e -> e.getField() + e.getDefaultMessage())
                .collect(Collectors.joining(","));
    }

    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    public static void state(boolean expression, ErrorType error) {
        if (!expression) {
            throw new BusinessException(error);
        }
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isTrue(boolean expression, ErrorType error) {
        if (!expression) {
            throw new BusinessException(error);
        }
    }

    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNull(Object object, ErrorType error) {
        if (object != null) {
            throw new BusinessException(error);
        }
    }

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object, ErrorType error) {
        if (object == null) {
            throw new BusinessException(error);
        }
    }

    public static void noNullElements(Object[] array, String message) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new IllegalArgumentException(message);
                }
            }
        }
    }

    public static void noNullElements(Object[] array, ErrorType error) {
        if (array != null) {
            for (Object element : array) {
                if (element == null) {
                    throw new BusinessException(error);
                }
            }
        }
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Collection<?> collection, ErrorType error) {
        if (collection == null || collection.isEmpty()) {
            throw new BusinessException(error);
        }
    }

    public static void notEmpty(Map<?, ?> map, String message) {
        if (map == null || map.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Map<?, ?> map, ErrorType error) {
        if (map == null || map.isEmpty()) {
            throw new BusinessException(error);
        }
    }

    public static void validPageArgs(int offset, int pageSize) {
        if (offset < 0 || pageSize < 0 || pageSize > PageConstants.MAX_PAGE_SIZE)
            throw new BusinessException(ErrorType.ILLEGAL_PAGE_SIZE);
    }

    public static void requireArg(Object arg, String argName) {
        if (arg == null) {
            throw new BusinessException(ErrorType.PARAM_REQUIRED.params(argName));
        }
    }

    @Deprecated
    public static void requireArg(Object arg) {
        if (arg == null) {
            throw new BusinessException(ErrorType.PARAM_REQUIRED);
        }
    }

}
