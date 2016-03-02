package com.ailk.ecs.esf.service.hessian.ext;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.github.bingoohuang.blackcat.javaagent.callback.Blackcat;
import org.apache.commons.lang.StringUtils;

import com.ailk.ecs.esf.base.exception.BusinessException;
import com.ailk.ecs.esf.base.exception.EsfException;
import com.ailk.ecs.esf.conf.EsfProperties;
import com.caucho.hessian.client.HessianProxy;
import com.caucho.hessian.client.HessianProxyFactory;


public class EsfHessianProxy extends HessianProxy {
    protected EsfHessianProxy(HessianProxyFactory factory, URL url) {
        super(url, factory);
    }

    @Override
    protected void addRequestHeaders(URLConnection conn) {
        super.addRequestHeaders(conn);

        HttpURLConnection httpConn = (HttpURLConnection) conn;
        Blackcat.prepareRPC(httpConn);
    }


    /**
     * Method that allows subclasses to parse response headers such as cookies.
     * Default implementation is empty.
     * @param conn
     */
    @Override
    protected void parseResponseHeaders(URLConnection conn) {
        HttpURLConnection httpConn = (HttpURLConnection) conn;
        int code = 500;
        try {
            code = httpConn.getResponseCode();
        }
        catch (IOException e) {
            throw new EsfException("Get Response Code Error", e);
        }

        String rcStr = EsfProperties.getProperty("RC_CODES", "");
        if (StringUtils.isEmpty(rcStr) && code != 200) {
            throw new BusinessException("RC_" + code, "HTTP Response Code:" + code + "异常");
        }

        String[] rcs = rcStr.split(",");
        for (String rc : rcs) {
            if (rc.equals("" + code)) {
                throw new BusinessException("RC_" + code, "HTTP Response Code:" + code + "异常");
            }
        }
    }
}
