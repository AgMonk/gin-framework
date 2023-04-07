package utils.reflect;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import annotation.MyRestController;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 反射工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 17:15
 */
public class ReflectUtils {

    /**
     * 获取类的别名
     * @param clazz 类对象
     * @return 别名
     */
    public static String getAliasName(Class<?> clazz) {
        final Schema schema = clazz.getAnnotation(Schema.class);
        return schema != null ? schema.description() : clazz.getSimpleName();
    }

    /**
     * 返回一个对象所有的字段和字段值(含父类)
     * @param obj 对象
     * @return 字段和字段值
     */
    public static List<FieldValue> getAllFieldValues(Object obj) {
        return getAllFields(obj.getClass()).stream().map(field -> {
            final Object value = getFieldValue(field, obj);
            return new FieldValue(field, value);
        }).toList();
    }

    /**
     * 返回一个类的所有字段，含所有父类字段
     * @param clazz 类对象
     * @return 字段
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> list = new ArrayList<>(List.of(clazz.getDeclaredFields()));
        final Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            list.addAll(0, getAllFields(superclass));
        }
        return list;
    }

    /**
     * 返回持有4种注解的元素
     * @param annotatedElement 元素
     * @return 接口方法
     */
    public static List<String> getApiPath(AnnotatedElement annotatedElement) {
        MyRestController a0 = annotatedElement.getAnnotation(MyRestController.class);
        if (a0 != null) {
            return Arrays.asList(a0.value());
        }
        RequestMapping a1 = annotatedElement.getAnnotation(RequestMapping.class);
        if (a1 != null) {
            return Arrays.asList(a1.value());
        }
        PostMapping a2 = annotatedElement.getAnnotation(PostMapping.class);
        if (a2 != null) {
            return Arrays.asList(a2.value());
        }
        GetMapping a3 = annotatedElement.getAnnotation(GetMapping.class);
        if (a3 != null) {
            return Arrays.asList(a3.value());
        }
        return new ArrayList<>();
    }

    /**
     * 从Controller实例中获取本类对象
     * @param controllerInstance controller 实例
     * @return 类对象
     */
    public static Class<?> getControllerClass(Object controllerInstance) {
        final String name = controllerInstance.getClass().getName();
        final String className = name.contains("$") ? name.substring(0, name.indexOf("$")) : name;
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }


    public static Object getFieldValue(Field field, Object obj) {
        try {
            field.setAccessible(true);
            final Object res = field.get(obj);
            field.setAccessible(false);
            return res;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 返回一个对象所有的字段和字段值
     * @param obj 对象
     * @return 字段和字段值
     */
    public static List<FieldValue> getFieldValues(Object obj) {
        return Arrays.stream(obj.getClass().getDeclaredFields()).map(field -> {
            final Object value = getFieldValue(field, obj);
            return new FieldValue(field, value);
        }).toList();
    }
}   
