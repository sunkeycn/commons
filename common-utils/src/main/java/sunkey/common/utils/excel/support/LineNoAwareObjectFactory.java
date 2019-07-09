package sunkey.common.utils.excel.support;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.Factory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sunkey
 * @since 2019-06-12 10:34
 **/
public class LineNoAwareObjectFactory implements ObjectFactory {

    public static final LineNoAwareObjectFactory INSTANCE = new LineNoAwareObjectFactory();

    private final Map<Class, Factory> factories = new HashMap<>();

    @Override
    public <T> T createObject(Class<T> type) {
        Factory factory = factories.computeIfAbsent(type, this::createFactory);
        return (T) factory.newInstance(new Injector());
    }

    private Factory createFactory(Class type) {
        return (Factory) Enhancer.create(type, new Class[]{LineNoAware.class}, new Injector());
    }

    public static class Injector implements MethodInterceptor {

        private int lineNo = 0;

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy)
                throws Throwable {
            if (method.getName().equals("getLineNo") && method.getParameterCount() == 0) {
                return lineNo;
            } else if (method.getName().equals("setLineNo") && method.getParameterCount() == 1 &&
                    method.getParameterTypes()[0] == Integer.TYPE) {
                this.lineNo = (Integer) objects[0];
                return null;
            } else {
                return methodProxy.invokeSuper(o, objects);
            }
        }

    }

}
