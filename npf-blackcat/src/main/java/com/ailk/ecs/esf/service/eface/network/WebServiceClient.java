package com.ailk.ecs.esf.service.eface.network;

import com.ailk.ecs.esf.service.eface.base.EfaceException;
import com.github.bingoohuang.blackcat.BlackcatUtils;
import com.github.bingoohuang.blackcat.utils.XmlMinifier;
import com.google.common.base.Throwables;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.utils.JavaUtils;

import javax.xml.namespace.QName;

import static org.apache.commons.lang3.StringUtils.indexOf;

/**
 * WebService Client.
 */
public abstract class WebServiceClient {
    /**
     * callService.
     *
     * @param endPoint      String
     * @param operationName String
     * @param namespace     String
     * @param sendMsg       String
     * @param ifaceTiemout  int
     * @return 响应报文
     */
    public static String callService(
            String endPoint, String operationName, String namespace,
            String sendMsg, int ifaceTiemout) {
        BlackcatUtils.log("EFACE.REQ",
                "endPoint:{}, operationName:{}, qname:{}, reqMsg:{}, timeout:{}",
                endPoint, operationName, namespace, XmlMinifier.minify(sendMsg), ifaceTiemout);
        try {
            Service ws = new Service();
            Call call = (Call) ws.createCall();

            call.setTargetEndpointAddress(new java.net.URL(endPoint));
            call.setOperationName(new QName(namespace, operationName));
            call.setTimeout(ifaceTiemout);
            Object rsp = call.invoke(new Object[]{sendMsg});

            String rspMsg = (String) JavaUtils.convert(rsp, String.class);
            BlackcatUtils.log("EFACE.RSP", XmlMinifier.minify(rspMsg));
            return rspMsg;
        } catch (Exception ex) {
            BlackcatUtils.log("EFACE.ERR", Throwables.getStackTraceAsString(ex));
            boolean isTimeout = indexOf(ex.getMessage(), "TimeoutException") >= 0;
            throw new EfaceException(isTimeout ? "接口调用超时" : "接口调用异常", ex);
        }
    }

}
