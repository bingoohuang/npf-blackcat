package com.ailk.ecs.esf.service.microflow.wiring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 标注字段自动注入接口.
 * @author Bingoo Huang
 *
 */
public interface FieldAutoWirable {
    Class<? extends Annotation> getAnnotationClass();
    void autoWiring(Object object, Class<?> cls, Field field);
}
