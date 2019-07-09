package sunkey.common.utils.concurrent;

import sunkey.common.spring.Spring;
import sunkey.common.utils.Assert;
import sunkey.common.utils.Sleep;
import sunkey.common.utils.Snowflake;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author Sunkey
 * @since 2019-05-20 16:32
 **/
@Slf4j
public class RedisLock {

    private final long interval;
    private final String prefix;
    private final String suffix;
    private final Class<? extends RedisTemplate> redisTemplateType;
    private final String redisTemplateName;
    private final long defaultWaitTimeout;
    private final long lockTimeout;

    private volatile RedisTemplate redisTemplate;
    private final String instanceId = Long.toHexString(INSTANCE_ID_GENERATOR.nextId());

    public RedisLock(long interval, String prefix, String suffix,
                     Class<? extends RedisTemplate> redisTemplateType, String redisTemplateName,
                     long defaultWaitTimeout, long lockTimeout) {
        this.interval = interval;
        this.prefix = prefix;
        this.suffix = suffix;
        this.redisTemplateType = redisTemplateType;
        this.redisTemplateName = redisTemplateName;
        this.defaultWaitTimeout = defaultWaitTimeout;
        this.lockTimeout = lockTimeout;
        validate();
    }

    private void validate() {
        Assert.state(interval > 0, "interval should bigger than 0!");
        Assert.notNull(prefix, "prefix");
        Assert.notNull(suffix, "suffix");
        if (redisTemplateType == null && redisTemplateName == null) {
            throw new IllegalArgumentException("both redisTemplateType and redisTemplateName are null!");
        }
        if (redisTemplateType != null && redisTemplateName != null) {
            throw new IllegalArgumentException("both redisTemplateType and redisTemplateName are not null!");
        }
        Assert.state(defaultWaitTimeout > 0, "defaultWaitTimeout should bigger than 0!");
        Assert.state(lockTimeout > 0, "lockTimeout should bigger than 0!");
    }

    public boolean doLock(String identifier, Runnable runnable,
                          long waitTimeout, TimeUnit unit) {
        try {
            boolean lock = lock(identifier, waitTimeout, unit);
            if (lock) {
                runnable.run();
            }
            return lock;
        } catch (Throwable ex) {
            log.error("");
            return false;
        } finally {
            unlock(identifier);
        }
    }

    /**
     * 尝试锁，不等待
     *
     * @param identifier
     * @return
     */
    public boolean tryLock(String identifier) {
        return lock(identifier, 0, TimeUnit.MILLISECONDS);
    }

    /**
     * 使用默认等待时间，并不是一直阻塞
     *
     * @param identifier
     * @return
     */
    public boolean lock(String identifier) {
        return lock(identifier, defaultWaitTimeout, TimeUnit.MILLISECONDS);
    }

    protected String lockId() {
        return instanceId + Thread.currentThread().getName();
    }

    /**
     * 解锁
     *
     * @param identifier
     */
    public void unlock(String identifier) {
        prepare();
        Current.clear();
        byte[] bKey = serial(getKey(identifier));
        String lockId = lockId();
        redisTemplate.execute((RedisCallback) conn -> {
            String oLockId = deserial(conn.get(bKey));
            if (lockId.equals(oLockId)) {
                conn.del(bKey);
            }
            return null;
        });
    }

    protected String getKey(String identifier) {
        return prefix + identifier + suffix;
    }

    /**
     * @param identifier  唯一业务标识，锁标识
     * @param waitTimeout 等待锁超时时间
     * @param unit        时间单位
     * @return 持有锁结果
     */
    public boolean lock(String identifier, long waitTimeout, TimeUnit unit) {
        // 懒加载:从SpringContext获得RedisTemplate实例
        prepare();
        waitTimeout = unit.toMillis(waitTimeout);
        long stopWait = System.currentTimeMillis() + waitTimeout;
        String key = getKey(identifier);
        // 锁定后的最长释放时间，即锁超时时间
        long _lockTimeout = Math.max(lockTimeout, waitTimeout);
        // 唯一锁标识
        String lockId = lockId();
        // 第一次尝试持有锁
        // 操作Redis
        boolean tryLock = setNX(key, lockId, _lockTimeout);
        // 如果tryLock(wait=0)则立即返回
        if (tryLock || waitTimeout <= 0) {
            // 锁成功
            Current.set(this, identifier);
            return tryLock;
        }
        // 重试持有锁
        // 最小循环重试单位时间
        long _interval = Math.min(interval, waitTimeout);
        // 随机重试时间: 50-100ms
        long _bound = _interval * 2;
        // 距离停止重试的时间
        long awaitTime;
        while (!tryLock && (awaitTime = stopWait - System.currentTimeMillis()) > 0) {
            if (awaitTime < _interval) { // 如果距离停止重试的时间已经小于最小循环单位时间
                Sleep.sleep(awaitTime);
            } else {
                Sleep.random(_interval, _bound);
            }
            // 不设置超时时间, 只设置值
            // 操作Redis
            tryLock = setNX(key, lockId, 0);
        }
        // 如果持有锁成功, 则设置超时时间
        if (tryLock) {
            // 持有锁后重新设置超时时间
            // 操作Redis
            Current.set(this, identifier);
            redisTemplate.expire(key, _lockTimeout, TimeUnit.MILLISECONDS);
        }
        return tryLock;
    }

    /**
     * @param key
     * @param value
     * @param expireMillis
     * @return
     */
    protected boolean setNX(String key, String value, long expireMillis) {
        Assert.state(expireMillis > 0, "expireMillis should bigger than 0!");
        return (Boolean) redisTemplate.execute((RedisCallback) conn -> {
            byte[] serialKey = serial(key);
            boolean suc = conn.setNX(serialKey, serial(value));
            if (suc && expireMillis > 0) {
                conn.pExpire(serialKey, expireMillis);
            }
            return suc;
        });
    }

    private byte[] serial(String str) {
        return str == null ? null : str.getBytes(StandardCharsets.UTF_8);
    }

    public String deserial(byte[] val) {
        return val == null ? null : new String(val, StandardCharsets.UTF_8);
    }

    private void prepare() {
        if (redisTemplate == null) {
            if (redisTemplateName != null) {
                this.redisTemplate = Spring.getBean(redisTemplateName);
            } else if (redisTemplateType != null) {
                this.redisTemplate = Spring.getBean(redisTemplateType);
            }
            if (redisTemplate == null) {
                throw new IllegalStateException("redisTemplate not found!");
            }
        }
    }

    public static Builder newLock() {
        return new Builder();
    }

    public static final long DEFAULT_QUERY_LOOP_INTERVAL = 50; // ms
    public static final Class<? extends RedisTemplate> DEFAULT_TEMPLATE_TYPE
            = StringRedisTemplate.class;
    public static final String DEFAULT_PREFIX = "lock_";
    public static final String DEFAULT_SUFFIX = ".lock";
    public static final long DEFAULT_WAIT_TIMEOUT = 15 * 1000; // 15s
    public static final long DEFAULT_LOCK_TIMEOUT = 10 * 60 * 1000; // 10min

    private static final Snowflake INSTANCE_ID_GENERATOR = Snowflake.newBuilder()
            .epochYear("2019")
            .build();

    public static class Builder {

        private long interval = DEFAULT_QUERY_LOOP_INTERVAL;
        private Class<? extends RedisTemplate> redisTemplateType = DEFAULT_TEMPLATE_TYPE;
        private String redisTemplateName;
        private String prefix = DEFAULT_PREFIX;
        private String suffix = DEFAULT_SUFFIX;
        private long waitTimeout = DEFAULT_WAIT_TIMEOUT;
        private long lockTimeout = DEFAULT_LOCK_TIMEOUT;

        public Builder interval(long interval) {
            this.interval = interval;
            return this;
        }

        public Builder redisTemplate(String beanName) {
            redisTemplateType = null;
            redisTemplateName = beanName;
            return this;
        }

        public Builder redisTemplate(Class<? extends RedisTemplate> beanType) {
            redisTemplateType = beanType;
            redisTemplateName = null;
            return this;
        }

        public Builder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder suffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        public Builder waitTimeout(long waitTimeout) {
            this.waitTimeout = waitTimeout;
            return this;
        }

        public Builder lockTimeout(long lockTimeout) {
            this.lockTimeout = lockTimeout;
            return this;
        }

        public RedisLock build() {
            return new RedisLock(interval, prefix, suffix,
                    redisTemplateType, redisTemplateName,
                    waitTimeout, lockTimeout);
        }

    }

    @AllArgsConstructor
    @Getter
    static class LockAction {

        private RedisLock lock;
        private String identifier;

    }

    public static class Current {

        private static final ThreadLocal<LockAction> local = new ThreadLocal<>();

        static void set(RedisLock lock, String identifier) {
            local.set(new LockAction(lock, identifier));
        }

        static void clear() {
            local.remove();
        }

        public void unlock() {
            LockAction action = local.get();
            if (action != null) {
                action.getLock().unlock(action.getIdentifier());
            }
        }

    }

}
