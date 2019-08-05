package sunkey.common.utils.excel.support;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Sunkey
 * @since 2019-07-03 14:35
 **/
public interface WorkbookProvider {

    Workbook createWorkbook();

    Workbook createWorkbook(InputStream in) throws IOException;

    WorkbookProvider HSSF = new WorkbookProvider() {
        @Override
        public Workbook createWorkbook() {
            return new HSSFWorkbook();
        }

        @Override
        public Workbook createWorkbook(InputStream in) throws IOException {
            return new HSSFWorkbook(in);
        }
    };

    WorkbookProvider XSSF = new WorkbookProvider() {
        @Override
        public Workbook createWorkbook() {
            return new XSSFWorkbook();
        }

        @Override
        public Workbook createWorkbook(InputStream in) throws IOException {
            return new XSSFWorkbook(in);
        }
    };

    WorkbookProvider SXSSF = new WorkbookProvider() {
        @Override
        public Workbook createWorkbook() {
            return new SXSSFWorkbook();
        }

        @Override
        public Workbook createWorkbook(InputStream in) throws IOException {
            throw new UnsupportedOperationException();
        }
    };

}
