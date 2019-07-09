package sunkey.common.utils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Sunkey
 * @since 2019-05-20 17:54
 **/
public class Sleep {

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
        }
    }

    public static void random(long bound) {
        sleep(ThreadLocalRandom.current().nextLong(bound));
    }

    public static void random(long offset, long bound) {
        sleep(ThreadLocalRandom.current().nextLong(offset, bound));
    }

}
