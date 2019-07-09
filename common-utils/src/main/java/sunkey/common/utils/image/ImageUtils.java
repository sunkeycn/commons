package sunkey.common.utils.image;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.ToString;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

/**
 * @author Sunkey
 */
public class ImageUtils {

    @SneakyThrows
    public static void mergeImagesAndWrite(BufferedImage background,
                                           BufferedImage foreground,
                                           Align fX, Align fY, OutputStream out, String format) {
        BufferedImage bufferedImage = mergeImages(background, foreground, fX, fY);
        ImageIO.write(bufferedImage, format, out);
    }

    public static BufferedImage mergeImages(BufferedImage background,
                                            BufferedImage foreground,
                                            Align fX, Align fY) {
        int bWidth = background.getWidth();
        int bHeight = background.getHeight();
        int fWidth = foreground.getWidth();
        int fHeight = foreground.getHeight();
        if (bWidth < fWidth || bHeight < fHeight) {
            throw new IllegalArgumentException("图片大小不符合要求");
        }
        BufferedImage combined = new BufferedImage(bWidth, bHeight, background.getType());
        Graphics g = combined.getGraphics();
        g.drawImage(background, 0, 0, null);
        g.drawImage(foreground, fX.calc(bWidth, fWidth), fY.calc(bHeight, fHeight), null);
        g.dispose();
        return combined;
    }

    @Getter
    @Setter
    @ToString
    public static class Align {
        private boolean positive;
        private boolean excludeSelf;
        private int pixel;

        public static Align left(int pixel) {
            return new Align(true, false, pixel);
        }

        public static Align right(int pixel, boolean excludeSelf) {
            return new Align(false, excludeSelf, pixel);
        }

        public static Align right(int pixel) {
            return right(pixel, false);
        }

        public static Align top(int pixel) {
            return new Align(true, false, pixel);
        }

        public static Align bottom(int pixel, boolean excludeSelf) {
            return new Align(false, excludeSelf, pixel);
        }

        public static Align bottom(int pixel) {
            return bottom(pixel, false);
        }

        private Align(boolean positive, boolean excludeSelf, int pixel) {
            this.positive = positive;
            this.excludeSelf = excludeSelf;
            this.pixel = pixel;
        }

        private int calc(int bVal, int fVal) {
            if (positive) {
                return pixel;
            }
            int exclude = fVal;
            if (excludeSelf) {
                exclude = 0;
            }
            return bVal - exclude - pixel;
        }

    }

}
