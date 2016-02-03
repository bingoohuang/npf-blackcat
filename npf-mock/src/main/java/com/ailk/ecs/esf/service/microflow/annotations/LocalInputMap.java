package com.ailk.ecs.esf.service.microflow.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * LocalInputMap标注.<br>
 * 通过该标注自动注入当前线程的inputmap实例.<br>
 * @author Bingoo Huang
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LocalInputMap {

}
