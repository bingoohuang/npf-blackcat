package com.ailk.ecs.esf.service.scall;

import java.util.Map;

@SuppressWarnings("unchecked")
public interface ServiceAccessible {

    Map process(String target, Map esfMap, Map mapIn) throws Exception;

    /**
     * 远程服务统一接口(可以传入对象参数，以及返回对象参数)
     */
    Object process2(String target, Map esfMap, Object... obj) throws Exception;
}
