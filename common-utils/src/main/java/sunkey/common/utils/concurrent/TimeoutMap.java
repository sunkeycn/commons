package sunkey.common.utils.concurrent;

import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sunkey
 * @since 2018-11-27 下午5:37
 **/
public class TimeoutMap<K, V> {

    public static final long DEFAULT_EXPIRE = 60 * 1000L; // 1min
    public static final long MAX_INTERVAL = 60 * 1000L; // 1 min
    public static final long MIN_INTERVAL = 10L; // 0.01 second
    public static final long DEFAULT_INTERVAL = MAX_INTERVAL;

    private static final LinkedList<TimeoutMap> _instances = new LinkedList<>();
    private static volatile long _interval = DEFAULT_INTERVAL;
    private static volatile boolean _start = false;

    @Getter
    private final long defaultExpire;
    private final TimeoutHandler<K, V> timeoutHandler;
    private volatile long nextTriggerTime = Long.MAX_VALUE;
    private volatile Map<K, TimeoutEntry<V>> target = new ConcurrentHashMap<>();

    public TimeoutMap() {
        this(DEFAULT_EXPIRE);
    }

    public TimeoutMap(long defaultExpire) {
        this(defaultExpire, DEFAULT_INTERVAL, null);
    }

    public TimeoutMap(long defaultExpire, long interval, TimeoutHandler<K, V> timeoutHandler) {
        this.defaultExpire = defaultExpire;
        this.timeoutHandler = timeoutHandler;
        _instances.addFirst(this);
        resetInterval(interval);
        checkStart();
    }

    private synchronized void clearExpiredValuesInternal() {
        Map<K, TimeoutEntry<V>> _map = new HashMap<>(target);
        Iterator<Map.Entry<K, TimeoutEntry<V>>> iterator = _map.entrySet().iterator();
        List<Map.Entry<K, TimeoutEntry<V>>> timeouts = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<K, TimeoutEntry<V>> next = iterator.next();
            if (next.getValue().isExpired()) {
                iterator.remove();
                if (timeoutHandler != null)
                    timeouts.add(next);
            }
        }
        this.target = new ConcurrentHashMap<>(_map);
        if (timeoutHandler != null)
            for (Map.Entry<K, TimeoutEntry<V>> timeout : timeouts) {
                timeoutHandler.handleTimeout(timeout.getKey(), timeout.getValue().value);
            }
    }

    public int size() {
        return target.size();
    }

    public boolean isEmpty() {
        return target.isEmpty();
    }

    public boolean containsKey(Object key) {
        return target.containsKey(key);
    }

    public V get(Object key) {
        TimeoutEntry<V> value = target.get(key);
        if (value != null)
            return value.value;
        return null;
    }

    public V put(K key, V value) {
        return put(key, value, defaultExpire);
    }

    public V put(K key, V value, long timeout) {
        TimeoutEntry<V> entry = new TimeoutEntry<>();
        entry.value = value;
        entry.expireTime = System.currentTimeMillis() + timeout;
        if (entry.expireTime < nextTriggerTime) {
            nextTriggerTime = entry.expireTime;
        }
        TimeoutEntry<V> origin = target.put(key, entry);
        if (origin != null)
            return origin.value;
        return null;
    }

    public V remove(Object key) {
        TimeoutEntry<V> value = target.remove(key);
        if (value != null)
            return value.value;
        return null;
    }

    public void clear() {
        target.clear();
    }

    public Set<K> keySet() {
        return target.keySet();
    }

    public static class TimeoutEntry<V> {
        private long expireTime;
        private V value;

        boolean isExpired() {
            return expireTime < System.currentTimeMillis();
        }
    }

    public interface TimeoutHandler<K, V> {
        void handleTimeout(K k, V v);
    }

    private static synchronized void resetInterval(long newInterval) {
        if (newInterval < _interval) {
            if (newInterval < MIN_INTERVAL) {
                throw new IllegalArgumentException("min than min value " + MIN_INTERVAL);
            }
            _interval = newInterval;
        }
    }

    private static synchronized void checkStart() {
        if (!_start) {
            _start = true;
            Thread timeoutMapClearTask = new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(_interval);
                        doClearExpiredValues();
                    } catch (InterruptedException e) {
                        // ILLEGAL STATE
                        Thread.currentThread().interrupt();
                    }
                }
            }, "TimeoutMapClearTask");
            timeoutMapClearTask.setDaemon(true);
            timeoutMapClearTask.start();
        }
    }

    private static void doClearExpiredValues() {
        for (TimeoutMap instance : _instances) {
            if (instance.nextTriggerTime <= System.currentTimeMillis()) {
                instance.clearExpiredValuesInternal();
            }
        }
    }

}
