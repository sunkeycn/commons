package sunkey.common.utils.excel.support;

import org.apache.poi.ss.usermodel.Cell;
import sunkey.common.utils.StringUtils;

/**
 * @author Sunkey
 * @since 2019-07-30 13:46
 **/
public class DefaultStringValueExtractor implements StringValueExtractor {

    public static final DefaultStringValueExtractor INSTANCE = new DefaultStringValueExtractor(true, true);

    private final boolean ignoreBlank;
    private final boolean autoTrim;

    public DefaultStringValueExtractor(boolean ignoreBlank, boolean autoTrim) {
        this.ignoreBlank = ignoreBlank;
        this.autoTrim = autoTrim;
    }

    public String extractValue(Cell cell) {
        String text = cell.toString();
        if (ignoreBlank && StringUtils.isBlank(text)) {
            return null;
        }
        return autoTrim ? text.trim() : text;
    }

}
