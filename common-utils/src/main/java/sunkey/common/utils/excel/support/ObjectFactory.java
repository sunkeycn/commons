package sunkey.common.utils.excel.support;

public interface ObjectFactory {
    <T> T createObject(Class<T> type);
}