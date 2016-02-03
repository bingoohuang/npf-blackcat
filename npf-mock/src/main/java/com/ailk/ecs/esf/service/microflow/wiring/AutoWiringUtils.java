package com.ailk.ecs.esf.service.microflow.wiring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.ailk.ecs.esf.base.utils.CglibUtils;

/**
 * 自动注入帮助类.
 * 
 * @author Bingoo Huang
 * 
 */
public abstract class AutoWiringUtils {
    /**
     * 是否有任一方法有指定的标注.
     * 
     * @param stepClass
     * @param methodAnnotation1
     * @param methodAnnotations
     * @return
     */
    @SuppressWarnings("unchecked")
    public static boolean hasAnyOfMethodAnnotation(Class<?> stepClass, Class<?> methodAnnotation1,
            Class<?>... methodAnnotations) {
        Method[] methods = stepClass.getDeclaredMethods();
        for (final Method m : methods) {
            if (Modifier.isPrivate(m.getModifiers())) {
                continue;
            }

            if (m.isAnnotationPresent((Class<? extends Annotation>) methodAnnotation1)) {
                return true;
            }

            for (Class<?> methodAnnotation : methodAnnotations) {
                if (m.isAnnotationPresent((Class<? extends Annotation>) methodAnnotation)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 为对象注入资源.
     * 
     * @param serviceObject
     * @param fieldAutoWirings
     * @throws Exception
     */
    public static void wire(Object serviceObject, FieldAutoWirable... fieldAutoWirings)
            throws Exception {
        Class<?> cls = serviceObject.getClass();
        while (cls != Object.class) {
            cls = wire(serviceObject, cls, fieldAutoWirings);
        }
    }

    public static Class<?> wire(Object serviceObject, Class<?> cls,
            FieldAutoWirable... fieldAutoWirings) throws Exception {
        if (CglibUtils.isDynaProxyClass(cls)) {
            cls = cls.getSuperclass();
        }

        for (Field field : cls.getDeclaredFields()) {
            for (FieldAutoWirable fieldAutoWiring : fieldAutoWirings) {
                if (field.isAnnotationPresent(fieldAutoWiring.getAnnotationClass())) {
                    fieldAutoWiring.autoWiring(serviceObject, cls, field);
                    break;
                }
            }
        }

        return cls.getSuperclass();
    }
}
