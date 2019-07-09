package sunkey.common.utils.excel.support;

public class DefaultObjectFactory implements ObjectFactory {

    public static final DefaultObjectFactory INSTANCE = new DefaultObjectFactory();

    @Override
    public <T> T createObject(Class<T> type) {
        try {
            return (T) type.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

}