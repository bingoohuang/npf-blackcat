package com.ailk.ecs.esf.service.microflow.wiring;

import com.ailk.ecs.esf.service.microflow.annotations.EfaceTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Eface标注资源自动注入.
 * 
 * @author Bingoo Huang
 * 
 */
public class EfaceFieldAutoWiring implements FieldAutoWirable {
    @Override
    public void autoWiring(Object object, Class<?> cls, Field field) {

    }

    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return EfaceTemplate.class;
    }

}
