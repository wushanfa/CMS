package com.gentlehealthcare.mobilecare.activity.login;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.gentlehealthcare.mobilecare.R;
import com.gentlehealthcare.mobilecare.UserInfo;
import com.gentlehealthcare.mobilecare.activity.BaseActivity;
import com.gentlehealthcare.mobilecare.activity.CamerService;
import com.gentlehealthcare.mobilecare.activity.home.HomeAct;
import com.gentlehealthcare.mobilecare.bean.sys.BarcodeDict;
import com.gentlehealthcare.mobilecare.config.BloodCodeRule;
import com.gentlehealthcare.mobilecare.constant.GlobalConstant;
import com.gentlehealthcare.mobilecare.constant.UrlConstant;
import com.gentlehealthcare.mobilecare.db.dao.LoginRecordDao;
import com.gentlehealthcare.mobilecare.net.IRespose;
import com.gentlehealthcare.mobilecare.net.JsonFileUtil;
import com.gentlehealthcare.mobilecare.net.KeyObsoleteException;
import com.gentlehealthcare.mobilecare.net.RequestManager;
import com.gentlehealthcare.mobilecare.net.bean.BasicInfoItem;
import com.gentlehealthcare.mobilecare.net.bean.LoginBean;
import com.gentlehealthcare.mobilecare.net.bean.WardInfoItem;
import com.gentlehealthcare.mobilecare.net.impl.LoginRequest;
import com.gentlehealthcare.mobilecare.tool.CCLog;
import com.gentlehealthcare.mobilecare.tool.LocalSave;
import com.gentlehealthcare.mobilecare.tool.StringTool;
import com.gentlehealthcare.mobilecare.tool.SupportDisplay;
import com.gentlehealthcare.mobilecare.tool.SystemInfoSave;
import com.gentlehealthcare.mobilecare.view.AlertDialogOneBtn;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录界面 TODO
 */
public class LoginAct extends BaseActivity implements OnTouchListener {
	private AlertDialogOneBtn dialog;
	private boolean isPositive = true;
	private EditText edtUsername, edtPassword;
	private Button dropdown;
	private PopupWindow popu;
	private ListView usernamerecord;
	private List<Map<String, Object>> usernames;
	private MyAdapter myadapter;
	private Boolean flag = false;
	private int wid;
	private int hei;
	private List<String> listlrs;
	private LoginRecordDao loginRecordDao;
	private ProgressDialog pDialog;
	public BroadcastReceiver receiver = null;
	public ProgressDialog progressDialog = null;

	private void initConfigure() {
		//LocalSave localSave = LocalSave.getInstance(LoginAct.this);
		if (JsonFileUtil.isLocal())
			JsonFileUtil.copyJsonFile();
		if (JsonFileUtil.isLocal()) {
			Gson gson = new Gson();
			Type type = new TypeToken<LoginBean>() {
			}.getType();
			LoginBean t = gson.fromJson(
					JsonFileUtil.getJsonFileContent("LoginRequest.json"), type);

//			TB_LoginRecord table = new TB_LoginRecord();
//			table.setUpdateTime(System.currentTimeMillis());
//			table.setUserName(t.getUsername()); 17-3-20 why:outdated

			BasicInfoItem basicInfoItem = t.getBasicInfo();
			if (basicInfoItem != null) {
				UserInfo.setName(basicInfoItem.getName());
				UserInfo.setDeptName(basicInfoItem.getItemName());
				List<WardInfoItem> list = basicInfoItem.getWardList();
				UserInfo.setWardList(list);
				if (list != null && list.size() > 0) {
					UserInfo.setWardCode(list.get(0).getWardCode());
				}
				System.err.println(list);
			}
			UserInfo.setUserName(t.getUsername());
			UserInfo.setKey(t.getKey());
		}
		SystemInfoSave save = SystemInfoSave.getInstance(LoginAct.this);
		Map<String, String> map = save.get();
		if (map != null) {
			if (map.get("ip") == null || "".equals(map.get("ip"))
					|| map.get("port") == null || "".equals(map.get("port"))) {
				map = new HashMap<String, String>();
				map.put("ip", "192.168.120.190");
				map.put("port", "8081");
				save.save(map);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initConfigure();
		getBarcodeDict();
		edtUsername = (EditText) findViewById(R.id.txt_username);
		edtPassword = (EditText) findViewById(R.id.txt_password);
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("正在登录,请稍后...");
		edtUsername.setOnTouchListener(this);
		edtPassword.setOnTouchListener(this);
		loginRecordDao = LoginRecordDao.newInstance(this);
		edtUsername.setText(loginRecordDao.queryRecordOfUserName());
		edtUsername.setSelection(edtUsername.getText().toString().length());
		edtPassword.setSelection(edtPassword.getText().toString().length());
		edtUsername.setBackgroundResource(R.drawable.edittextshape);
		dropdown = (Button) findViewById(R.id.dropdown_button);
		initPopuwindow();
		(findViewById(R.id.txt_copyright))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						startActivity(new Intent(LoginAct.this,
								CopyRightAct.class));
					}
				});

		findViewById(R.id.txt_update).setOnLongClickListener(
				new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						// downloadApk();
						loadapk();
						return false;
					}
				});
		//用户登录
		(findViewById(R.id.btn_login))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						doLogin();
					}
				});
		(findViewById(R.id.btn_login))
				.setOnLongClickListener(new View.OnLongClickListener() {
					@Override
					public boolean onLongClick(View view) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								LoginAct.this);
						builder.setTitle("配置信息");
						View dialogView = LayoutInflater.from(LoginAct.this)
								.inflate(R.layout.activity_login_peizhi, null);
						final EditText ip = (EditText) dialogView
								.findViewById(R.id.et_peizhi_ip);
						final EditText port = (EditText) dialogView
								.findViewById(R.id.et_peizhi_port);
						ip.setText(UrlConstant.getIp());
						port.setText(UrlConstant.getPort());
						dialogView.setLayoutParams(new ViewGroup.LayoutParams(
								ViewGroup.LayoutParams.MATCH_PARENT,
								ViewGroup.LayoutParams.WRAP_CONTENT));
						builder.setView(dialogView);
						builder.setNegativeButton("保存",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (ip.getText().toString().equals("")) {
											Toast.makeText(LoginAct.this,
													"无法保存，请填写IP",
													Toast.LENGTH_SHORT).show();
										} else if (port.getText().toString()
												.equals("")) {
											Toast.makeText(LoginAct.this,
													"无法保存，请填写Port",
													Toast.LENGTH_SHORT).show();
										} else {
											UrlConstant.setIp(ip.getText()
													.toString());
											UrlConstant.setPort(port.getText()
													.toString());
											Toast.makeText(LoginAct.this,
													"保存成功。", Toast.LENGTH_SHORT)
													.show();
										}
									}
								});
						builder.setPositiveButton("取消", null);
						AlertDialog dialog = builder.create();
						dialog.show();
						WindowManager.LayoutParams params = dialog.getWindow()
								.getAttributes();
						params.width = 600;
						params.height = 600;
						dialog.getWindow().setAttributes(params);
						return false;
					}
				});

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		String[] news = null;
		if (UserInfo.getAttentions() == null)
			news = new String[] {};
		else {
			news = new String[UserInfo.getAttentions().size()];
			for (int i = 0; i < UserInfo.getAttentions().size(); i++) {
				news[i] = UserInfo.getAttentions().get(i).getNoticeContent();
			}
		}

		dropdown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (popu != null) {
					if (popu.isShowing()) {
						popu.dismiss();
					} else {
						popu.showAsDropDown(edtUsername);
					}
				}

			}
		});
	}

	@Override
	protected void resetLayout() {
		LinearLayout root = (LinearLayout) findViewById(R.id.root_login);
		SupportDisplay.resetAllChildViewParam(root);
	}

	private void initPopuwindow() {
		usernames = new ArrayList<Map<String, Object>>();
		usernamerecord = new ListView(this);
		listlrs = loginRecordDao.queryAllRecordOfUserName();
		if (listlrs == null)
			listlrs = new ArrayList<String>();
		usernamerecord.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.frame_black));
		if (listlrs != null) {
			for (int i = 0; i < listlrs.size(); i++) {
				Map map = new HashMap();
				map.put("username", listlrs.get(i));
				map.put("icon", R.drawable.xicon);
				usernames.add(map);
			}
		}
		myadapter = new MyAdapter(this, usernames, R.layout.dropdown_item,
				new String[] { "username", "icon" }, new int[] { R.id.textview,
						R.id.delete });
		usernamerecord.setAdapter(myadapter);

		ViewTreeObserver vto = edtUsername.getViewTreeObserver();

		vto.addOnPreDrawListener(new OnPreDrawListener() {

			@Override
			public boolean onPreDraw() {
				wid = edtUsername.getMeasuredWidth();
				hei = edtUsername.getMeasuredHeight();
				int heii = 0;
				if (listlrs != null) {
					if (listlrs.size() <= 4)
						heii = listlrs.size() * hei;
					else {
						heii = 4 * hei;
					}
				}
				popu = new PopupWindow(usernamerecord, wid, heii, true);
				popu.setFocusable(true);
				popu.setOutsideTouchable(true);
				ColorDrawable c = new ColorDrawable(Color.WHITE);
				popu.setBackgroundDrawable(c);
				return true;
			}
		});
	}

	/**
	 * 获取系统条码规则
	 */
	private void getBarcodeDict() {
		HttpUtils http = new HttpUtils();
		String url = UrlConstant.getBarcodeDict();
		http.send(HttpRequest.HttpMethod.POST, url,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						JSONObject object = null;
						boolean status = false;
						try {
							object = new JSONObject(responseInfo.result);
							status = object.getBoolean("result");
							if (status) {
								Gson gson = new Gson();
								Type type = new TypeToken<List<BarcodeDict>>() {
								}.getType();
								JSONArray jsonArray = object
										.getJSONArray("data");
								List<BarcodeDict> barcodeDicts = new ArrayList<BarcodeDict>();
								barcodeDicts = gson.fromJson(
										jsonArray.toString(), type);
								LocalSave.setDataList(getApplicationContext(),
										GlobalConstant.KEY_BARCODE,
										barcodeDicts);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException e, String s) {

					}
				});
	}

	private String username;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private void doLogin() {
		String password = edtPassword.getText().toString().trim();
		username = edtUsername.getText().toString().trim();
		if (username.isEmpty()) {
			Toast.makeText(LoginAct.this, "请按扫描键扫描或者手动输入账号", Toast.LENGTH_SHORT).show();
		} else if (!flag) {
			flag = true;
			edtPassword.setBackgroundResource(R.drawable.edittextshape);
			edtUsername.setFocusable(false);
			edtPassword.setFocusable(true);
			edtUsername.setBackgroundResource(R.drawable.frame_black);
		} else if (password.isEmpty()) {
			Toast.makeText(LoginAct.this, "请输入密码!", Toast.LENGTH_SHORT).show();
		} else {
			pDialog.setCanceledOnTouchOutside(false);
			pDialog.setCancelable(false);
			pDialog.show();

			LoginBean loginBean = new LoginBean();
			loginBean.setPassword(password);
			loginBean.setUsername(username);
			UserInfo.setKey(null);
			RequestManager.connection(new LoginRequest(LoginAct.this,
					new IRespose<LoginBean>() {

						@Override
						public void doResult(LoginBean t, int id) {

						}

						@Override
						public void doResult(String result) throws KeyObsoleteException {
							Gson gson = new Gson();
							Type type = new TypeToken<LoginBean>() {
							}.getType();
							LoginBean t = gson.fromJson(result, type);
							if (!t.getResult()) {
								// 登录失败
								pDialog.dismiss();
								Toast.makeText(LoginAct.this, t.getMsg(),Toast.LENGTH_SHORT).show();
								return;
							}
							if(!StringTool.isEmpty(t.getServiceTime())){
								GlobalConstant.SERVICE_TIME = t.getServiceTime();
							}
							pDialog.setMessage("正在加载数据，请稍后...");
//							TB_LoginRecord table = new TB_LoginRecord();
//							table.setUpdateTime(System.currentTimeMillis());
//							table.setUserName(username);
//
//							if (!listlrs.contains(username)) {
//								loginRecordDao.insert(table);
//							} else {
//								loginRecordDao.update(table);
//							} 2017-3-22 17:25 why outdate
							BasicInfoItem basicInfoItem = t.getBasicInfo();
							if (basicInfoItem != null) {
								UserInfo.setCapability(basicInfoItem
										.getCapability());
								UserInfo.setDeptName(basicInfoItem
										.getDeptName());
								UserInfo.setName(basicInfoItem.getName());
								UserInfo.setDeptName(basicInfoItem
										.getItemName());
								List<WardInfoItem> list = basicInfoItem
										.getWardList();
								UserInfo.setWardList(list);//设置科室
								UserInfo.setWardCode(basicInfoItem.getDeptCode());
							} else {
								UserInfo.setName("");
								UserInfo.setDeptName("");
								UserInfo.setWardList(new ArrayList<WardInfoItem>());
								UserInfo.setWardCode("");
								UserInfo.setCapability("");
							}
                            getBarcodeDict();
                            UserInfo.setUserName(username);
							UserInfo.setKey(t.getKey());
							toHomeAct();
							startService(new Intent(LoginAct.this,CamerService.class));
							Intent sendIntent = new Intent("df.scanservice.toapp");
							sendBroadcast(sendIntent);
							pDialog.dismiss();
						}

						@Override
						public void doException(Exception e,
								boolean networkState) {
							pDialog.dismiss();
							if (networkState) {
								Toast.makeText(LoginAct.this, "加载异常" + e.getMessage(),
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(LoginAct.this, getString(R.string.netstate_content),
										Toast.LENGTH_SHORT).show();
							}
						}
					}, 0, true, loginBean));
		}
	}

	OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			if (position == 11) {

			} else if (position == 10) {

				if (!flag) {
					edtUsername.setText("");
				} else {
					edtPassword.setText("");
				}
			}
		}
	};

	public void toHomeAct() {
		Intent intent = new Intent(LoginAct.this, HomeAct.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.slide_in_right,
				R.anim.in_or_out_static);
	}

	class MyAdapter extends SimpleAdapter {

		private List<Map<String, Object>> data;

		public MyAdapter(Context context, List<Map<String, Object>> usernames,
				int resource, String[] from, int[] to) {
			super(context, usernames, resource, from, to);
			this.data = usernames;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			System.out.println(position);
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(LoginAct.this).inflate(
						R.layout.dropdown_item, null);
				holder.btn = (ImageButton) convertView
						.findViewById(R.id.delete);
				holder.tv = (TextView) convertView.findViewById(R.id.textview);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv.setText(data.get(position).get("username").toString());
			holder.tv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					edtUsername.setText(listlrs.get(position));
					edtUsername.setBackgroundResource(R.drawable.edittextshape);
					edtPassword.setBackgroundResource(R.drawable.frame_black);
					flag = false;
					popu.dismiss();
				}
			});
			holder.btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (usernames.size() > 0) {
						loginRecordDao.deleteByUserName(listlrs.get(position));
						usernames.remove(position);
						myadapter.notifyDataSetChanged();
					} else {
						popu.dismiss();
					}
				}
			});
			return convertView;
		}
	}

	class ViewHolder {
		private TextView tv;
		private ImageButton btn;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == R.id.txt_username) {
			edtUsername.setBackgroundResource(R.drawable.edittextshape);
			edtPassword.setBackgroundResource(R.drawable.frame_black);
			flag = false;
		} else {
			edtPassword.setBackgroundResource(R.drawable.edittextshape);
			edtUsername.setBackgroundResource(R.drawable.frame_black);
			flag = true;
		}

		return false;
	}

	/**
	 * 获取血袋上码的规则
	 */
	public void getBloodCodeRule() {
		String url = UrlConstant.loadBloodCodeRule() + "?username="
				+ UserInfo.getUserName();
		HttpUtils http = new HttpUtils();
		CCLog.i("获取血袋码规则>>>", url);
		http.send(HttpRequest.HttpMethod.POST, url,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						JSONObject jsonobj = null;
						JSONObject jsonobData = null;
						try {
							jsonobj = new JSONObject(responseInfo.result);
							boolean result = jsonobj.getBoolean("result");
							if (result) {
								jsonobData = jsonobj.getJSONObject("data");
								BloodCodeRule.setBloodCode(jsonobData
										.getString("bloodCode"));
								BloodCodeRule.setBloodType(jsonobData
										.getString("bloodType"));
								BloodCodeRule.setEffectiveDate(jsonobData
										.getString("effectiveDate"));
								BloodCodeRule.setPatCode(jsonobData
										.getString("patCode"));
								BloodCodeRule.setProductCode(jsonobData
										.getString("productCode"));
							} else {
								CCLog.i("获取血袋码规则失败");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(HttpException e, String s) {
						CCLog.i("获取血袋码规则失败");
					}
				});
	}

	public void downloadApk() {
		final String fileName = "mobilecare.apk";
		String serviceString = Context.DOWNLOAD_SERVICE;
		DownloadManager downloadManager;
		downloadManager = (DownloadManager) getSystemService(serviceString);
		// Uri uri = Uri.parse(UrlConstant.downloaderApk());
		Uri uri = Uri.parse("http://192.168.120.214:8081/mobilecare.apk");
		Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_DOWNLOADS).mkdir();
		CCLog.i("下载地址>>>" + uri);
		DownloadManager.Request request = new DownloadManager.Request(uri);
		final long refense = downloadManager.enqueue(request);
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		request.setMimeType("application/vnd.android.package-archive");
		request.setVisibleInDownloadsUi(false);
		request.setAllowedOverRoaming(false);
		request.setTitle("下载");
		request.setDescription("移动护理正在下载");
		request.allowScanningByMediaScanner();
		// request.setDestinationInExternalFilesDir(this,
		// Environment.DIRECTORY_DOWNLOADS,fileName);
		request.setDestinationInExternalPublicDir("/sdcard", fileName);
		// request.setDestinationInExternalFilesDir(this,null,fileName);
		// request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
		// fileName);

		IntentFilter filter = new IntentFilter(
				DownloadManager.ACTION_NOTIFICATION_CLICKED);
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String extraID = DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS;
				long[] references = intent.getLongArrayExtra(extraID);
				for (long reference : references) {
					if (reference == refense) {

						String file = Environment.getExternalStorageDirectory()
								+ "/download/" + fileName;
						Intent intentAv = new Intent(Intent.ACTION_VIEW);
						intentAv.setDataAndType(Uri.fromFile(new File(file)),
								"application/vnd.android.package-archive");
						startActivity(intentAv);
					}
				}
			}
		};

		registerReceiver(receiver, filter);
	}

	public void loadapk() {
		String fileName = "mobilecare.apk";
		final String path = "/sdcard/" + fileName;
		showProgressDialog();
		HttpUtils http = new HttpUtils();
		String url = UrlConstant.GetIpAndPort() + fileName;
		HttpHandler handler = http.download(url, path, true, true,
				new RequestCallBack<File>() {
					@Override
					public void onSuccess(ResponseInfo<File> responseInfo) {
						progressDialog.dismiss();
						Intent intentAv = new Intent(Intent.ACTION_VIEW);
						intentAv.setDataAndType(Uri.fromFile(new File(path)),
								"application/vnd.android.package-archive");
						startActivity(intentAv);
					}

					@Override
					public void onFailure(HttpException e, String s) {
						ShowToast("下载失败");
						progressDialog.dismiss();
					}
				});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
	}

	public void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.setMessage("下载中...");
		progressDialog.show();
	}
}
