package sunkey.common.utils.excel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.convert.TypeDescriptor;
import sunkey.common.utils.NumberUtils;
import sunkey.common.utils.StringUtils;
import sunkey.common.utils.excel.support.DefaultStringValueExtractor;
import sunkey.common.utils.excel.valid.ValidResult;
import sunkey.common.utils.excel.valid.Validator;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    public Result<T> readToList(InputStream in) throws IOException {
        return readToList(in, config.format());
    }

    public Result<T> readToList(InputStream in, Format format) throws IOException {
        Workbook workbook = format.getProvider().createWorkbook(in);
        return readToList(workbook.getSheetAt(0));
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

    private String formattedString(String format, Object val) {
        if (val instanceof Date) {
            return new SimpleDateFormat(format).format((Date) val);
        }
        if (val instanceof Number) {
            return new DecimalFormat(format).format(val);
        }
        return null;
    }

    private Date formattedDate(String format, String val) throws ParseException {
        return new SimpleDateFormat(format).parse(val);
    }

    private Object formattedNumber(String format, String val, Class requireType) throws ParseException {
        Number parsedValue = new DecimalFormat(format).parse(val);
        return NumberUtils.convertTo(parsedValue, requireType);
    }

    private Object convertValue(Header header, Object value) throws ParseException {
        if (value == null) {
            return null;
        }
        Class<?> type = header.getType();
        if (type.isInstance(value)) {
            return value;
        }
        if (StringUtils.hasText(header.getFormat())) {
            if (type == Date.class && value instanceof String) {
                return formattedDate(header.getFormat(), (String) value);
            }
            if (Number.class.isAssignableFrom(type)) {
                return formattedNumber(header.getFormat(), value.toString(), type);
            }
            if (type == String.class) {
                return formattedString(header.getFormat(), value);
            }
        }
        // fallback
        return config.getConversionService().convert(value,
                TypeDescriptor.valueOf(value.getClass()),
                TypeDescriptor.valueOf(type));
    }

    private <T> T parseValue(Headers<T> headers, Row row, ValidResult result) {
        int cols = Math.min(headers.length(), row.getLastCellNum());
        T target = (T) config.getObjectFactory().createObject(config.getDataType());
        for (int i = 0; i < cols; i++) {
            Header header = headers.getHeader(i);
            if (header != null) {
                Object value = null;
                try {
                    value = config.getValueExtractor().extractValue(header, row.getCell(i));
                    Object convertedValue = convertValue(header, value);
                    header.setValue(target, convertedValue);
                } catch (Exception ex) {
                    if (value == null) {
                        // Value Fallback
                        value = DefaultStringValueExtractor.INSTANCE.extractValue(row.getCell(i));
                    }
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
