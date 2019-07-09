package sunkey.common.config;

import sunkey.common.utils.ReflectUtils;
import lombok.Getter;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 61405 on 2017/9/5.
 */
public class YamlConfig extends AbstractConfig {

    public static final String APPLICATION_YML = "classpath:application.yml";

    private static final Map<String, YamlConfig> cached = new HashMap<>();

    @Getter
    private final Map<String, Object> data;

    private YamlConfig(String name, Map<String, Object> data) {
        super(name);
        this.data = data;
    }

    public Object getByKeys(String keys) {
        String[] vals = StringUtils.tokenizeToStringArray(keys, ".");
        Object target = this.data;
        for (String val : vals) {
            if (target == null) return null;
            if (target instanceof Map) {
                target = ((Map) target).get(val);
            } else {
                return null;
            }
        }
        return target;
    }

    @Deprecated
    public static YamlConfig application() {
        return forPath(APPLICATION_YML);
    }

    public static YamlConfig forPath(String path) {
        YamlConfig cache = cached.get(path);
        if (cache == null) {
            Map<String, Object> map = yamlToMap(path);
            cache = new YamlConfig(path, map);
            cached.put(path, cache);
        }
        return cache;
    }

    public static Map<String, Object> yamlToMap(String path) {
        try {
            YamlMapFactoryBean yaml = new YamlMapFactoryBean();
            Resource[] resources = ResourceFinder.getResources(path);
            yaml.setResources(resources);
            return yaml.getObject();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
    }

    @Override
    protected String getProperty(String key) {
        Object val = getByKeys(key);
        return val == null ? null : val.toString();
    }


    public <T> T getObject(String prefix, Class<T> type) {
        return ReflectUtils.convertObject(getByKeys(prefix), type);
    }

    public <T> List<T> getList(String prefix, Class<T> type) {
        return ReflectUtils.convertList(getByKeys(prefix), type);
    }

}
