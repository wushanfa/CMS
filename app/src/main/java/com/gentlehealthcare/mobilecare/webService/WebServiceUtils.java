package com.gentlehealthcare.mobilecare.webService;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.HashMap;
import java.util.Map;

/**
 *  访问WebService的工具类
 */
public class WebServiceUtils {

    private static final String WEB_SERVER_URL = "http://10.0.0.215:8089/VitalSignsService.asmx";  //张智成
//  private static final String WEB_SERVER_URL = "http://10.0.0.115:8787/VitalSignsService.asmx";  //武杉

    // 命名空间
    private static final String NAMESPACE = "http://tempuri.org/";

    public static String callWebService(HashMap<String,String> properties) {
        String nameSpace = NAMESPACE;
        String serviceURL = WEB_SERVER_URL;
        String methodName = "CPMessageServer";
        final String soapAction = "http://tempuri.org/CPMessageServer";
        final HttpTransportSE ht = new HttpTransportSE(serviceURL);
        ht.debug = false;
        SoapObject request = new SoapObject(nameSpace, methodName);
        if (properties != null) {
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                request.addProperty(entry.getKey(), entry.getValue());
            }
        }
        final SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.bodyOut = request;
        envelope.setOutputSoapObject(request);
        try {
            ht.call(soapAction, envelope);
            return envelope.getResponse().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
