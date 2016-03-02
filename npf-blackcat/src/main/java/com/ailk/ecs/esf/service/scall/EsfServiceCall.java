package com.ailk.ecs.esf.service.scall;

import java.util.HashMap;
import java.util.Map;

import com.ailk.ecs.esf.base.exception.BaseException;
import com.ailk.ecs.esf.base.exception.EsfException;
import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.blackcat.BlackcatUtils;
import com.google.common.base.Throwables;

import static com.alibaba.fastjson.JSON.toJSON;
import static com.alibaba.fastjson.JSON.toJSONString;
import static com.google.common.base.Throwables.getStackTraceAsString;

/**
 * 逻辑层服务调用API
 * @author Wangt
 *
 */
@SuppressWarnings("unchecked")
public class EsfServiceCall {

    private static ThreadLocal result = new ThreadLocal();

    private String module = null;

    public EsfServiceCall(String module) {
        this.module = module;
    }

    public static final String MAP_PARAM_TYPE = "MAP_PARAM_TYPE";
    public static final String OBJ_PARAM_TYPE = "OBJ_PARAM_TYPE";

    public Map call(String target, Map dataMap) {
        if (dataMap == null) {
            dataMap = new HashMap(1);
        }
        return (Map) angentProcess(target, MAP_PARAM_TYPE, new HashMap(), dataMap);
    }

    public Object call2(String target, Object... obj) {
        return angentProcess(target, OBJ_PARAM_TYPE, new HashMap(), obj);
    }

    public static void expectLastCallReturn(Object obj) {
        result.set(obj);
    }

    protected Object angentProcess(String target, String paramType, Map esfMap, Object... obj) {
        ServiceAccessible srvAngent = ServiceUtils.getServiceDelegate(module);

        if (srvAngent == null) {
            return result.get();
        }

        try {
            BlackcatUtils.log("HESSIANCALL.START", "target:{}, params:{}, obj: {}",
                    target, toJSONString(esfMap), toJSONString(obj));

            Object callResult;
            if (MAP_PARAM_TYPE.equals(paramType)) {
                callResult = srvAngent.process(target, esfMap,  (Map) obj[0]);
            } else {
                callResult = srvAngent.process2(target, esfMap, obj);
            }
            BlackcatUtils.log("HESSIANCALL.RESULT", toJSONString(callResult));
            return callResult;
        }
        catch (RuntimeException e) {
            BlackcatUtils.log("HESSIANCALL.ERROR", getStackTraceAsString(e));
            throw e;
        }
        catch (Exception e) {
            BlackcatUtils.log("HESSIANCALL.ERROR", getStackTraceAsString(e));
            Throwable cause = e;
            while (cause != null) {
                if (cause instanceof BaseException) {
                    throw (BaseException) cause;
                }
                cause = cause.getCause();
            }
            throw new EsfException("Service调用失败:" + target, e);
        }
    }
}
