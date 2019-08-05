package sunkey.common.utils.excel.support;

import org.apache.poi.ss.usermodel.Cell;
import sunkey.common.utils.excel.Header;

public interface ValueExtractor {

    Object extractValue(Header header, Cell cell);

}