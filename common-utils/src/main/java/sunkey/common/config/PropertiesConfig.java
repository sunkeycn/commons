package sunkey.common.config;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Properties;

/**
 * @author Sunkey
 * @since 2018-04-19 下午3:45
 **/

public class PropertiesConfig extends AbstractConfig {

    private static final HashMap<String, PropertiesConfig> caches = new HashMap<>();

    public static PropertiesConfig forName(String location) {
        PropertiesConfig config = caches.get(location);
        if (config != null) return config;
        config = new PropertiesConfig(location);
        caches.put(location, config);
        return config;
    }

    private final Properties properties;

    private PropertiesConfig(String location) {
        super(location);
        this.properties = new Properties();
        try {
            InputStream inputStream = ResourceFinder.getResourceAsStream(location);
            properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (Exception ex) {
            throw new ResourceFinder.ResourceNotFoundException(location, ex);
        }
    }

    @Override
    protected String getProperty(String key) {
        return properties.getProperty(key);
    }
}
