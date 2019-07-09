package sunkey.common.utils.concurrent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Sunkey
 * @since 2018-11-27 下午5:37
 **/
public class RotatingMap<K, V> {

    private static final int DEFAULT_NUM_BUCKETS = 3;

    public interface ExpiredCallback<K, V> {
        void expire(K key, V val);
    }

    private LinkedList<HashMap<K, V>> _buckets;

    private ExpiredCallback _callback;

    public RotatingMap(int numBuckets, ExpiredCallback<K, V> callback, long rotateInterval) {
        if (numBuckets < 2) {
            throw new IllegalArgumentException("numBuckets must be >= 2");
        }
        _buckets = new LinkedList<>();
        for (int i = 0; i < numBuckets; i++) {
            _buckets.add(new HashMap<>());
        }
        _callback = callback;
        if (rotateInterval > 0) {
            Scheduler.startRotateWith(this, rotateInterval);
        }
    }

    public RotatingMap(ExpiredCallback<K, V> callback) {
        this(DEFAULT_NUM_BUCKETS, callback, 0);
    }

    public RotatingMap(int numBuckets) {
        this(numBuckets, null, 0);
    }

    public Map<K, V> rotate() {
        Map<K, V> dead = _buckets.removeLast();
        _buckets.addFirst(new HashMap<K, V>());
        if (_callback != null) {
            for (Map.Entry<K, V> entry : dead.entrySet()) {
                _callback.expire(entry.getKey(), entry.getValue());
            }
        }
        return dead;
    }

    public boolean containsKey(K key) {
        for (HashMap<K, V> bucket : _buckets) {
            if (bucket.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    public V get(K key) {
        for (HashMap<K, V> bucket : _buckets) {
            if (bucket.containsKey(key)) {
                return bucket.get(key);
            }
        }
        return null;
    }

    public void put(K key, V value) {
        Iterator<HashMap<K, V>> it = _buckets.iterator();
        HashMap<K, V> bucket = it.next();
        bucket.put(key, value);
        while (it.hasNext()) {
            bucket = it.next();
            bucket.remove(key);
        }
    }


    public Object remove(K key) {
        for (HashMap<K, V> bucket : _buckets) {
            if (bucket.containsKey(key)) {
                return bucket.remove(key);
            }
        }
        return null;
    }

    public int size() {
        int size = 0;
        for (HashMap<K, V> bucket : _buckets) {
            size += bucket.size();
        }
        return size;
    }

}