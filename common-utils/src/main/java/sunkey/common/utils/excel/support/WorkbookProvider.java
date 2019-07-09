package sunkey.common.utils.excel.support;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author Sunkey
 * @since 2019-07-03 14:35
 **/
public interface WorkbookProvider {

    Workbook createWorkbook();


    WorkbookProvider HSSF = new WorkbookProvider() {
        @Override
        public Workbook createWorkbook() {
            return new HSSFWorkbook();
        }
    };

    WorkbookProvider XSSF = new WorkbookProvider() {
        @Override
        public Workbook createWorkbook() {
            return new XSSFWorkbook();
        }
    };

    WorkbookProvider SXSSF = new WorkbookProvider() {
        @Override
        public Workbook createWorkbook() {
            return new SXSSFWorkbook();
        }
    };

}
