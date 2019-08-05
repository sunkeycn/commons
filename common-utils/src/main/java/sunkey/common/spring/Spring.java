package sunkey.common.spring;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.bind.PropertiesConfigurationFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Validator;
import sunkey.common.utils.Assert;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * SpringBoot工具类
 *
 * @author Sunkey
 * @since 2019-01-10 下午3:29
 **/
@Slf4j
public class Spring implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private Spring() {
    }

    private static final AtomicBoolean loaded = new AtomicBoolean(false);
    @Getter
    private static ConfigurableApplicationContext applicationContext;

    public static boolean isLoaded() {
        return loaded.get();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Spring.applicationContext = applicationContext;
        log.info("Utils For Spring Loaded.");
        loaded.set(true);
    }

    /**
     * 获取资源
     *
     * @param location
     * @return
     */
    public static Resource getResource(String location) {
        return getApplicationContext().getResource(location);
    }

    /**
     * 获取资源，不会抛出异常
     *
     * @param location
     * @return
     */
    public static Resource[] getResources(String location) {
        try {
            return getApplicationContext().getResources(location);
        } catch (IOException ex) {
            return new Resource[0];
        }
    }

    /**
     * 获取Spring Environment
     *
     * @return
     */
    public static ConfigurableEnvironment getEnvironment() {
        return getApplicationContext().getEnvironment();
    }

    /**
     * 根据类型获取Bean
     *
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> type) {
        try {
            return getApplicationContext().getBean(type);
        } catch (BeansException ex) {
            return null;
        }
    }

    /**
     * 根据名称获取Bean
     *
     * @param name
     * @param <T>
     * @return
     */
    public static <T> T getBean(String name) {
        try {
            return (T) getApplicationContext().getBean(name);
        } catch (BeansException ex) {
            return null;
        }
    }

    /**
     * 根据名称及类型获取Bean
     *
     * @param name
     * @param <T>  requiredType
     * @return
     */
    public static <T> T getBean(String name, Class<T> type) {
        try {
            return (T) getApplicationContext().getBean(name, type);
        } catch (BeansException ex) {
            return null;
        }
    }

    /**
     * 根据类型获取Beans
     *
     * @param type
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> type) {
        try {
            return getApplicationContext().getBeansOfType(type);
        } catch (BeansException ex) {
            return new HashMap<>();
        }
    }

    /**
     * 获取Environment Property值
     *
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        return getEnvironment().getProperty(key);
    }

    /**
     * 获取Environment Property值，带有默认值
     *
     * @param key
     * @return
     */
    public static String getProperty(String key, String defaultValue) {
        return getEnvironment().getProperty(key, defaultValue);
    }

    /**
     * 获取指定类型的Environment Property值
     *
     * @param key
     * @return
     */
    public static <T> T getProperty(String key, Class<T> type) {
        return getEnvironment().getProperty(key, type);
    }

    /**
     * 获取指定类型的Environment Property值，带有默认值
     *
     * @param key
     * @return
     */
    public static <T> T getProperty(String key, Class<T> type, T defaultValue) {
        return getEnvironment().getProperty(key, type, defaultValue);
    }

    /**
     * 解析Environment Property值
     * <p>
     * 例：
     * String str = "hello ${hestia.username}, this is a string.";
     * String str1 = Spring.resolvePlaceholders(str);
     * // (hestia.username = "Lily")
     * System.out.println(str1); => "hello Lily, this is a string."
     *
     * @param text
     * @return
     */
    public static String resolvePlaceholders(String text) {
        return getEnvironment().resolvePlaceholders(text);
    }

    /**
     * 解析Environment Property值(required)
     *
     * @param text
     * @return
     * @see Spring#resolvePlaceholders
     */
    public static String resolveRequiredPlaceholders(String text) {
        return getEnvironment().resolveRequiredPlaceholders(text);
    }

    /**
     * 取得转换服务 convert(a=>b)
     *
     * @return
     */
    public static ConversionService getConversionService() {
        ConversionService cs = getBean(ConversionService.class);
        return cs == null ? new DefaultConversionService() : cs;
    }

    public static Validator getValidator() {
        return getBean(Validator.class);
    }

    public static void bindTo(Object target) throws BindException {
        bindTo(null, target);
    }

    public static void bindTo(String prefix, Object target) throws BindException {
        bindTo(prefix, target, getConversionService(), getValidator());
    }

    /**
     * 将Environment Properties 绑定到对象上
     *
     * @param prefix    属性列表的前缀
     * @param target    绑定目标对象
     * @param cs        convert服务
     * @param validator 验证器
     * @throws BindException
     */
    public static void bindTo(String prefix, Object target,
                              ConversionService cs, Validator validator) throws BindException {
        PropertiesConfigurationFactory<Object> factory = new PropertiesConfigurationFactory<>(target);
        factory.setPropertySources(getEnvironment().getPropertySources());
        if (validator != null && validator.supports(target.getClass())) {
            factory.setValidator(validator);
        }
        factory.setConversionService(cs != null ? cs : new DefaultConversionService());
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
        } else if (prefix != null) {
            factory.setTargetName(prefix);
        }
        factory.bindPropertiesToTarget();
    }

    /**
     * 注册单例对象到Spring Context
     * 例如工具类
     *
     * @param beanName
     * @param singletonObject
     */
    public static void registerSingleton(String beanName, Object singletonObject) {
        getApplicationContext().getBeanFactory().registerSingleton(beanName, singletonObject);
    }

    /**
     * 为Bean注入Values
     *
     * @param existingBean
     */
    public static void autowireBean(Object existingBean) {
        getApplicationContext().getBeanFactory().autowireBean(existingBean);
    }

    /**
     * 获得SpringConfig(例如来自Apollo配置中心的配置)
     *
     * @return
     */
    public static SpringConfig getConfig() {
        return SpringConfig.INSTANCE;
    }


    private static final Map<Class, Object> proxies = new HashMap<>();

    /**
     * 缓存的。获得自身的代理对象
     *
     * @param self $this
     * @param <T>
     * @return real proxy object
     */
    public static <T> T self(T self) {
        Assert.notNull(self, "self");
        return (T) proxies.computeIfAbsent(self.getClass(), clazz -> {
            Map beans = getBeansOfType(clazz);
            if (beans.isEmpty())
                return null;
            if (beans.size() > 1) {
                throw new IllegalStateException(String.format("beansOfType %s has %s", clazz, beans.size()));
            }
            return beans.values().stream().findFirst();
        });
    }

}
