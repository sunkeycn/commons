package sunkey.common.utils.excel.support;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

public class NoneStyler implements Styler {

    public static final NoneStyler INSTANCE = new NoneStyler();

    private NoneStyler() {
    }

    @Override
    public CellStyle style(Workbook workbook) {
        return null;
    }

}