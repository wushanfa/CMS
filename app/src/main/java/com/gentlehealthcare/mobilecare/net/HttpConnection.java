package com.gentlehealthcare.mobilecare.net;

import android.content.Context;
import android.util.Log;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * @author cally
 * @class HttpConnection
 * @date 2012-11-14 下午3:43:39
 * @description 网络访问工具类
 */
public class HttpConnection {
    private static final String TAG = "HttpConnection";
    private static HttpURLConnection conn;
    private String resposeContent;

    /**
     * @param url 网络路径
     * @return
     * @description 初始化网络通信
     * @date 2012-11-14 下午3:17:15
     */
    private HttpURLConnection initConnection(String url) throws Exception {
        Log.d(TAG, "url->" + url);
        HttpURLConnection conn = (HttpURLConnection) new URL(url)
                .openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Charset", "UTF-8");
        // 设置一般的请求属性
        conn.setRequestProperty("Content-Type", "text/json;charset=utf-8");
        // 请求超时
        if (JsonFileUtil.isLocal()) {
            conn.setConnectTimeout(100);
            conn.setReadTimeout(100);
        } else {
            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(10 * 1000);
        }
        conn.setUseCaches(false);
        conn.setInstanceFollowRedirects(true);
        conn.setRequestProperty("Connection", "close");
        conn.setDoInput(true);
        conn.setDoOutput(true);
        return conn;
    }

    /**
     * @param url    网络路径
     * @return
     * @description 网络通信（通过json形式） 未判断网络状态
     * @date 2012-11-14 下午3:23:45
     */
    public Object connection(String url, Object obj) throws Exception {
        Log.d(TAG, "");
        HttpURLConnection conn = initConnection(url + obj.toString());

        // OutputStream os = conn.getOutputStream();
        // if (os != null) {
        try {
            conn.connect();
            HttpConnection.conn = conn;
            JsonUtil jsonUtil = new JsonUtil();
            // os.write(obj.toString().getBytes());
            // os.flush();
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                Object o = null;
                try {
                    resposeContent = getResposeContent(is);
                    o = jsonUtil.doFromJson(resposeContent, obj.getClass());
                } catch (RuntimeException e) {
                    throw new IJsonExeption("Json 解析错误");
                }
                // Object o=jsonUtil.formJson(is, obj.getClass());
                conn.disconnect();
                return o;
            }
        } catch (EOFException e) {
            e.printStackTrace();
            conn.disconnect();
            throw e;
            // connection(url, obj);
        }
        // }
        conn.disconnect();
        throw new SocketTimeoutException();

    }

    public String getResposeContent(InputStream is) {
        try {
            int len = -1;
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            while ((len = is.read(buffer)) != -1) {
                bao.write(buffer, 0, len);
            }
            return new String(bao.toByteArray(), "utf-8");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    /**
     * @param url    网络路径
     * @return
     * @description 网络通信（通过xml形式） 判断网络状态
     * @date 2012-11-14 下午3:23:45
     */
    public synchronized Object connection(String url, Object obj,
                                          Context context) {
        long c = System.currentTimeMillis();
        if (LinstenNetState.isLinkState(context) && !JsonFileUtil.isLocal()) {
            // if (User.getUserId() != null && !"".equals(User.getUserId())) {
            // url += "?tmId=" + User.getUserId();
            // }
            try {
                Object object = connection(url, obj);
                long d = System.currentTimeMillis();
                long result = d - c;
                System.out.println(result);
                // conn.disconnect();
                return object;
            } catch (ConnectTimeoutException e) {
                // ToastShow.showMessage("请求连接服务器超时");
                if (conn != null) {
                    conn.disconnect();
                }
                e.printStackTrace();
                throw new RuntimeException("请求连接服务器超时");
            } catch (SocketException e) {
                // ToastShow.showMessage("连接服务器失败", 3 * 1000);
                if (conn != null) {
                    conn.disconnect();
                }
                e.printStackTrace();
                throw new RuntimeException("连接服务器失败");
            } catch (SocketTimeoutException e) {
                // ToastShow.showMessage("服务器响应时间超时，请联系客服！", 3 * 1000);

                if (conn != null) {
                    conn.disconnect();
                }
                e.printStackTrace();
                throw new RuntimeException("服务器响应时间超时");
            } catch (RuntimeException e) {
                if (conn != null) {
                    conn.disconnect();
                }
                e.printStackTrace();
                throw new RuntimeException("RuntimeException");
            } catch (Exception e) {
                if (conn != null) {
                    conn.disconnect();
                }
                e.printStackTrace();
                throw new RuntimeException("其他 Exception");
            }
        } else {
            // ToastShow.showMessage(
            // context.getResources().getString(
            // R.string.netstate_content), 3 * 1000);
            // return null;
            throw new NullNetworkException("无网络");
        }

    }

    public static void main(String[] args) throws Exception {
        new HttpConnection().connection(
                "http://192.168.0.253:8080/demo/city/query?id=11", null);
    }

    /**
     * 断开连接
     *
     * @description
     * @date 2012-11-28 下午1:00:18
     */
    public void disconnection() {
        if (conn != null) {
            conn.disconnect();
        }
    }

    public String getResposeContent() {
        return resposeContent;
    }

    public void setResposeContent(String resposeContent) {
        this.resposeContent = resposeContent;
    }

    private class IJsonExeption extends Exception {

        public IJsonExeption(String string) {
            // TODO Auto-generated constructor stub
        }

    }

}
