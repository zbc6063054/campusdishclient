package com.android.campusdishclient.util;

import android.content.Context;
import android.content.Intent;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;



public class TotalUtils {
	public static String STR_DISHID="Dish_Id";
	public static void Log(String msg){
		android.util.Log.i("com.android.campusdishclient",msg);
	}
	public static void Log(int msg){
		android.util.Log.i("com.android.campusdishclient",String.valueOf(msg));
	}
	public static void Log(String msg1,int msg){
		android.util.Log.i("com.android.campusdishclient",msg1+String.valueOf(msg));
	}
	public static void showToast(Context context,String msg){
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	public static void showToastInThread(final Context context, final String msg)
	{
//		Looper.prepare();
//		Handler handler = new Handler()
//		{
//
//			@Override
//			public void handleMessage(Message msg)
//			{
//				super.handleMessage(msg);
//				showToast(context, (String)msg.obj);
//			}
//
//		};
//		Message msg2=handler.obtainMessage();
//		msg2.obj=msg;
//		handler.sendMessage(msg2);
//		Looper.loop();
		Handler handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {
			public void run() {
				showToast(context,msg);
			}
		});
	}
	public static void startActivity(Context context,Class<?> activity){
		Intent intent=new Intent(context,activity);
		context.startActivity(intent);
	}
}
