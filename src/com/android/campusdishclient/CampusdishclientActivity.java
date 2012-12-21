package com.android.campusdishclient;



import com.android.campusdishclient.data.ResourceData;
import com.android.campusdishclient.util.DialogUtil;
import com.android.campusdishclient.util.HttpUtil;
import android.app.ActivityGroup;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost.TabSpec;
import android.widget.TabHost;

public class CampusdishclientActivity extends ActivityGroup implements
		OnClickListener {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mainlayout);
		getViews();
		init();
		switchActivity(DISHCLASS_BREAKFAST);

	}

	private static final int MENU_SHOWCART = Menu.FIRST;
	private static final int MENU_SETSERVERIP=Menu.FIRST+1;
	private static final int MENU_REFRESH=Menu.FIRST+2;

	// private ListClassAdapter mListClassAdapter;
	private TabHost mTabHost;
	private TabSpec pTabSec;
//	private RadioButton mRadioButton_breakfast, mRadioButton_lunch,
//			mRadioButton_supper;
	private RadioGroup mRadioGroup;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_SHOWCART, 0, R.string.Main_showCart);
		menu.add(0, MENU_SETSERVERIP, 0, R.string.Main_setServerIp);
		menu.add(0,MENU_REFRESH,0,"刷新");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case MENU_SHOWCART:
			startActivity(new Intent(this, ShowCartActivity.class));
			break;
		case MENU_SETSERVERIP:
			setServerIp();
			break;
		case MENU_REFRESH:
			refresh();
			break;
		}
		return true; 
	}
	private void refresh(){
		Intent intent=new Intent(this,SplashActivity.class);//CampusdishclientActivity.class);
		PendingIntent p=PendingIntent.getActivity(this, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
		AlarmManager mgr=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
		mgr.set(AlarmManager.RTC, System.currentTimeMillis(), p);
		System.exit(0);
	}
	private void setServerIp(){
		final EditText edittext=new EditText(this);
		edittext.setText(getSharedPreferences("aplicaint_info", 0).getString(ResourceData.SETTING_SERVERIP, ""));
		DialogUtil.showViewDialog(this, "设置服务器IP地址：", edittext,
				new DialogUtil.Handler() {
					@Override
					public void todo(DialogInterface dialog, int which) {
						if (!edittext.getText().toString().trim().equals("")) {
							if (checkString(edittext.getText().toString()
									.trim())) {
								WriteSharedPreferences(
										ResourceData.SETTING_SERVERIP, edittext
												.getText().toString());
								HttpUtil.BASE_URL= edittext
										.getText().toString();
								dolater();
								
							} else {
								DialogUtil.showAffirmDialog(
										CampusdishclientActivity.this, "错误",
										"IP地址格式输入不正确！应为：192.168.1.1",
										new DialogUtil.Handler() {
											@Override
											public void todo(
													DialogInterface dialog,
													int which){
												setServerIp();
											}
										}, new DialogUtil.Handler() {

											@Override
											public void todo(
													DialogInterface dialog,
													int which) {
											}
										});
							}
						}else{
							DialogUtil.showAffirmDialog(
									CampusdishclientActivity.this, "错误",
									"ip地址不能为空",
									new DialogUtil.Handler() {
										@Override
										public void todo(
												DialogInterface dialog,
												int which) {
											setServerIp();
										}
									}, new DialogUtil.Handler() {

										@Override
										public void todo(
												DialogInterface dialog,
												int which) {
										}
									});
						}
					}
				}, new DialogUtil.Handler() {

					@Override
					public void todo(DialogInterface dialog, int which) {

					}
				});
	}
	void WriteSharedPreferences(String key, String strPassword) {
		SharedPreferences user = getSharedPreferences("aplicaint_info", 0);
		Editor editor = user.edit();
		editor.putString(key, strPassword);
		editor.commit();
	}

	private boolean checkString(String s) {
		return s.matches("^(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})(\\.(25[0-5]|2[0-4][0-9]|1[0-9][0-9]|[0-9]{1,2})){3}$");
	}
	private void dolater(){
//		DishBuffer.clear();
//		Cart.clear();
//		DishListActivity.clear();
//		this.finish();
//		System.exit(0);
		Intent intent=new Intent(this,SplashActivity.class);
		PendingIntent p=PendingIntent.getActivity(this, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
		AlarmManager mgr=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
		mgr.set(AlarmManager.RTC, System.currentTimeMillis(), p);
		System.exit(0);
	}
	private void getViews() {
		mTabHost = (TabHost) findViewById(R.id.main_tabhost);
//		mRadioButton_breakfast = (RadioButton) findViewById(R.id.main_radio_breakfast);
//		mRadioButton_lunch = (RadioButton) findViewById(R.id.main_radio_lunch);
//		mRadioButton_supper = (RadioButton) findViewById(R.id.main_radio_supper);
		mRadioGroup=(RadioGroup)findViewById(R.id.main_radiogroup );
	}

	private void init() {
		ResourceData.getResourceData(this);
		initTabs();
		initRadios();
	}

	private void initRadios() {
//		mRadioButton_breakfast.setOnClickListener(this);
//		mRadioButton_lunch.setOnClickListener(this);
//		mRadioButton_supper.setOnClickListener(this);
	}

	private void initTabs() {
		String[] arrDishClass=ResourceData.arrDishClass;
		Intent intent;
		mTabHost.setup(this.getLocalActivityManager());
		
		int index=0 ;
		for(final String str:arrDishClass){
			pTabSec = mTabHost.newTabSpec(str);
			intent = new Intent(this, DishListActivity.class);
			intent.putExtra(CampusdishclientActivity.MSG_DISHCLASS, ResourceData.arrDishClassId[index]);
			pTabSec.setIndicator(str);
			pTabSec.setContent(intent);
			mTabHost.addTab(pTabSec);
			RadioButton btn=(RadioButton)View.inflate(this, R.layout.main_radiobutton , null);//new RadioButton(this);
			btn.setText(str);
			//btn.
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) { 
					mTabHost.setCurrentTabByTag(str);
				}
			});
			
			mRadioGroup.addView(btn,Integer.parseInt(getResources().getString(R.string.main_radio_button_width)),Integer.parseInt(getResources().getString(R.string.main_radio_button_height)));
			index++;
		}
		((RadioButton)(mRadioGroup.getChildAt(0))).setChecked(true);
		
		
//		pTabSec = mTabHost.newTabSpec("早餐");
//		intent = new Intent(this, DishListActivity.class);
//		intent.putExtra(CampusdishclientActivity.MSG_DISHCLASS, 1);
//		pTabSec.setContent(intent);
//		pTabSec.setIndicator("早餐");
//
//		mTabHost.addTab(pTabSec);
//
//		pTabSec = mTabHost.newTabSpec("中餐");
//		intent = new Intent(this, DishListActivity.class);
//		intent.putExtra(CampusdishclientActivity.MSG_DISHCLASS, 2);
//		pTabSec.setContent(intent);
//		pTabSec.setIndicator("中餐");
//		mTabHost.addTab(pTabSec);
//
//		pTabSec = mTabHost.newTabSpec("晚餐");
//		pTabSec.setIndicator("晚餐");
//		intent = new Intent(this, DishListActivity.class);
//		intent.putExtra(CampusdishclientActivity.MSG_DISHCLASS, 3);
//		pTabSec.setContent(intent);
//		mTabHost.addTab(pTabSec);
//		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
//
//			@Override
//			public void onTabChanged(String tabId) {
//
//			}
//		});
	}

	boolean isBackKeyDown = false;

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_UP&&isBackKeyDown) {
				DialogUtil.showAffirmDialog(this, "确认", "确认退出？", true,
						new DialogUtil.Handler() {
							public void todo(DialogInterface dialog, int witch) {
								CampusdishclientActivity.this.finish();
								System.exit(0);
							}
						});
				isBackKeyDown = false;
				return true;
			} else if (event.getAction() == KeyEvent.ACTION_DOWN) {
				isBackKeyDown = true;
				return true;
			}

		}
		return super.dispatchKeyEvent(event);

	}

	private void switchActivity(int pDishClass) {
		switch (pDishClass) {
		case DISHCLASS_BREAKFAST:
			mTabHost.setCurrentTab(0);
			break;
		case DISHCLASS_LUNCH:
			break;
		case DISHCLASS_SUPPER:
			break;
		}
	}

	public static final String MSG_DISHCLASS = "msg_dishclass";

	public static final int DISHCLASS_BREAKFAST = 1;
	public static final int DISHCLASS_LUNCH = 2;
	public static final int DISHCLASS_SUPPER = 3;

	@Override
	public void onClick(View v) {
//		switch (v.getId()) {
//		case (R.id.main_radio_breakfast):
//			mTabHost.setCurrentTab(0);
//			break;
//		case (R.id.main_radio_lunch):
//			mTabHost.setCurrentTab(1);
//			break;
//		case (R.id.main_radio_supper):
//			mTabHost.setCurrentTab(2);
//			break;
//		}
	}
}
