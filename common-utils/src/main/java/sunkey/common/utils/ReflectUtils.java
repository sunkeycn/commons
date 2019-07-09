package sunkey.common.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sunkey
 * @since 2019-04-16 15:37
 **/
public class ReflectUtils {

    public static <T> T mapToObject(Map<String, ?> map, T target) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(target.getClass());
            for (PropertyDescriptor prop : beanInfo.getPropertyDescriptors()) {
                Object propValue = map.get(prop.getName());
                if (propValue != null) {
                    prop.getWriteMethod().invoke(target, propValue);
                }
            }
            return target;
        } catch (InvocationTargetException ex) {
            // caller throws
            if (ex.getTargetException() instanceof RuntimeException) {
                throw (RuntimeException) ex.getTargetException();
            } else {
                throw new RuntimeException(ex.getTargetException());
            }
        } catch (Exception ex) {
            // illegal access should not cause.
            // introspect bean exception should not cause.
            throw new IllegalStateException(ex.getMessage(), ex);
        }
    }

    /**
     *      * 实体类转map
     *      * @param obj
     *      * @return
     *      
     */
    public static Map<String, Object> convertBeanToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    if (null == value) {
                        map.put(key, "");
                    } else {
                        map.put(key, value);
                    }
                }


            }
        } catch (Exception e) {
            System.out.println("convertBean2Map Error" + e);
        }
        return map;
    }

    public static <T> T convertObject(Object obj, Class<T> type) {
        String json = JSON.toJSONString(obj);
        return JSON.parseObject(json, type);
    }

    public static <T> List<T> convertList(Object obj, Class<T> type) {
        String json = JSON.toJSONString(obj);
        return JSON.parseList(json, type);
    }

    public static Map<String, Field> getAllFields(Class<?> type) {
        Map<String, Field> result = new HashMap<>();
        do {
            for (Field field : type.getDeclaredFields()) {
                result.putIfAbsent(field.getName(), field);
            }
            type = type.getSuperclass();
        } while (type != null && type != Object.class);
        return result;
    }

    public static Map<String, Method> getAllMethods(Class<?> type, String... ignores) {
        Map<String, Method> result = new HashMap<>();
        List<String> ignoreList = Arrays.asList(ignores);
        do {
            for (Method method : type.getDeclaredMethods()) {
                if (!ignoreList.isEmpty()
                        && !ignoreList.contains(method.getName())) {
                    result.putIfAbsent(method.getName(), method);
                }
            }
            type = type.getSuperclass();
        } while (type != null && type != Object.class);
        return result;
    }

}
