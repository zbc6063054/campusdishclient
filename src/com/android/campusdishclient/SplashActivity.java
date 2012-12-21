package com.android.campusdishclient;

import org.apache.http.cookie.SetCookie;
import org.apache.http.protocol.ResponseDate;

import com.android.campusdishclient.data.ResourceData;
import com.android.campusdishclient.util.BackgroundUtil;
import com.android.campusdishclient.util.BackgroundUtil.Deliver;
import com.android.campusdishclient.util.DialogUtil;
import com.android.campusdishclient.util.HttpUtil;

import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public class SplashActivity extends Activity {

	private final int SPLASH_DISPLAY_LENGHT = 3000; // 延迟三秒

	private Handler mHandler;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash);
		if (isFirstStart()) {
			showInputIp();
		}else{
			dolater();
		}
		

		// switchTiMain();
	}

	private boolean checkString(String s) {
		return s.matches("^(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})(\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})){3}$");
	}

	void WriteSharedPreferences(String key, String strPassword) {
		SharedPreferences user = getMySharedPreferences();
		Editor editor = user.edit();
		editor.putString(key, strPassword);
		editor.commit();
	}

	void showInputIp() {
		final EditText edittext = new EditText(this);
		DialogUtil.showViewDialog(this, "首次启动，请输入服务器IP地址：", edittext,
				new DialogUtil.Handler() {
					@Override
					public void todo(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (!edittext.getText().toString().trim().equals("")) {
							if (checkString(edittext.getText().toString()
									.trim())) {
								WriteSharedPreferences(ISFIRSTSTART, false);
								WriteSharedPreferences(
										ResourceData.SETTING_SERVERIP, edittext
												.getText().toString());
								dolater();
								
							} else {
								DialogUtil.showAffirmDialog(
										SplashActivity.this, "错误",
										"IP地址格式输入不正确！应为：192.168.1.1",
										new DialogUtil.Handler() {
											@Override
											public void todo(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method
												// stub
												showInputIp();
											}
										}, new DialogUtil.Handler() {

											@Override
											public void todo(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method
												// stub
												System.exit(0);
											}
										});
							}
						}else{
							DialogUtil.showAffirmDialog(
									SplashActivity.this, "错误",
									"ip地址不能为空",
									new DialogUtil.Handler() {
										@Override
										public void todo(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method
											// stub
											showInputIp();
										}
									}, new DialogUtil.Handler() {

										@Override
										public void todo(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method
											// stub
											System.exit(0);
										}
									});
						}
					}
				}, new DialogUtil.Handler() {

					@Override
					public void todo(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						System.exit(0);
					}
				});
	}

	void WriteSharedPreferences(String key, boolean bool) {
		SharedPreferences user = getMySharedPreferences();
		Editor editor = user.edit();
		editor.putBoolean(key, bool);
		editor.commit();
	}
	void dolater(){
		
		loadSettings();// 加载设置
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
			}
		};
		BackgroundUtil.doInBackGround(null, new Deliver() {

			@Override
			public Object todo() {
				return null;
			}

		}, null);
		switchToMain();
	}
	private void switchToMain() {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent mainIntent = new Intent(SplashActivity.this,
						CampusdishclientActivity.class);
				SplashActivity.this.startActivity(mainIntent);
				SplashActivity.this.finish();
			}
		}, SPLASH_DISPLAY_LENGHT);
	}

	private SharedPreferences getMySharedPreferences() {
		return getSharedPreferences("aplicaint_info", 0);
	}

	public void loadSettings() {
		HttpUtil.BASE_URL = "http://"
				+ getMySharedPreferences().getString(
						ResourceData.SETTING_SERVERIP, "10.0.2.2:80") + "/";
	}

	boolean isFirstStart() {
		SharedPreferences user = getSharedPreferences("aplicaint_info", 0);
		return user.getBoolean(ISFIRSTSTART, true);
	}

	private static final String ISFIRSTSTART = "isFirstStart";

}
