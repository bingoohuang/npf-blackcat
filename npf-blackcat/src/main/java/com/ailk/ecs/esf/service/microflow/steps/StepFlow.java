package com.ailk.ecs.esf.service.microflow.steps;


import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.github.bingoohuang.blackcat.BlackcatUtils;
import com.google.common.base.Throwables;
import org.apache.log4j.Logger;

import com.ailk.ecs.esf.base.exception.EsfException;
import com.ailk.ecs.esf.base.utils.CglibUtils;
import com.ailk.ecs.esf.base.utils.Tuple;
import com.ailk.ecs.esf.service.microflow.annotations.TransactionGuard;
import com.ailk.ecs.esf.service.microflow.interceptor.StepMethodInterceptor;
import com.ailk.ecs.esf.service.microflow.wiring.AutoWiringUtils;
import com.ailk.ecs.esf.service.microflow.wiring.DaoSqlAutoWiring;
import com.ailk.ecs.esf.service.microflow.wiring.DaoSqlMapAutoWiring;
import com.ailk.ecs.esf.service.microflow.wiring.EfaceFieldAutoWiring;
import com.ailk.ecs.esf.service.microflow.wiring.EsfContextFieldAutoWiring;
import com.ailk.ecs.esf.service.microflow.wiring.InputMapFieldAutoWiring;

class StepFlowRun {
    /*
     * 当前步骤信息.
     */
    private Tuple<String, Class<? extends IStep>, String> currStep = null;
    /*
     * 步骤信息列表.
     */
    private final List<Tuple<String, Class<? extends IStep>, String>> steps;
    /*
     * 步骤跳转信息列表.
     */
    private final List<Tuple<String, StepReturn, String>> transitions;
    /*
     * 当前步骤索引.
     */
    private int stepIndex = 0;

    /*
     * 步骤流定义.
     */
    private final StepFlow stepFlow;

    /*
     * 步骤参数。
     */
    private Object param;

    /**
     * 构造函数。
     *
     * @param stepFlow
     * @param param
     */
    StepFlowRun(StepFlow stepFlow, Object param) {
        this.stepFlow = stepFlow;
        this.param = param;
        this.steps = stepFlow.steps;
        this.transitions = stepFlow.transitions;
    }

    /**
     * 运行所有步骤.
     *
     * @throws Exception
     */
    Object run() throws Exception {
        BlackcatUtils.log("STEPFLOW.START", "total {} steps", steps.size());

        currStep = steps.get(0);

        while (currStep != null) {
            runStep();
        }

        BlackcatUtils.log("STEPFLOW.END", JSON.toJSONString(param));

        return param;
    }

    /**
     * 为步骤生成动态代理.
     *
     * @param stepClass
     * @return
     */
    private static IStep newProxyInstance(Class<? extends IStep> stepClass) {

        if (!Modifier.isPublic(stepClass.getModifiers())) {
            throw new EsfException(stepClass.getName() + " is not public");
        }

        IStep step;
        // 当步骤类有非私有方法定义了事务保护标注，则使用步骤方法拦截器生成动态代理.
        if (AutoWiringUtils.hasAnyOfMethodAnnotation(stepClass, TransactionGuard.class)) {
            step = StepMethodInterceptor.newProxyInstance(stepClass);
        }
        else {
            // 使用默认代理
            step = CglibUtils.newProxyInstance(stepClass);
        }

        return step;
    }

    /*
     * 运行当前步骤.
     */
    private void runStep() throws Exception {
        // 检查下一步骤编号是否合法.
        checkNextStepNo();

        // 生成当前步骤的代理.
        IStep step = newProxyInstance(currStep.getSecond());

        // 为当前步骤注入资源.
        stepFlow.wiringStep(step);

        StepReturn stepRet;

        try {
            StepFlow.logger.debug("Start Step Run:" + step.getClass().getName());

            BlackcatUtils.log("STEP.START", "{} with params {}",
                    step.getClass().getName(), JSON.toJSONString(param));

            // 执行前处理
            step.beforeExec(param);

            // 执行步骤.
            stepRet = step.exec(param);

            // 执行后处理
            step.afterExec(null);
        }
        catch (Exception e) {
            BlackcatUtils.log("STEP.ERROR", Throwables.getStackTraceAsString(e));

            // 执行后异常处理
            step.afterExec(e);

            // 步骤异常处理.
            stepRet = step.exception(e);
        }
        finally {
            StepFlow.logger.debug("End Step Run:" + step.getClass().getName());

            // 设置步骤输出参数.
            this.param = step.getParam();
            step.setParam(null);
        }

        // 计算下一个步骤.
        computeNextStep(stepRet);
    }

    /*
     * 检查下一个步骤编号是否合法.
     */
    private void checkNextStepNo() {
        for (int i = 0; i < steps.size(); ++i) {
            Tuple<String, Class<? extends IStep>, String> step = steps.get(i);
            if (step.getThird().equals(StepFlow.TERMINATE)
                    || step.getThird().equals(StepFlow.CONTINUE)) {
                continue;
            }

            int nextStepNum = StepFlow.findStep(steps, step.getThird());
            if (nextStepNum < 0) {
                throw new EsfException("步骤" + step.toString() + "的下一步编码不存在:" + step.getThird());
            }
        }
    }

    /**
     * 根据当前步骤运行结果计算下一步骤.
     *
     * @param stepRet
     *            当前步骤返回.
     */
    private void computeNextStep(StepReturn stepRet) {
        Tuple<String, StepReturn, String> transition = null;
        for (Tuple<String, StepReturn, String> tuple : transitions) {
            if (tuple.getFirst().equals(currStep.getFirst()) && tuple.getSecond().equals(stepRet)) {
                transition = tuple;
                break;
            }
        }

        // 根据配置的Transition跳转
        if (transition != null) {
            // -1表示结束当前流程处理
            if (transition.getThird().equals(StepFlow.TERMINATE)) {
                stepIndex = -1;
            }
            else if (transition.getThird().equals(StepFlow.CONTINUE)) {
                ++stepIndex;
            }
            else {
                stepIndex = StepFlow.findStep(steps, transition.getThird());
            }
        }

        // 根据Steps配置的跳转
        else {
            if (currStep.getThird().equals(StepFlow.CONTINUE)) {
                ++stepIndex;
            }
            else if (currStep.getThird().equals(StepFlow.TERMINATE)) {
                stepIndex = -1;
            }
            else {
                stepIndex = StepFlow.findStep(steps, currStep.getThird());
            }
        }

        currStep = stepIndex >= 0 && stepIndex < steps.size() ? steps.get(stepIndex) : null;
    }
}

/**
 * 步骤流程类.
 *
 * @author Bingoo Huang
 *
 */
public class StepFlow {
    static Logger logger = Logger.getLogger(StepFlow.class);
    /**
     * 步骤跳转终结.
     */
    public final static String TERMINATE = "TERMINATE";
    /**
     * ' 步骤跳转到下一步.
     */
    public final static String CONTINUE = "CONTINUE";

    /*
     * 步骤信息列表.
     */
    List<Tuple<String, Class<? extends IStep>, String>> steps;
    /*
     * 步骤跳转信息列表.
     */
    List<Tuple<String, StepReturn, String>> transitions;

    /**
     * 构造函数.
     */
    public StepFlow() {
        steps = new ArrayList<Tuple<String, Class<? extends IStep>, String>>();
        transitions = new ArrayList<Tuple<String, StepReturn, String>>();
    }

    /**
     * 运行所有步骤.
     *
     * @throws Exception
     */
    public Object run(Object param) throws Exception {
        return new StepFlowRun(this, param).run();
    }

    /*
     * 插件步骤编号是否合法.<br> 步骤编号不能重复.<br>
     */
    private void checkStepNo(String stepStr, String stepId, String nextStepId) {
        if (findStep(steps, stepId) >= 0) {
            throw new EsfException("[Steps]的(" + stepStr + ")的" + "步骤编号不能重复");
        }
    }

    /*
     * 检查跳转步骤编号.<br> 步骤编号必须存在.<br> 跳转步骤编号必须存在.<br>
     */
    private void checkTransitionStepNo(String transition, String stepId, String toStepId) {
        String errerMsg = "[Transitions]的(" + transition + ")的";
        if (findStep(steps, stepId) < 0) {
            throw new EsfException(errerMsg + "步骤编号不存在" + stepId);
        }
        if (!TERMINATE.equals(toStepId) && !CONTINUE.equals(toStepId)
                && findStep(steps, toStepId) < 0) {
            throw new EsfException(errerMsg + "跳转的步骤编码不存在:" + toStepId);
        }
    }

    /**
     * 添加步骤.
     *
     * @param stepId
     *            步骤编号.
     * @param stepClass
     *            步骤类.
     */
    public void addStep(String stepId, Class<? extends IStep> stepClass) {
        addStep("", stepId, stepClass, CONTINUE);
    }

    /**
     * 添加步骤.
     *
     * @param stepId
     *            步骤编号.
     * @param stepClass
     *            步骤类.
     * @param nextStepId
     *            下一个步骤编号.
     */
    public void addStep(String stepStr, String stepId, Class<? extends IStep> stepClass,
                        String nextStepId) {
        checkStepNo(stepStr, stepId, nextStepId);
        steps.add(new Tuple<String, Class<? extends IStep>, String>(stepId, stepClass, nextStepId));
    }

    /**
     * 添加步骤之间的跳转.
     *
     * @param stepId
     *            步骤编号.
     * @param stepRet
     *            步骤返回.
     * @param toStepId
     *            跳转步骤编号.
     */
    public void addTransition(String transition, String stepId, StepReturn stepRet, String toStepId) {
        checkTransitionStepNo(transition, stepId, toStepId);

        transitions.add(new Tuple<String, StepReturn, String>(stepId, stepRet, toStepId));
    }

    /**
     * 为步骤注入资源.<br>
     * 目前注入Dao资源，EjfContext资源，inputMap资源.
     *
     * @param step
     *            步骤实例.
     * @throws Exception
     */
    public void wiringStep(IStep step) throws Exception {
        AutoWiringUtils.wire(step, new InputMapFieldAutoWiring(), new DaoSqlAutoWiring(),
                new EfaceFieldAutoWiring(), new DaoSqlMapAutoWiring(),
                new EsfContextFieldAutoWiring());
    }

    /**
     * 根据步骤编号查找步骤.
     *
     * @param stepIdx
     * @return -1 未找到, 其他，找到的步骤编号.
     */
    public static int findStep(List<Tuple<String, Class<? extends IStep>, String>> steps,
                               String stepIdx) {
        for (int i = 0; i < steps.size(); ++i) {
            if (steps.get(i).getFirst().equals(stepIdx)) {
                return i;
            }
        }

        return -1;
    }
}