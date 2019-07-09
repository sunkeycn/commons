package sunkey.common.utils.excel.support;

import org.apache.poi.ss.usermodel.Cell;

public interface ValueExtractor {
    String extractValue(Cell cell);
}