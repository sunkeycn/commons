package sunkey.common.utils.excel;

import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * @author Sunkey
 * @since 2019-06-10 15:50
 **/
public class Excel {

    /**
     * Import Functions
     */
    public static <T> Result<T> readToList(MultipartFile file, Class<T> type) throws IOException {
        return readToList(file, new ReadConfiguration<T>(type));
    }

    public static <T> Result<T> readToList(MultipartFile file, ReadConfiguration<T> config)
            throws IOException {
        return new ExcelReader<>(config).readToList(
                file.getInputStream(),
                Format.detectFile(file.getOriginalFilename()));
    }

    public static <T> Result<T> readToList(InputStream in, Class<T> type)
            throws IOException {
        return readToList(in, new ReadConfiguration<T>(type));
    }

    public static <T> Result<T> readToList(InputStream in, Class<T> type, Format format)
            throws IOException {
        return readToList(in, new ReadConfiguration<T>(type).format(format));
    }

    public static <T> Result<T> readToList(InputStream in, ReadConfiguration<T> config)
            throws IOException {
        return new ExcelReader<>(config).readToList(in);
    }

    public static <T> Result<T> readToList(Sheet sheet, ReadConfiguration<T> config) {
        return new ExcelReader<>(config).readToList(sheet);
    }

    /**
     * Export Functions
     */

    public static <T> void writeTo(List<T> list, OutputStream out, Class<T> type) {
        writeTo(list, new WriteConfiguration<T>(type), out);
    }

    public static <T> void writeTo(List<T> list, Sheet sheet, Class<T> type) {
        writeTo(list, new WriteConfiguration<T>(type), sheet);
    }

    public static <T> void writeTo(List<T> list, WriteConfiguration<T> config, OutputStream out) {
        new ExcelWriter<>(config).writeTo(list, out);
    }

    public static <T> void writeTo(List<T> list, WriteConfiguration<T> config, Sheet sheet) {
        new ExcelWriter<>(config).writeTo(list, sheet);
    }

}
