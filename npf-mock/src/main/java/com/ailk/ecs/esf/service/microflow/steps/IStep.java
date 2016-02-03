package com.ailk.ecs.esf.service.microflow.steps;

/**
 * 业务框架步骤接口.<br>
 * @author Bingoo Huang
 *
 */
public interface IStep {
    /**
     * 取得参数.
     * @return 参数.
     */
    Object getParam();

    /**
     * 设置参数.
     * @param in 参数.
     */
    void setParam(Object in);

    /**
     * 执行当前步骤.
     * @param in 输入参数.
     * @return 步骤返回枚举.
     * @throws Exception
     */
    StepReturn exec(Object in) throws Exception;

    /**
     * 步骤执行方法异常时响应类.
     * @param ex 异常.
     * @return 步骤返回枚举.
     * @throws Exception
     */
    StepReturn exception(Exception ex) throws Exception;

    /**
     * 步骤执行前处理.
     * @param in 执行参数.
     */
    void beforeExec(Object in);

    /**
     * 步骤执行成功后处理.
     * @param e 执行异常信息.
     */
    void afterExec(Exception e);

}
