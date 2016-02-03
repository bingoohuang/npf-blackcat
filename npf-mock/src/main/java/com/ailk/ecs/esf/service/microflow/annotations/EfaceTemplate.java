package com.ailk.ecs.esf.service.microflow.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Eface报文模板资源标注.
 * 
 * @author Bingoo Huang
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EfaceTemplate {
    /**
     * 模板对应的接口文件.<br>
     * 模板名称可以根据该接口模板文件的名称来确定.<br>
     * 模板路径可以从该接口中所使用的@TemplatePath标注来确定.<br>
     * 
     * @return
     */
    public Class<?> value();

}
