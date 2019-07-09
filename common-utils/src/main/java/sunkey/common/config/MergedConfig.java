package sunkey.common.config;

import sunkey.common.utils.Assert;
import sunkey.common.utils.StringUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sunkey
 * @since 2018-04-16 下午5:34
 **/
public class MergedConfig extends AbstractConfig {

    @Getter
    protected final List<AbstractConfig> configs;
    protected final String name;

    public AbstractConfig getConfig(String name) {
        for (AbstractConfig cfg : configs) {
            if (cfg.getName().equals(name))
                return cfg;
        }
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    public MergedConfig(AbstractConfig[] attrsList) {
        super(null);
        Assert.notNull(attrsList, "attrsList");
        Assert.noNullElements(attrsList, "contains null!");
        this.configs = Arrays.asList(attrsList);
        this.name = "MergedConfig[" + getMergedName() + "]";
    }

    public MergedConfig(List<AbstractConfig> attrsList) {
        super(null);
        Assert.notNull(attrsList, "contains null!");
        for (AbstractConfig cfg : attrsList) {
            if (cfg == null) throw new IllegalArgumentException("list contains null!");
        }
        this.configs = new ArrayList<>(attrsList);
        this.name = "MergedConfig[" + getMergedName() + "]";
    }

    private String getMergedName() {
        return StringUtils.join(",",
                configs.stream()
                        .map(AbstractConfig::getName)
                        .collect(Collectors.toList()));
    }

    @Override
    protected String getProperty(String key) {
        String value;
        for (AbstractConfig cfg : configs) {
            try {
                value = cfg.getProperty(key);
                if (value != null) {
                    return value;
                }
            } catch (Exception ex) {
                // ignore.
            }
        }
        return null;
    }
}
