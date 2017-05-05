package com.gentlehealthcare.mobilecare.net;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.activity.login.LoginAct;
import com.gentlehealthcare.mobilecare.tool.LocalSave;

/**
 *
 * 消息请求
 *
 * @author KerWin
 *
 * @param <T>
 *            返回的对象类型
 */
public abstract class ABRequest<T> implements IRequest, Runnable {

    protected static String TAG = "";
    /**
     * 消息类型：异常
     */
    private static final int DO_EXCEPTION = 1111;
    /**
     * 消息类型：正常处理
     */
    private static final int DO_RESPOSE = 1110;

    /**
     * 消息类型:无网络
     */
    public static final int DO_INTENT_STATE = 1112;
    /**
     * 请求的返回结果
     */
    private T result;
    /**
     * 结果处理实现类
     */
    private IRespose<T> respose;
    /**
     * 请求返回的源代码
     */
    private String sourceCode;
    /**
     *
     */
    private Handler handler;
    /**
     * 请求的标识ID
     */
    private int id;
    /**
     * 是否是在主线程中处理结果
     */
    private Boolean isInMainThread;
    /**
     * 取消标志，如果cancel后请求继续，但不再对结果进行处理
     */
    private Boolean cancel = false;

    /**
     * 该请求的异常
     */
    private Exception e;
    /**
     * 上下文，用来获取主线程的Looper
     */
    private Context context;

    public static boolean DEBUG = true;
    private String state = "";

    /**
     *
     * @param context
     *            上下文，如果是在子线程中处理结果，此处可传null
     * @param respose
     *            请求结果处理实现
     * @param id
     *            请求的标识，需自己设置
     * @param isInMainThread
     *            是否在主线程处理请求的结果
     */
    public ABRequest(Context context, IRespose<T> respose, int id,
                     Boolean isInMainThread) {
        this.context=context;
        onCreateHandler(context);
        TAG = getClass().getSimpleName();
        this.setRespose(respose);
        this.setContext(context);
        this.id = id;
        this.isInMainThread = isInMainThread;

    }




    /**
     * @description
     * @author KerWin
     * @date 2012-12-4 下午11:06:06
     *
     */
    @Override
    public void run() {
        doRequest();
    }

    /**
     *
     * @description 初始化Handler 如果context为null则handler不做初始化操作，handler为单例
     * @author Administrator
     * @date 2012-10-17 上午10:47:41
     *
     * @param context
     */
    private void onCreateHandler(final Context context) {
        if (context != null && handler == null) {
            if (DEBUG) {
                Log.d("ABRequest", "前台中转Handler已初始化，上下文对象类型："
                        + context.getClass().getName());
            }
            handler = new Handler(context.getMainLooper()) {


                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case DO_EXCEPTION:
                            Map<String,String> map= LocalSave.getInstance(context).get();
                            if (map.get("version")!=null&&map.get("version").equals("local")){
                                sourceCode =JsonFileUtil.getJsonFileContent(TAG+".json");
                                if (sourceCode!=null&&!"".equals(sourceCode)) {
                                    ((ABRequest<?>) msg.obj).doRespose();
                                    break;
                                }
                        }

                            if (("".equals(sourceCode)||sourceCode==null)&&result==null)
                            ((ABRequest<?>) msg.obj).doException();
                            break;
                        case DO_RESPOSE:
                            JsonFileUtil.createJsonFile(TAG+".json",sourceCode);
                            ((ABRequest<?>) msg.obj).doRespose();
                            break;
                        case DO_INTENT_STATE:

                            Toast.makeText(getContext(), getContext().getResources().getString(
                                    R.string.netstate_content), Toast.LENGTH_SHORT).show();
                    }
                };
            };
        }
    }

    /**
     * 请求的处理过程管理方法，不需要继承覆盖此方法。
     * <p>
     * 如果需要在主线程处理请求结果
     */
    public final void doRequest() {
        if (DEBUG) {
            Log.d(TAG, "开始处理请求,时间[" + new Date() + "].");
            state = "is do Request...";
        }
        try {
            connection();
            sourceCode=conn();
            if (("".equals(sourceCode)||sourceCode==null)&&result==null)
            {
                throw  new RuntimeException("后台数据返回NULL或者\"\"");
            }
                disPatchResult();
        } catch (Exception e) {
            try {
                sourceCode=conn();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            if (("".equals(sourceCode)||sourceCode==null)&&result==null) {
                e.printStackTrace();
                this.e = e;
                sendMessage(DO_EXCEPTION);
            }else{
                sendMessage(DO_RESPOSE);
            }
        }

    };

    /**
     *
     * @description 分发 处理结果
     * @author Administrator
     * @date 2012-10-17 上午11:17:54
     *
     * @throws Exception
     */
    private void disPatchResult() throws Exception {

        if (isInMainThread) {
            if (handler == null) {
                throw new Exception("意图在无Context前提下处理前台事务");
            }
            if (!cancel) {
                sendMessage(DO_RESPOSE);
            }
        } else {
            doRespose();
        }
    }

    /**
     *
     * @description 发送消息到主线程处理异常或结果
     * @author Administrator
     * @date 2012-10-17 上午11:04:21
     *
     * @param what
     *            异常或结果。
     * @see #DO_RESPOSE
     * @see #DO_EXCEPTION
     */
    private void sendMessage(int what) {
        if(handler==null)
            return;
        Message msg = handler.obtainMessage();
        msg.what = what;
        msg.obj = ABRequest.this;
        handler.sendMessage(msg);
    }

    /**
     *
     * @description 取消该请求
     * @author Administrator
     * @date 2012-10-17 上午11:05:49
     *
     */
    public synchronized void doCancelRequest() {
        cancel = true;
        if (DEBUG) {
            Log.d(TAG, "已取消请求,时间[" + new Date() + "]. 当前请求状态：" + state);
            state = "is do Cancel...";
        }
    }

    /**
     *
     * @description 访问异常,如果请求没有被取消并且有请求结果的处理者，则将异常传给处理者处理
     * @author Administrator
     * @date 2012-10-17 上午10:57:17
     *
     */
    private void doException() {

        if (DEBUG) {
            Log.d(TAG, "发生了异常,时间[" + new Date() + "]. 当前请求状态：" + state);
            state = "is do Exception...";
            e.printStackTrace();
            try {
                File file = new File(Environment.getExternalStorageDirectory()
                        + "cupmobile_log.txt");
                if (!file.exists()) {
                    file.createNewFile();
                    PrintStream ps = new PrintStream(file);
                    e.printStackTrace(ps);
                    ps.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if (getRespose() != null && !cancel) {
            getRespose().doException(e,!(e instanceof NullNetworkException));
        }
    }

    /**
     *
     * 实际做的工作，如果在处理中的过程中有异常需要在主线程处理或想另行处理，可抛出在
     * {@link IRespose#doException(Exception,boolean)}中处理
     *
     * @return
     * @throws Exception
     */
    protected abstract T connection() throws Exception;


    protected abstract String conn() throws Exception;

    /**
     * 处理结果
     */
    public synchronized final void doRespose() {
        if (DEBUG) {
            Log.d(TAG, "请求结束,发送结果。时间[" + new Date() + "]. 当前请求状态：" + state);
            state = "is do Respose...";
        }
        if (getRespose() != null && !cancel) {
            if (result!=null)
                getRespose().doResult(result, id);
            else {
                try {
                    getRespose().doResult(sourceCode);
                }catch (KeyObsoleteException e){
                    e.printStackTrace();
                    //密钥过期
                    Toast.makeText(getContext(),"密钥过期,需重新登录",Toast.LENGTH_SHORT).show();
                    ((Activity) getContext()).finish();
                    getContext().startActivity(new Intent(getContext(),LoginAct.class));
                }

            }
        }
    }

    public IRespose<T> getRespose() {
        return respose;
    }

    public void setRespose(IRespose<T> respose) {
        this.respose = respose;
    }

    public Handler getHandler() {
        return handler;
    }

    public int getId() {
        return id;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
