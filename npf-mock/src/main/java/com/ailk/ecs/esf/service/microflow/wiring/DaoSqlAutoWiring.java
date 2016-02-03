package com.ailk.ecs.esf.service.microflow.wiring;

import com.ailk.ecs.esf.service.microflow.annotations.DaoSql;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * DAO标注字段注入.
 *
 * @author Bingoo Huang
 */
public class DaoSqlAutoWiring implements FieldAutoWirable {
    @Override
    public void autoWiring(Object object, Class<?> cls, Field field) {
    }

    @Override
    public Class<? extends Annotation> getAnnotationClass() {
        return DaoSql.class;
    }

}
