package com.ailk.ecs.esf.base.utils;



/**
 * CGLIB小函数。
 * @author BingooHuang
 *
 */
@SuppressWarnings("unchecked")
public abstract class CglibUtils {
    /**
     * 是否是一个cglib的动态代理类.
     * 
     * @param cls 类
     * @return true 是CGLIB动态代理类 false 不是
     */
    public static boolean isDynaProxyClass(Class<?> cls) {
        // return AopUtils.isCglibProxyClass(cls);
        return cls.getName().indexOf("$$EnhancerByCGLIB$$") > 0;
    }

    /**
     * 创建一个类的实例或者空代理.
     * @param <T> 类型信息
     * @param superclass      the super class to extend. null if none.
     * @return the proxy object.
     */
    public static <T> T newProxyInstance(Class<T> superclass) {
       return null;
    }


}
