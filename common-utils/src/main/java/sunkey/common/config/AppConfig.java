package sunkey.common.config;


import sunkey.common.spring.SpringConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sunkey
 * @since 2018-04-19 下午3:37
 **/
public class AppConfig extends MergedConfig {

    private AppConfig(List<AbstractConfig> configList) {
        super(configList);
    }

    public static Finder finder() {
        return new Finder();
    }

    public static class Finder {

        private List<AbstractConfig> configs = new ArrayList<>();

        public AppConfig find() {
            return new AppConfig(configs);
        }

        public Finder yaml(String location) {
            try {
                YamlConfig config = YamlConfig.forPath(location);
                if (config != null) {
                    configs.add(config);
                }
            } catch (Throwable ex) {
            }
            return this;
        }

        public Finder properties(String location) {
            try {
                PropertiesConfig config = PropertiesConfig.forName(location);
                if (config != null) {
                    configs.add(config);
                }
            } catch (Throwable ex) {
            }
            return this;
        }

        public Finder system() {
            configs.add(SystemConfig.INSTANCE);
            return this;
        }

        public Finder application() {
            configs.add(SpringConfig.INSTANCE);
            return this;
        }

    }


}
