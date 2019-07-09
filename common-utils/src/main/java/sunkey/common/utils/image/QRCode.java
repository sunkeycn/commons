package sunkey.common.utils.image;

import com.google.zxing.*;
import com.google.zxing.aztec.AztecReader;
import com.google.zxing.aztec.AztecWriter;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.datamatrix.DataMatrixReader;
import com.google.zxing.datamatrix.DataMatrixWriter;
import com.google.zxing.oned.*;
import com.google.zxing.pdf417.PDF417Reader;
import com.google.zxing.pdf417.PDF417Writer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sunkey
 */
public class QRCode {

    // constants
    public static final String DEFAULT_FORMAT = "jpg";
    public static final String DEFAULT_ENCODING = "UTF-8";
    public static final BarcodeFormat DEFAULT_BARCODE_FORMAT = BarcodeFormat.QR_CODE;
    public static final int DEFAULT_HEIGHT = 200;
    public static final int DEFAULT_WIDTH = 200;
    // private use
    private static final MatrixToImageConfig DEFAULT_CONFIG = new MatrixToImageConfig();
    // initialize
    public static final QRCode DEFAULT = newBuilder().build();
    // attributes
    private final BarcodeFormat barcodeFormat;
    private final Map<EncodeHintType, Object> hints;
    private final Dimension dimension;
    private final String format;
    private final MatrixToImageConfig config;
    // internals
    private final Writer writer;
    private final Reader reader;

    private QRCode(@NonNull BarcodeFormat barcodeFormat,
                   @NonNull Map<EncodeHintType, Object> hints,
                   @NonNull Dimension dimension,
                   @NonNull String format,
                   @NonNull MatrixToImageConfig config) {
        this.barcodeFormat = barcodeFormat;
        this.hints = hints;
        this.dimension = dimension;
        this.format = format;
        this.config = config;
        this.writer = createWriter(barcodeFormat);
        this.reader = createReader(barcodeFormat);
    }

    public String decodeToString(BufferedImage image) {
        return decode(image).getText();
    }

    public Result decode(BufferedImage image) {
        return decode(reader, image);
    }

    public void writeTo(String content, OutputStream out) throws IOException {
        MatrixToImageWriter.writeToStream(bitMatrix(content), format, out, config);
    }

    public BufferedImage toImage(String content) {
        return MatrixToImageWriter.toBufferedImage(bitMatrix(content), config);
    }

    protected BitMatrix bitMatrix(String content) {
        try {
            return writer.encode(content,
                    barcodeFormat,
                    dimension.getWidth(), dimension.getHeight(),
                    hints);// 生成矩阵;
        } catch (WriterException ex) {
            throw new QRCodeException(ex.getMessage(), ex);
        }
    }

    public static class QRCodeException extends RuntimeException {
        public QRCodeException() {
        }

        public QRCodeException(String message) {
            super(message);
        }

        public QRCodeException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private BarcodeFormat barcodeFormat = DEFAULT_BARCODE_FORMAT;
        private Map<EncodeHintType, Object> hints = getDefaultHints();
        private Dimension dimension = new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        private String format = DEFAULT_FORMAT;
        private MatrixToImageConfig config = DEFAULT_CONFIG;

        public Builder charset(String charset) {
            Charset.forName(charset);
            hints.put(EncodeHintType.CHARACTER_SET, charset);
            return this;
        }

        public Builder margin(int pixel) {
            hints.put(EncodeHintType.MARGIN, pixel);
            return this;
        }

        public Builder errorCorrection(ErrorCorrectionLevel level) {
            hints.put(EncodeHintType.ERROR_CORRECTION, level);
            return this;
        }

        /**
         * 1,2,3,4
         *
         * @param level
         * @return
         * @see ErrorCorrectionLevel
         */
        public Builder errorCorrection(int level) {
            level = Math.max(Math.min(level, 1), 4);
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.values()[level - 1]);
            return this;
        }

        public Builder barcodeFormat(BarcodeFormat barcodeFormat) {
            this.barcodeFormat = barcodeFormat;
            return this;
        }

        public Builder format(String format) {
            this.format = format;
            return this;
        }

        public Builder colors(int foreColor, int backColor) {
            this.config = new MatrixToImageConfig(foreColor, backColor);
            return this;
        }

        public Builder dimension(int width, int height) {
            this.dimension = new Dimension(width, height);
            return this;
        }

        public QRCode build() {
            return new QRCode(barcodeFormat, hints, dimension, format, config);
        }

    }

    @Getter
    @Setter
    @ToString
    @RequiredArgsConstructor
    public static class Dimension {
        private final int width;
        private final int height;
    }

    private static Map<EncodeHintType, Object> getDefaultHints() {
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, DEFAULT_ENCODING);
        return hints;
    }

    public static Reader createReader(BarcodeFormat format) {
        Reader reader;
        switch (format) {
            case EAN_8:
                reader = new EAN8Reader();
                break;
            case UPC_E:
                reader = new UPCEReader();
                break;
            case EAN_13:
                reader = new EAN13Reader();
                break;
            case UPC_A:
                reader = new UPCAReader();
                break;
            case QR_CODE:
                reader = new QRCodeReader();
                break;
            case CODE_39:
                reader = new Code39Reader();
                break;
            case CODE_93:
                reader = new Code93Reader();
                break;
            case CODE_128:
                reader = new Code128Reader();
                break;
            case ITF:
                reader = new ITFReader();
                break;
            case PDF_417:
                reader = new PDF417Reader();
                break;
            case CODABAR:
                reader = new CodaBarReader();
                break;
            case DATA_MATRIX:
                reader = new DataMatrixReader();
                break;
            case AZTEC:
                reader = new AztecReader();
                break;
            default:
                throw new IllegalArgumentException("No encoder available for format " + format);
        }
        return reader;
    }

    public static Writer createWriter(BarcodeFormat format) {
        Writer writer;
        switch (format) {
            case EAN_8:
                writer = new EAN8Writer();
                break;
            case UPC_E:
                writer = new UPCEWriter();
                break;
            case EAN_13:
                writer = new EAN13Writer();
                break;
            case UPC_A:
                writer = new UPCAWriter();
                break;
            case QR_CODE:
                writer = new QRCodeWriter();
                break;
            case CODE_39:
                writer = new Code39Writer();
                break;
            case CODE_93:
                writer = new Code93Writer();
                break;
            case CODE_128:
                writer = new Code128Writer();
                break;
            case ITF:
                writer = new ITFWriter();
                break;
            case PDF_417:
                writer = new PDF417Writer();
                break;
            case CODABAR:
                writer = new CodaBarWriter();
                break;
            case DATA_MATRIX:
                writer = new DataMatrixWriter();
                break;
            case AZTEC:
                writer = new AztecWriter();
                break;
            default:
                throw new IllegalArgumentException("No encoder available for format " + format);
        }
        return writer;
    }

    public static String decodeAnythingToString(BufferedImage image) {
        return decodeAnything(image).getText();
    }

    public static Result decodeAnything(BufferedImage image) {
        // temp reader
        return decode(new MultiFormatReader(), image);
    }

    public static Result decode(Reader reader, BufferedImage image) {
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        HybridBinarizer bin = new HybridBinarizer(source);
        BinaryBitmap biMap = new BinaryBitmap(bin);
        try {
            return reader.decode(biMap);
        } catch (Exception e) {
            throw new QRCodeException(e.getMessage(), e);
        }
    }

}
