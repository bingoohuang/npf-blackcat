package com.ailk.ecs.esf.service.microflow.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DAO资源标注.
 * 
 * @author Binoo Huang
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DaoSqlMap {
    /**
     * sqlmap文件类路径.<br>
     * sqlmap文件可以包含多个sql文件.<br>
     * 
     * @return
     */
    String value();

    /**
     * 数据源的key.
     * 
     * @return
     */
    String dsKey() default "";

}
