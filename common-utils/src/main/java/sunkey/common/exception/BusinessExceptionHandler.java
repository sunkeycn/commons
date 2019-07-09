package sunkey.common.exception;

import sunkey.common.exception.EnableBusinessExceptionHandler.PrintLevel;
import sunkey.common.exception.EnableBusinessExceptionHandler.PrintType;
import sunkey.common.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;

/**
 * @author Sunkey
 * @since 2018-03-20 18:49
 **/
@Slf4j
@Configuration
@ControllerAdvice
public class BusinessExceptionHandler implements ImportAware {

    private PrintType bizType;
    private PrintLevel bizLevel;
    private PrintType sysType;
    private PrintLevel sysLevel;

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response handleValidateException(MethodArgumentNotValidException ex) {
        String errorStr = ex.getBindingResult().getFieldErrors().stream().map((e) ->
                e.getField() + e.getDefaultMessage()
        ).collect(Collectors.joining(","));
        return Response.error(ErrorType.INVALID_PARAM.params(errorStr));
    }

    @Override
    public void setImportMetadata(AnnotationMetadata meta) {
        AnnotationAttributes annoAttrs =
                AnnotationAttributes.fromMap(
                        meta.getAnnotationAttributes(
                                EnableBusinessExceptionHandler.class.getName()));
        this.bizType = annoAttrs.getEnum("bizType");
        this.bizLevel = annoAttrs.getEnum("bizLevel");
        this.sysType = annoAttrs.getEnum("sysType");
        this.sysLevel = annoAttrs.getEnum("sysLevel");
    }

    @PostConstruct
    public void afterPropertiesSet() {
        if (bizType == null || bizLevel == null || sysType == null || sysLevel == null) {
            bizType = def(bizType, PrintType.NONE);
            bizLevel = def(bizLevel, PrintLevel.INFO);
            sysType = def(sysType, PrintType.MESSAGE_AND_STACKTRACE);
            sysLevel = def(sysLevel, PrintLevel.ERROR);
            log.info("[Framework] @EnableBusinessExceptionHandler.values() get failed. " +
                    "falling to default values. \n" +
                    " bizType  : {}\n" +
                    " bizLevel : {}\n" +
                    " sysType  : {}\n" +
                    " sysLevel : {}", bizType, bizLevel, sysType, sysLevel);
        } else {
            log.info("[Framework] @EnableBusinessExceptionHandler import -> \n" +
                    " bizType  : {}\n" +
                    " bizLevel : {}\n" +
                    " sysType  : {}\n" +
                    " sysLevel : {}", bizType, bizLevel, sysType, sysLevel);
        }
    }

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public Response handle(Throwable ex) {
        if (log.isTraceEnabled()) {
            log.trace("Handle Exception : {} by BusinessExceptionHandler.", ex.getClass().getName());
        }
        PrintType type;
        PrintLevel level;
        Response error;
        String prefix;
        if (ex instanceof BusinessException) {
            type = bizType;
            level = bizLevel;
            error = Response.error(((BusinessException) ex).getErrorType());
            prefix = "BusinessException : {}";
        } else {
            type = sysType;
            level = sysLevel;
            error = Response.error(ex.getMessage());
            prefix = "UncaughtException : {}";
        }
        if (type == PrintType.NONE) return error;
        log(type, level, ex, prefix);
        return error;
    }

    private PrintType def(PrintType type, PrintType def) {
        return type == null ? def : type;
    }

    private PrintLevel def(PrintLevel level, PrintLevel def) {
        return level == null ? def : level;
    }

    private void log(PrintType type, PrintLevel level, Throwable ex, String prefix) {
        switch (level) {
            case DEBUG:
                if (log.isDebugEnabled()) {
                    switch (type) {
                        case MESSAGE:
                            log.debug(prefix, ex.getMessage());
                            break;
                        case MESSAGE_AND_STACKTRACE:
                            log.debug(prefix, ex.getMessage(), ex);
                            break;
                    }
                }
                break;
            case INFO:
                if (log.isInfoEnabled()) {
                    switch (type) {
                        case MESSAGE:
                            log.info(prefix, ex.getMessage());
                            break;
                        case MESSAGE_AND_STACKTRACE:
                            log.info(prefix, ex.getMessage(), ex);
                            break;
                    }
                }
                break;
            case WARN:
                if (log.isWarnEnabled()) {
                    switch (type) {
                        case MESSAGE:
                            log.warn(prefix, ex.getMessage());
                            break;
                        case MESSAGE_AND_STACKTRACE:
                            log.warn(prefix, ex.getMessage(), ex);
                            break;
                    }
                }
                break;
            case ERROR:
                if (log.isErrorEnabled()) {
                    switch (type) {
                        case MESSAGE:
                            log.error(prefix, ex.getMessage());
                            break;
                        case MESSAGE_AND_STACKTRACE:
                            log.error(prefix, ex.getMessage(), ex);
                            break;
                    }
                }
                break;
        }
    }

}

