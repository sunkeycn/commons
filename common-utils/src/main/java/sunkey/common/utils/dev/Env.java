package sunkey.common.utils.dev;

import lombok.SneakyThrows;
import org.springframework.boot.bind.PropertiesConfigurationFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.env.PropertySourcesLoader;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.StringUtils;

/**
 * @author Sunkey
 * @since 2019-01-13 下午2:11
 **/
public class Env {

    @SneakyThrows
    public static <T> T bindTo(String path, Class<T> type) {
        T target = type.newInstance();
        bindTo(path, target);
        return target;
    }

    @SneakyThrows
    public static void bindTo(String path, Object target) {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource(path);
        PropertySourcesLoader loader = new PropertySourcesLoader();
        loader.load(resource);
        PropertiesConfigurationFactory<Object> factory = new PropertiesConfigurationFactory<Object>(target);
        factory.setPropertySources(loader.getPropertySources());
        factory.setConversionService(new DefaultConversionService());
        ConfigurationProperties annotation =
                AnnotationUtils.findAnnotation(target.getClass(), ConfigurationProperties.class);
        if (annotation != null) {
            factory.setIgnoreInvalidFields(annotation.ignoreInvalidFields());
            factory.setIgnoreUnknownFields(annotation.ignoreUnknownFields());
            factory.setExceptionIfInvalid(annotation.exceptionIfInvalid());
            factory.setIgnoreNestedProperties(annotation.ignoreNestedProperties());
            if (StringUtils.hasLength(annotation.prefix())) {
                factory.setTargetName(annotation.prefix());
            }
        }
        factory.bindPropertiesToTarget();
    }

}
