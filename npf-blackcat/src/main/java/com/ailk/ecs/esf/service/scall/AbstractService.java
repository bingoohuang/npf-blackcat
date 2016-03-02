package com.ailk.ecs.esf.service.scall;

import java.lang.reflect.Method;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.blackcat.javaagent.callback.Blackcat;
import com.google.common.base.Throwables;
import org.apache.commons.lang.StringUtils;

import com.ailk.ecs.esf.base.utils.ClazzUtils;
import com.ailk.ecs.esf.base.utils.LocalUtils;

@SuppressWarnings("unchecked")
public abstract class AbstractService implements ServiceAccessible {

    @Override
    public Map process(String target, Map esfMap, Map mapIn) throws Exception {
        try {
            LocalUtils.setInputMap(mapIn);
            LocalUtils.setEsfMap(esfMap);
            return proxy(target, mapIn);
        } finally {
            LocalUtils.clear();
        }
    }

    @Override
    public Object process2(String target, Map esfMap, Object... obj) throws Exception {
        try {
            Blackcat.log("SERVICE.START", "EsfMap:{}, Params:{}",
                    JSON.toJSONString(esfMap), JSON.toJSONString(obj));

            LocalUtils.setEsfMap(esfMap);
            Object result = proxy2(target, obj);

            Blackcat.log("SERVICE.RESULT", JSON.toJSONString(result));

            return result;
        } catch (Exception ex) {
            Blackcat.log("SERVICE.ERROR", Throwables.getStackTraceAsString(ex));
            throw ex;
        } finally {
            LocalUtils.clear();
        }
    }

    private Map proxy(String target, Map mapIn) throws Exception {
        String className = StringUtils.substringBeforeLast(target, ".");
        String methodName = StringUtils.substringAfterLast(target, ".");
        Class serviceClass = ClazzUtils.loadClass(className);
        Method method = ClazzUtils.getMethod(serviceClass, methodName, Map.class);
        method.setAccessible(true);
        Object result = method.invoke(serviceClass.newInstance(), mapIn);

        return (Map) result;
    }

    private Object proxy2(String target, Object... in) throws Exception {
        String className = StringUtils.substringBeforeLast(target, ".");
        String methodName = StringUtils.substringAfterLast(target, ".");
        Class serviceClass = ClazzUtils.loadClass(className);
        int len = in.length;
        Class[] argTypes = new Class[len];

        for (int i = 0; i < len; i++) {
            argTypes[i] = in[i] != null ? in[i].getClass() : null;
        }

        Method method = ClazzUtils.getExactMethod(serviceClass, methodName, argTypes);
        method.setAccessible(true);
        Object result = method.invoke(serviceClass.newInstance(), in);

        return result;
    }


}