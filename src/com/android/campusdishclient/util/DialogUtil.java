package com.android.campusdishclient.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Message;
import android.view.View;

public class DialogUtil {
	public static void showDialog(final Context c, String msg, boolean closeSelf) {
		AlertDialog.Builder builder = new AlertDialog.Builder(c)
				.setMessage(msg).setCancelable(false);
		if (closeSelf) {
			builder.setPositiveButton("确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 结束Activity
					((Activity) c).finish();
				}
			});
		} else {
			builder.setPositiveButton("确定", null);
		}
		builder.create().show();
	}

	/**
	 * 显示确认对话框
	 * 
	 * @param c
	 *            Context
	 * @param title
	 *            标题
	 * @param msg
	 *            显示的信息
	 * @param cancel
	 *            是否有取消按钮
	 * @param pHandler
	 *            确认时调用的方法
	 */
	public static void showAffirmDialog(final Context c, String title,
			String msg, boolean cancel, final Handler pHandler) {
		AlertDialog.Builder builder = new AlertDialog.Builder(c)
				.setTitle(title).setMessage(msg).setCancelable(cancel);
		builder.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 结束Activity
				if (pHandler != null) {
					pHandler.todo(dialog, which);
				}
			}
		});
		builder.setNegativeButton("取消", null);
		builder.create().show();
	}
	public static void showAffirmDialog(final Context c, String title,
			String msg, final Handler pHandlerOK,final Handler pHandlerCancel) {
		AlertDialog.Builder builder = new AlertDialog.Builder(c)
				.setTitle(title).setMessage(msg).setCancelable(false);
		builder.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 结束Activity
				if (pHandlerOK != null) {
					pHandlerOK.todo(dialog, which);
				}
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 结束Activity
				if (pHandlerCancel != null) {
					pHandlerCancel.todo(dialog, which);
				}
			}
		});
		builder.create().show();
	}

	public static void showViewDialog(final Context c, String title,
			 boolean cancel,View v, final Handler pHandler) {
		AlertDialog.Builder builder = new AlertDialog.Builder(c)
				.setTitle(title).setView(v).setCancelable(cancel);
		builder.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 结束Activity
				if (pHandler != null) {
					pHandler.todo(dialog, which);
				}
			}
		});
		builder.setNegativeButton("取消", null);
		builder.create().show();
	}
	public static void showViewDialog(final Context c, String title,View v,final Handler pHandlerOK,final Handler pHandlerCancel) {
		AlertDialog.Builder builder = new AlertDialog.Builder(c)
				.setTitle(title).setView(v).setCancelable(false);
		builder.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 结束Activity
				if (pHandlerOK != null) {
					pHandlerOK.todo(dialog, which);
				}
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 结束Activity
				if (pHandlerCancel != null) {
					pHandlerCancel.todo(dialog, which);
				}
			}
		});
		builder.create().show();
	}
	
	public interface Handler {
		public void todo(DialogInterface dialog, int which);
	}
	
	public static ProgressDialog showSpinnerProgressDialog(Context context,
			String msg)
	{
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(msg);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		
		return progressDialog;
	}
	public static void cancelProgressDialog(final ProgressDialog progressDialog)
	{
		if(!progressDialog.isShowing()) return;
		android.os.Handler handler = new android.os.Handler()		{
			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);
				progressDialog.cancel();
			}

		};
		handler.sendEmptyMessage(0);

	}
}
