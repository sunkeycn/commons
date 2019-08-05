package sunkey.common.utils.excel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sunkey.common.utils.Assert;
import sunkey.common.utils.excel.support.WorkbookProvider;

@Getter
@AllArgsConstructor
public enum Format {

    XLS(WorkbookProvider.HSSF),
    XLSX(WorkbookProvider.XSSF),
    XLSX_STREAM(WorkbookProvider.SXSSF),
    ;

    private final WorkbookProvider provider;

    public static Format detectFile(String fileName) {
        Assert.requireArg(fileName, "fileName");
        int idx = fileName.lastIndexOf('.');
        if (idx != -1) {
            String fmt = fileName.substring(idx + 1).toLowerCase();
            switch (fmt) {
                case "xls":
                    return XLS;
                case "xlsx":
                    return XLSX;
                default:
                    return null;
            }
        } else {
            return null;
        }
    }

}