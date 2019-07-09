package sunkey.common.utils.excel;

import org.apache.poi.POIXMLException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @author Sunkey
 * @since 2019-06-10 15:50
 **/
public class Excel {

    private static Workbook tryLoad(InputStream in) throws IOException {
        Workbook workbook;
        try {
            workbook = new XSSFWorkbook(in);
        } catch (POIXMLException e) {
            workbook = new HSSFWorkbook(in);
        }
        return workbook;
    }

    /**
     * Import Functions
     */

    public static <T> Result<T> readToList(InputStream in, Class<T> type)
            throws IOException {
        return readToList(in, new ReadConfiguration<T>().dataType(type));
    }

    public static <T> Result<T> readToList(InputStream in, ReadConfiguration<T> config)
            throws IOException {
        return readToList(tryLoad(in).getSheetAt(0), config);
    }

    public static <T> Result<T> readToList(Sheet sheet, ReadConfiguration<T> config) {
        return new ExcelReader<>(config).readToList(sheet);
    }

    /**
     * Export Functions
     */

    public static <T> void writeTo(List<T> list, OutputStream out) {
        writeTo(list, new WriteConfiguration<T>(), out);
    }

    public static <T> void writeTo(List<T> list, Sheet sheet) {
        writeTo(list, new WriteConfiguration<T>(), sheet);
    }

    public static <T> void writeTo(List<T> list, WriteConfiguration<T> config, OutputStream out) {
        writerFor(config).writeTo(list, out);
    }

    public static <T> void writeTo(List<T> list, WriteConfiguration<T> config, Sheet sheet) {
        writerFor(config).writeTo(list, sheet);
    }

    public static <T> ExcelWriter<T> writerFor(WriteConfiguration<T> config) {
        return new ExcelWriter<>(config);
    }

}
