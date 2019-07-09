package sunkey.common.utils.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Sunkey
 * @since 2018-11-27 下午6:35
 **/
class Scheduler {

    private static final AtomicInteger counter = new AtomicInteger(0);

    private static final ScheduledExecutorService executor =
            Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors(),
                    runnable -> new Thread(runnable, "RotateMap-" + counter.incrementAndGet()));

    static ScheduledFuture<?> startRotateWith(RotatingMap map, long interval) {
        return executor.scheduleWithFixedDelay(() -> {
            map.rotate();
        }, interval, interval, TimeUnit.MILLISECONDS);
    }

}
