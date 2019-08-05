package sunkey.common.utils.excel;

import lombok.*;
import sunkey.common.utils.excel.support.NoneStyler;
import sunkey.common.utils.excel.support.Styler;
import sunkey.common.utils.excel.support.WorkbookProvider;

import java.util.List;

/**
 * @author Sunkey
 * @since 2019-07-03 14:25
 **/

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class WriteConfiguration<T> {

    @NonNull
    private final Class<T> dataType;
    private WorkbookProvider provider = WorkbookProvider.XSSF;
    private Styler headerStyler = NoneStyler.INSTANCE;
    private Styler dataStyler = NoneStyler.INSTANCE;
    private int headerRow = 0;
    private int dataStartRow = 1;

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

    public WriteConfiguration format(Format format) {
        this.provider = format.getProvider();
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

}
