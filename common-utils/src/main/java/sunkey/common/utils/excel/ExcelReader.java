package sunkey.common.utils.excel;

import sunkey.common.utils.excel.valid.ValidResult;
import sunkey.common.utils.excel.valid.Validator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sunkey
 * @since 2019-06-11 15:09
 **/
@Getter
@Setter
@ToString
public class ExcelReader<T> {

    private final ReadConfiguration<T> config;

    public ExcelReader(ReadConfiguration<T> config) {
        this.config = config;
    }

    public Result<T> readToList(Sheet sheet) {
        Headers<T> headers = readHeaders(sheet);
        if (headers == null) {
            return null;
        }
        Result<T> result = new Result<>(this.config);
        int startRow = config.getDataStartRow();
        int endRow = Math.min(config.getDataEndRow(), sheet.getLastRowNum());
        for (int i = startRow; i <= endRow; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                T value = parseValue(headers, row, result.getValidResult());
                if (value != null) {
                    if (config.isAutoValidate()) {
                        Validator.validate(i, value, result.getValidResult());
                    }
                    result.add(value);
                }
            }
        }
        return result;
    }

    private <T> T parseValue(Headers<T> headers, Row row, ValidResult result) {
        int cols = Math.min(headers.length(), row.getLastCellNum());
        T target = (T) config.getObjectFactory().createObject(config.getDataType());
        for (int i = 0; i < cols; i++) {
            Header header = headers.getHeader(i);
            if (header != null) {
                String value = config.getValueExtractor().extractValue(row.getCell(i));
                try {
                    Object convertedValue = config.getConversionService().convert(
                            value,
                            TypeDescriptor.valueOf(String.class),
                            TypeDescriptor.valueOf(header.getType()));
                    header.setValue(target, convertedValue);
                } catch (ConversionFailedException ex) {
                    result.addError(row.getRowNum(), header.getName(), "格式错误", value);
                }
            }
        }
        headers.setLineNo(target, row.getRowNum());
        return target;
    }

    private <T> Headers<T> readHeaders(Sheet sheet) {
        int rows = sheet.getLastRowNum();
        // contains header line
        if (config.getHeaderRow() < rows) {
            Row row = sheet.getRow(config.getHeaderRow());
            return parseHeaders(row);
        }
        return null;
    }

    private <T> Headers<T> parseHeaders(Row row) {
        int cols = row.getLastCellNum();
        List<String> headers = new ArrayList<>();
        for (int i = 0; i < cols; i++) {
            Cell cell = row.getCell(i);
            headers.add(config.getHeaderNameExtractor().extractValue(cell));
        }
        return new Headers<>(headers, (Class<T>) config.getDataType());
    }

}
