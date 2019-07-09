package sunkey.common.spring;

import sunkey.common.config.AbstractConfig;
import org.springframework.beans.PropertyValue;
import org.springframework.boot.bind.PropertySourcesPropertyValues;
import org.springframework.boot.context.config.ConfigFileApplicationListener;

/**
 *
 * Spring Yaml 格式配置
 *
 * 不适用于Spring2.0版本
 *
 * @author Sunkey
 * @since 2019-01-10 下午3:41
 **/
public class SpringConfig extends AbstractConfig {

    public static final SpringConfig INSTANCE = new SpringConfig();

    private final PropertySourcesPropertyValues propertyValues;

    private SpringConfig() {
        super(ConfigFileApplicationListener.APPLICATION_CONFIGURATION_PROPERTY_SOURCE_NAME);
        this.propertyValues = new PropertySourcesPropertyValues(
                Spring.getEnvironment().getPropertySources());
    }

    @Override
    protected String getProperty(String key) {
        PropertyValue valueWrapper = propertyValues.getPropertyValue(key);
        return valueWrapper == null ? null :
                valueWrapper.getValue() == null ? null :
                        valueWrapper.getValue().toString();
    }

}
