package sunkey.common.utils.excel.support;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author Sunkey
 * @since 2019-07-03 14:33
 **/
public interface Styler {

    CellStyle style(Workbook workbook);

}
