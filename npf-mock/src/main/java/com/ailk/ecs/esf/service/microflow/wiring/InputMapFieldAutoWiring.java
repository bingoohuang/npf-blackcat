package com.ailk.ecs.esf.service.microflow.wiring;

import com.ailk.ecs.esf.service.microflow.annotations.LocalInputMap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * inputmap标注自动注入.
 * 
 * @author Bingoo Huang
 * 
 */
public class InputMapFieldAutoWiring implements FieldAutoWirable {

    @Override
    public void autoWiring(Object object, Class<?> cls, Field field) {
    }

    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return LocalInputMap.class;
    }
}
