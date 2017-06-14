package com.gentlehealthcare.mobilecare.activity.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.db.table.TB_LoginRecord;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.JsonFileUtil;
import com.gentlehealthcare.mobilecare.net.KeyObsoleteException;
import com.gentlehealthcare.mobilecare.net.RequestManager;
import com.gentlehealthcare.mobilecare.net.bean.BasicInfoItem;
import com.gentlehealthcare.mobilecare.net.bean.LoginBean;
import com.gentlehealthcare.mobilecare.net.bean.SystemInfoBean;
import com.gentlehealthcare.mobilecare.net.bean.WardInfoItem;
import com.gentlehealthcare.mobilecare.net.impl.SystemInfoRequest;
import com.gentlehealthcare.mobilecare.tool.LocalSave;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.tool.SystemInfoSave;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * App引导界面 TODO
 * 
 */
public class WelcomeAct extends BaseActivity {
	private static final String TAG="WelcomeAct";
	/** 是否第一次进入 **/
	private boolean isFirstIn;
	private handler hd;
	private mythread mt;
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = this.getLayoutInflater().inflate(R.layout.activity_welcome, null);

		setContentView(view);
		initWidget();
		HidnGestWindow(true);
        //添加动画效果
		Animation viewAnimation = AnimationUtils.loadAnimation(this, R.anim.welcome_alpha);
		view.startAnimation(viewAnimation);
		button = (Button) view.findViewById(R.id.btn_start);
		MobclickAgent.setDebugMode(true);
		MobclickAgent.updateOnlineConfig(this);
		// 判断程序是否是第一次启动
		SharedPreferences preferences = getSharedPreferences("NURSE_MOBILE_IS_FIRST", MODE_PRIVATE);
		isFirstIn = preferences.getBoolean("isFirstIn", false);
		hd=new handler();
		mt=new mythread();
		mt.start();
        LocalSave localSave=LocalSave.getInstance(WelcomeAct.this);
        Map<String,String> localmap=new HashMap<String, String>();
        localmap.put("version","net");
        localSave.save(localmap);
        if (JsonFileUtil.isLocal()){
            //如果是本地版本的户，拷贝文件等初始化处理
            JsonFileUtil.copyJsonFile();
            Gson gson=new Gson();
            Type type = new TypeToken<LoginBean>(){}.getType();
            LoginBean t=gson.fromJson(JsonFileUtil.getJsonFileContent("LoginRequest.json"), type);

            TB_LoginRecord table=new TB_LoginRecord();
            table.setUpdateTime(System.currentTimeMillis());
            table.setUserName(t.getUsername());
            BasicInfoItem basicInfoItem=t.getBasicInfo();
            if (basicInfoItem!=null)
            {
                UserInfo.setName(basicInfoItem.getName());
                UserInfo.setDeptName(basicInfoItem.getItemName());
                List<WardInfoItem> list=basicInfoItem.getWardList();
                UserInfo.setWardList(list);
                if(list!=null&&list.size()>0){
                    UserInfo.setWardCode(list.get(0).getWardCode());
                }
                System.err.println(list);
            }
            UserInfo.setUserName(t.getUsername());
            UserInfo.setKey(t.getKey());
        }

        SystemInfoSave save=SystemInfoSave.getInstance(WelcomeAct.this);
        Map<String ,String> map=save.get();
        //如果IP和PORT没有保存的话，初始化IP和PORT
		if (map != null) {
			if (map.get("ip")==null||"".equals(map.get("ip"))||map.get("port")==null||"".equals(map.get("port"))){
				map=new HashMap<String, String>();
				map.put("ip","192.168.120.190");
				map.put("port","8081");
				save.save(map);
			}
		}
        /**
         *系统资料获取
         */
		RequestManager.connection(new SystemInfoRequest(this, new IRespose<SystemInfoBean>() {
			@Override
			public void doResult(SystemInfoBean t, int id) {
				// TODO Auto-generated method stub
				Map<String,String> map=SystemInfoSave.getInstance(WelcomeAct.this).get();
				map.put("appCopyright", t.getAppCopyright());
				map.put("appName", t.getAppName());
				map.put("hospitalName", t.getHospitalName());
				map.put("appCode", t.getAppCode());
				SystemInfoSave.getInstance(WelcomeAct.this).save(map);
				UserInfo.setAttentions(t.getAttentions());
			}

            @Override
            public void doResult(String result) throws KeyObsoleteException{
						Gson gson = new Gson();
						Type type = new TypeToken<SystemInfoBean>() {
						}.getType();
						SystemInfoBean t = gson.fromJson(result, type);
						Map<String, String> map = SystemInfoSave.getInstance(WelcomeAct.this).get();
						map.put("appCopyright", t.getAppCopyright());
						map.put("appName", t.getAppName());
						map.put("hospitalName", t.getHospitalName());
						map.put("appCode", t.getAppCode());
						SystemInfoSave.getInstance(WelcomeAct.this).save(map);
						UserInfo.setAttentions(t.getAttentions());
            }

            @Override
			public void doException(Exception e,boolean networkState) {
				// TODO Auto-generated method stub
				e.printStackTrace();
			}
		}, 0, true));
		
	}

	@Override
	protected void resetLayout() {
		RelativeLayout root = (RelativeLayout) findViewById(R.id.root_welcome);
		SupportDisplay.resetAllChildViewParam(root);
	}

	/**
     * 初始化控件
     */
	private void initWidget(){
		Map<String,String> map=SystemInfoSave.getInstance(WelcomeAct.this).get();
		if(map==null||map.keySet().size()<=0)
			return ;
		//((TextView)findViewById(R.id.tv_hospital_name)).setText(map.get("hospitalName")==null?"河南省洛阳正骨医院":map.get("hospitalName"));
		((TextView)findViewById(R.id.tv_app_name)).setText(map.get("appName")==null?"移动护理系统":map.get("appName"));
	}

    /**
     * 倒计时线程
     */
	class mythread extends Thread{
		int time=2;
		@Override
		public void run() {
			while(time>-1){
				Message msg=new Message();
				msg.what=time;
				hd.sendMessage(msg);
				time--;
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

    /**
     * 上下文刷新倒计时控件
     */
	class handler extends Handler{
		@Override
		public void handleMessage(Message msg) {
				button.setText(msg.what+"");
			if(msg.what==0)
				toturn();
			
		}
	}

    /**
     * 当倒计时为0的时候，跳转界面
     */
	public void toturn() {
			Intent intent;
			if (isFirstIn) {
				// 跳转到登录界面
				intent = new Intent(WelcomeAct.this,LoginAct.class);
			} else {
				// 跳转到版权界面
				intent = new Intent(WelcomeAct.this,CopyRightAct.class);
			}
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
		}
	

}
