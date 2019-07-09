package sunkey.common.utils;

/**
 * @author Sunkey
 * @since 2018-04-10 下午3:50
 **/
public class FinalReference<T> {

    private volatile T reference;

    public void set(T t) {
        if (reference != null)
            throw new IllegalStateException("cannot update final object!");
        reference = t;
    }

    public T get() {
        if (reference == null)
            throw new IllegalStateException("final object not initialize!");
        return reference;
    }

    public T get(T def) {
        if (reference == null) return def;
        return reference;
    }

}
