package sunkey.common.config;

/**
 * @author Sunkey
 * @since 2018-04-19 下午4:04
 **/
public class SystemConfig extends AbstractConfig {

    private SystemConfig() {
        super("system");
    }

    @Override
    protected String getProperty(String key) {
        return System.getProperty(key);
    }

    public static SystemConfig INSTANCE = new SystemConfig();

}
