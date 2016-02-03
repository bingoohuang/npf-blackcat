package com.ailk.ecs.esf.service.microflow.wiring;

import com.ailk.ecs.esf.service.microflow.annotations.LocalEsfContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * EsfContext自动注入
 * 
 * @author Bingoo Huang
 * 
 */
public class EsfContextFieldAutoWiring implements FieldAutoWirable {
    @Override
    public void autoWiring(Object object, Class<?> cls, Field field) {

    }

    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return LocalEsfContext.class;
    }

}
