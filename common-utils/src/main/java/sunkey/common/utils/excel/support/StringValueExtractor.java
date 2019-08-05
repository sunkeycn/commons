package sunkey.common.utils.excel.support;

import org.apache.poi.ss.usermodel.Cell;

/**
 * @author Sunkey
 * @since 2019-07-30 11:37
 **/
public interface StringValueExtractor {

    String extractValue(Cell cell);

}
