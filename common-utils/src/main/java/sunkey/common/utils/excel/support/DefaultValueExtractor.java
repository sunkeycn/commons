package sunkey.common.utils.excel.support;

import sunkey.common.utils.StringUtils;
import org.apache.poi.ss.usermodel.Cell;

public class DefaultValueExtractor implements ValueExtractor {

    public static final DefaultValueExtractor INSTANCE = new DefaultValueExtractor(true, true);

    private final boolean ignoreBlank;
    private final boolean autoTrim;

    public DefaultValueExtractor(boolean ignoreBlank, boolean autoTrim) {
        this.ignoreBlank = ignoreBlank;
        this.autoTrim = autoTrim;
    }

    @Override
    public String extractValue(Cell cell) {
        if (cell == null) return null;
        String text = cell.toString();
        if (ignoreBlank && StringUtils.isBlank(text)) {
            return null;
        }
        return autoTrim ? text.trim() : text;
    }

}