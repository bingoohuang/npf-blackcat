package org.phw.eop.domain;

import com.github.bingoohuang.blackcat.javaagent.callback.Blackcat;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EopSystemParam {
    private EopAppBean eopApp;
    private EopActionBean eopAction;
    private EopLogBean eopLog;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Map map;

    public EopAppBean getEopApp() {
        return this.eopApp;
    }

    public EopActionBean getEopAction() {
        return this.eopAction;
    }

    public EopLogBean getEopLog() {
        return this.eopLog;
    }

    public void setEopApp(EopAppBean eopApp) {
        this.eopApp = eopApp;
    }

    public void setEopAction(EopActionBean eopAction) {
        this.eopAction = eopAction;
    }

    public void setEopLog(EopLogBean eopLog) {
        this.eopLog = eopLog;
    }

    public HttpServletRequest getRequest() {
        return this.request;
    }

    public HttpServletResponse getResponse() {
        return this.response;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
        Blackcat.reset(request);
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

}
