package sunkey.common.config;


import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.PropertyPlaceholderHelper;

/**
 * @author Sunkey
 * @since 2018-04-14 上午11:41
 **/
public abstract class AbstractConfig extends AbstractConfigAttributes
        implements PropertyPlaceholderHelper.PlaceholderResolver {

    private final String name;

    public AbstractConfig(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    private static final PropertyPlaceholderHelper helper
            = new PropertyPlaceholderHelper("${", "}", ":", false);

    private static final ConversionService conversionService = new DefaultConversionService();

    @Override
    protected <T> T getConfigAs(String key, Class<T> requireType) {
        String value = resolveProperty(key);
        if (value == null) {
            return null;
        }
        if (requireType == String.class) {
            return (T) value;
        }
        return conversionService.convert(value, requireType);
    }

    protected String resolveProperty(String key) {
        String value = getProperty(key);
        if (value == null) {
            return null;
        }
        return helper.replacePlaceholders(value, this);
    }

    @Override
    public String resolvePlaceholder(String placeholderName) {
        return getString(placeholderName);
    }

    protected abstract String getProperty(String key);

}
