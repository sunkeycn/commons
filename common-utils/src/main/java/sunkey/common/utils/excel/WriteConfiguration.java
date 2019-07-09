package sunkey.common.utils.excel;

import sunkey.common.utils.excel.support.NoneStyler;
import sunkey.common.utils.excel.support.Styler;
import sunkey.common.utils.excel.support.WorkbookProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * @author Sunkey
 * @since 2019-07-03 14:25
 **/

@ToString
public class WriteConfiguration<T> {

    private WorkbookProvider provider = WorkbookProvider.HSSF;
    private Styler headerStyler = NoneStyler.INSTANCE;
    private Styler dataStyler = NoneStyler.INSTANCE;
    private int headerRow = 0;
    private int dataStartRow = 1;
    private Class<T> dataType = null;

    public Class<T> resolveDataType(List<T> list) {
        if (dataType != null) {
            return dataType;
        }
        if (list == null || list.isEmpty()) {
            return null;
        }
        for (T value : list) {
            if (value != null) {
                return (Class<T>) value.getClass();
            }
        }
        return null;
    }

    public Class<T> dataType() {
        return dataType;
    }

    public WriteConfiguration dataType(Class<T> dataType) {
        this.dataType = dataType;
        return this;
    }

    public int headerRow() {
        return headerRow;
    }

    public WriteConfiguration headerRow(int headerRow) {
        this.headerRow = headerRow;
        return this;
    }

    public int dataStartRow() {
        return dataStartRow;
    }

    public WriteConfiguration dataStartRow(int dataStartRow) {
        this.dataStartRow = dataStartRow;
        return this;
    }

    public WriteConfiguration writeType(Type writeType) {
        this.provider = writeType.provider;
        return this;
    }

    public WorkbookProvider provider() {
        return provider;
    }

    public WriteConfiguration provider(WorkbookProvider provider) {
        this.provider = provider;
        return this;
    }

    public Styler headerStyler() {
        return headerStyler;
    }

    public WriteConfiguration headerStyler(Styler headerStyler) {
        this.headerStyler = headerStyler;
        return this;
    }

    public Styler dataStyler() {
        return dataStyler;
    }

    public WriteConfiguration dataStyler(Styler dataStyler) {
        this.dataStyler = dataStyler;
        return this;
    }

    @Getter
    @AllArgsConstructor
    public enum Type {

        XLS(WorkbookProvider.HSSF),
        XLSX(WorkbookProvider.XSSF),
        XLSX_STREAM(WorkbookProvider.SXSSF),
        ;

        private final WorkbookProvider provider;

    }

}
