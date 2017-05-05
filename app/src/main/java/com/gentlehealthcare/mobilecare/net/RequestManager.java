package com.gentlehealthcare.mobilecare.net;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;

/**
 *
 * @class RequestManager
 * @author KerWin
 * @date 2012-10-17 上午10:22:22
 * @description 请求消息管理器
 *
 */
public class RequestManager extends Thread {

    private Handler handler;

    /**
     * 单例的请求处理器
     */
    private static RequestManager manager;
    /**
     * 正在处理的请求
     */
    private ABRequest<?> request;
    static Object obj;
    private List<ABRequest<?>> list = new ArrayList<ABRequest<?>>();

    static {
        manager = new RequestManager();
        obj = new Object();
        manager.setDaemon(true);
        manager.start();
    }

    private RequestManager() {
    }

    /**
     * 发送一个请求到请求管理器，请求管理器将会做真正的请求调用处理
     *
     * @param request
     *            请求
     */
    public static void connection(ABRequest<?> request) {
        manager.add(request);
    }

    /**
     * 添加一个请求到队列
     *
     * @param request
     */
    private void add(ABRequest<?> request) {
        synchronized (obj) {
            list.add(request);
        }
    }

    /**
     *
     * @description 移除或取消一个请求.如果该请求存在于队列中，则将其移除并返回true，
     *              <p>
     *              如果请求已经在处理中，则cancel该请求,cancel成功则返回true，cancel失败则返回false
     * @author Administrator
     * @date 2012-10-17 上午10:26:29
     *
     * @param request
     * @return
     */
    private boolean remove(ABRequest<?> request) {
        synchronized (obj) {
            if (list.contains(request)) {
                request.doCancelRequest();
                return list.remove(request);
            } else {
                request.doCancelRequest();
            }
            return false;
        }
    }

    /**
     *
     * @description 取消某个请求，如果该请求存在于队列中，则将其移除并返回true，
     *              <p>
     *              如果请求已经在处理中，则cancel该请求,cancel成功则返回true，cancel失败则返回false
     * @author Administrator
     * @date 2012-10-17 上午10:23:00
     *
     * @param request
     * @return
     */
    public static boolean cancelConnection(ABRequest<?> request) {
        return manager.remove(request);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1*1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (list.size() > 0) {
                synchronized (obj) {
                    request = list.get(0);
//					if (!LinstenNetState.isLinkState(request.getContext())) {
//						request.getHandler().sendEmptyMessage(ABRequest.DO_INTENT_STATE);
//						continue;
//					}
                }

                if (request != null) {
                    // request.doRequest();
                    new Thread(request).start();
                }
                list.remove(0);
            }
        }
    }
}
