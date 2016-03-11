package com.github.bingoohuang.blackcat.javaagent.callback;

import com.github.bingoohuang.blackcat.BlackcatContext;
import com.github.bingoohuang.blackcat.BlackcatUtils;
import com.github.bingoohuang.blackcat.utils.Unsensitive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.helpers.MessageFormatter;

import javax.servlet.http.HttpServletRequest;
import java.net.HttpURLConnection;
import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class Blackcat {
    public static final String BLACKCAT_TRACE_ID = "blackcat_trace_id";
    public static final String BLACKCAT_LINK_ID = "blackcat_link_id";
    static Logger log = LoggerFactory.getLogger(Blackcat.class);

    static ThreadLocal<BlackcatContext> threadLocal = new InheritableThreadLocal<BlackcatContext>();

    public static void reset(
            String blackcatTraceId, String parentLinkId,
            String msgType, String msg) {
        BlackcatContext blackcatContext = new BlackcatContext();
        blackcatContext.setTraceId(blackcatTraceId);
        blackcatContext.setParentLinkId(parentLinkId);

        MDC.put(BLACKCAT_TRACE_ID, blackcatTraceId);
        threadLocal.set(blackcatContext);

        log(blackcatContext.getSubLinkId(), msgType, msg);
    }

    public static void reset(HttpServletRequest req) {
        String traceId = req.getHeader(BLACKCAT_TRACE_ID);
        if (isEmpty(traceId)) traceId = UUID.randomUUID().toString();

        String linkId = req.getHeader(BLACKCAT_LINK_ID);
        if (isEmpty(linkId)) linkId = "0";

        Blackcat.reset(traceId, linkId, "URL", req.getMethod() + ":" + getURL(req));
    }

    public static void prepareRPC(HttpURLConnection httpURLConn) {
        BlackcatContext context = threadLocal.get();
        if (context == null) return;

        log("RPC",  httpURLConn.getRequestMethod() + ":" + httpURLConn.getURL());

        httpURLConn.addRequestProperty(BLACKCAT_TRACE_ID, context.getTraceId());
        String linkId = context.getParentLinkId() + "." + context.getSubLinkId();
        httpURLConn.addRequestProperty(BLACKCAT_LINK_ID, linkId);
    }


    public static void log(String pattern, Object... args) {
        log("LOG", pattern, args);
    }

    public static void log(String msgType, String pattern, Object... args) {
        BlackcatContext blackcatContext = threadLocal.get();
        if (blackcatContext == null) return;

        String msg = MessageFormatter.arrayFormat(pattern, args).getMessage();
        log(blackcatContext.incrAndGetSubLinkId(), msgType, msg);
    }

    public static void log(int subLinkId, String msgType, String msg) {
        BlackcatContext blackcatContext = threadLocal.get();
        if (blackcatContext == null) return;

        String parentLinkId = blackcatContext.getParentLinkId();

        String traceId = blackcatContext.getTraceId();
        String linkId = parentLinkId + "." + subLinkId;

        String unsensitiveMsg = Unsensitive.unsensitive(msg);

        String singleLine = BlackcatUtils.oneLine(unsensitiveMsg);
        log.info("Blackcat:{}##{}##{}##{}", traceId, linkId, msgType, singleLine);
    }

    public static String getURL(HttpServletRequest req) {
        String scheme = req.getScheme();             // http
        String serverName = req.getServerName();     // hostname.com
        int serverPort = req.getServerPort();        // 80
        String contextPath = req.getContextPath();   // /mywebapp
        String servletPath = req.getServletPath();   // /servlet/MyServlet
        String pathInfo = req.getPathInfo();         // /a/b;c=123
        String queryString = req.getQueryString();   // d=789

        // Reconstruct original requesting URL
        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);

        if (serverPort != 80 && serverPort != 443) {
            url.append(":").append(serverPort);
        }

        url.append(contextPath).append(servletPath);

        if (pathInfo != null) url.append(pathInfo);
        if (queryString != null) url.append("?").append(queryString);
        return url.toString();
    }

    public static String currentTraceId() {
        BlackcatContext context = threadLocal.get();
        if (context == null) return null;

        return context.getTraceId();
    }
}
