package cn.ai.ebs.npf;

import com.github.bingoohuang.blackcat.BlackcatUtils;
import com.google.common.base.Throwables;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.struts2.StrutsStatics;

import javax.servlet.http.HttpServletRequest;

public class BlackcatStrutsInterceptor extends AbstractInterceptor {
    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        String method = invocation.getProxy().getMethod();
        ActionContext context = ActionContext.getContext();
        Object obj = context.get(StrutsStatics.HTTP_REQUEST);

        BlackcatUtils.reset((HttpServletRequest) obj);
        BlackcatUtils.log("METHOD.START", method);

        try {
            String result = invocation.invoke();
            BlackcatUtils.log("METHOD.RESULT", result);
            return result;
        } catch (Exception e) {
            BlackcatUtils.log("METHOD.ERROR", Throwables.getStackTraceAsString(e));
            throw e;
        }
    }

}
