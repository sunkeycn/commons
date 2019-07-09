package sunkey.common.utils.excel;

import sunkey.common.utils.Dates;
import sunkey.common.utils.StringUtils;
import sunkey.common.utils.excel.support.WriterException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Sunkey
 * @since 2019-07-03 14:57
 **/

@Getter
@Setter
@ToString
public class ExcelWriter<T> {

    private final WriteConfiguration<T> config;

    public ExcelWriter(WriteConfiguration<T> config) {
        this.config = config;
    }

    public void writeTo(List<T> list, OutputStream out) throws WriterException {
        Workbook workbook = config.provider().createWorkbook();
        Sheet sheet = workbook.createSheet();
        writeTo(list, sheet);
        try {
            workbook.write(out);
        } catch (IOException e) {
            throw new WriterException(e.getMessage(), e);
        }
    }

    public void writeTo(List<T> list, Sheet sheet) throws WriterException {
        if (list == null || list.isEmpty()) {
            return;
        }
        Workbook workbook = sheet.getWorkbook();
        CellStyle headerStyle = config.headerStyler().style(workbook);
        CellStyle dataStyle = config.dataStyler().style(workbook);
        Row headerRow = sheet.createRow(config.headerRow());
        Class<T> dataType = config.resolveDataType(list);
        if (dataType == null) {
            throw new IllegalArgumentException("cannot decide data type");
        }
        FieldWriter[] headers = parseHeaders(dataType);
        writeHeaders(headers, headerStyle, headerRow);
        int row = config.dataStartRow();
        for (T data : list) {
            if (data == null) {
                continue;
            }
            Row dataRow = sheet.createRow(row++);
            writeDataLine(data, headers, dataStyle, dataRow);
        }
    }

    private void writeDataLine(Object target, FieldWriter[] headers,
                               CellStyle dataStyle, Row dataRow) {
        for (int i = 0; i < headers.length; i++) {
            Cell cell = dataRow.createCell(i);
            if (dataStyle != null) {
                cell.setCellStyle(dataStyle);
            }
            headers[i].write(target, cell);
        }
    }

    private void writeHeaders(FieldWriter[] headers, CellStyle headerStyle, Row headerRow) {
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            if (headerStyle != null) {
                cell.setCellStyle(headerStyle);
            }
            cell.setCellValue(headers[i].fieldName);
        }
    }

    /**
     * 不会递归查找属性
     *
     * @param dataType
     * @return
     */
    private FieldWriter[] parseHeaders(Class<T> dataType) {
        ArrayList<FieldWriter> writers = new ArrayList<>();
        for (Field field : dataType.getDeclaredFields()) {
            ExcelField ann = field.getAnnotation(ExcelField.class);
            if (ann != null) {
                writers.add(new FieldWriter(ann, field));
            }
        }
        return writers.toArray(new FieldWriter[writers.size()]);
    }

    @Getter
    @Setter
    @ToString
    static class FieldWriter {
        private String fieldName;
        private String format;
        private Field field;
        private DecimalFormat df;

        FieldWriter(ExcelField ann, Field f) {
            this.fieldName = ann.value();
            this.format = ann.format();
            this.field = f;
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            if (Number.class.isAssignableFrom(f.getType()) && StringUtils.hasLength(format)) {
                this.df = new DecimalFormat(format);
            }
        }

        void write(Object target, Cell cell) {
            try {
                Object value = field.get(target);
                if (value != null) {
                    if (value instanceof Date) {
                        writeDateValue((Date) value, cell);
                    } else if (value instanceof TemporalAccessor) {
                        writeDateValue((TemporalAccessor) value, cell);
                    } else if (value instanceof Calendar) {
                        writeDateValue((Calendar) value, cell);
                    } else if (value instanceof Number) {
                        writeNumberValue((Number) value, cell);
                    } else {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cell.setCellValue(String.valueOf(value));
                    }
                }
            } catch (IllegalAccessException e) {
                // non reach here
            }
        }

        void writeNumberValue(Number number, Cell cell) {
            if (StringUtils.hasLength(format)) {
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue(df.format(number));
            } else {
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                cell.setCellValue(number.doubleValue());
            }
        }

        void writeDateValue(TemporalAccessor value, Cell cell) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if (StringUtils.hasLength(format)) {
                cell.setCellValue(Dates.format(value, format));
            } else {
                cell.setCellValue(Dates.toClassicDate(value));
            }
        }

        void writeDateValue(Calendar value, Cell cell) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if (StringUtils.hasLength(format)) {
                cell.setCellValue(Dates.format(value.getTimeInMillis(), format));
            } else {
                cell.setCellValue(value);
            }
        }

        void writeDateValue(Date value, Cell cell) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            if (StringUtils.hasLength(format)) {
                cell.setCellValue(Dates.format(value, format));
            } else {
                cell.setCellValue(value);
            }
        }

    }

}
