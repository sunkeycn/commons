package sunkey.common.utils.excel.valid;

import sunkey.common.utils.excel.support.ErrorFormatter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sunkey
 * @since 2019-06-11 16:53
 **/
@Getter
@RequiredArgsConstructor
public class ValidResult {

    private final ErrorFormatter formatter;
    private final Map<Integer, Map<String, Error>> errors = new TreeMap<>();

    @Override
    public String toString() {
        return errors.toString();
    }

    public List<Error> errors() {
        List<Error> result = new ArrayList<>();
        for (Map<String, Error> errMap : errors.values()) {
            result.addAll(errMap.values());
        }
        return result;
    }

    public List<String> errorMessages() {
        return errors().stream().map(Error::getFormattedMessage).collect(Collectors.toList());
    }

    private void addError(Error error) {
        errors.computeIfAbsent(error.getLineNo(), HashMap::new)
                .putIfAbsent(error.getFieldName(), error);
    }

    public void addError(int lineNo, String fieldName, String message, Object target) {
        addError(new Error(lineNo, fieldName, target, message, null));
    }

    public void addError(String message,
                         ValidContext context) {
        addError(new Error(
                context.getLineNo(),
                context.getFieldName(),
                context.getTarget(),
                message,
                context.getAnnotation()));
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public class Error {
        private int lineNo;
        private String fieldName;
        private Object target;
        private String message;
        private Annotation annotation;

        @Override
        public String toString() {
            return getFormattedMessage();
        }

        public String getFormattedMessage() {
            return formatter.format(this);
        }

    }

}
