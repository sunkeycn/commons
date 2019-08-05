package sunkey.common.utils.excel.support;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import sunkey.common.utils.NumberUtils;
import sunkey.common.utils.StringUtils;
import sunkey.common.utils.excel.Header;

public class DefaultValueExtractor implements ValueExtractor {

    public static final DefaultValueExtractor INSTANCE = new DefaultValueExtractor(true, true);

    private final boolean ignoreBlank;
    private final boolean autoTrim;

    public DefaultValueExtractor(boolean ignoreBlank, boolean autoTrim) {
        this.ignoreBlank = ignoreBlank;
        this.autoTrim = autoTrim;
    }

    @Override
    public Object extractValue(Header header, Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue();
            } else if (header != null && Number.class.isAssignableFrom(header.getType())) {
                return NumberUtils.convertTo(cell.getNumericCellValue(), (Class) header.getType());
            } else {
                return cell.getNumericCellValue();
            }
        }
        String text = cell.toString();
        if (ignoreBlank && StringUtils.isBlank(text)) {
            return null;
        }
        return autoTrim ? text.trim() : text;
    }

}