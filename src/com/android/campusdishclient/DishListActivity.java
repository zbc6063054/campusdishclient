package com.android.campusdishclient;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

import com.android.campusdishclient.data.DishBuffer;
import com.android.campusdishclient.data.DishBuffer.RefreshHandler;
import com.android.campusdishclient.util.BackgroundUtil;
import com.android.campusdishclient.util.BackgroundUtil.PreDeliver;
import com.android.campusdishclient.util.BackgroundUtil.AftDeliver;
import com.android.campusdishclient.util.BackgroundUtil.Deliver;
import com.android.campusdishclient.util.DialogUtil;
import com.android.campusdishclient.util.HttpUtil;
import com.android.campusdishclient.util.TotalUtils;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class DishListActivity extends Activity {

	// 主菜单，及详细菜单List
	private ListView mListDetail;
	private ListDetailAdapter mListDetailAdapter;
	private static RefreshHandler mRefreshHandler;
	private int mDishClass = -1;
	private ArrayList<Integer> mArrDishId;
	private static ArrayList<DishListActivity>  mArrDishList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.dishlist);
		TotalUtils.Log("DishList创建");
		findViews();
		init();
		setListeners();
		loadData();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		TotalUtils.Log("Dish List On Resume");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		TotalUtils.Log("Dish List On Pause");
	}

	private static ProgressDialog dialog;

	public void loadData() {
		BackgroundUtil.doInBackGround(new PreDeliver() {

			@Override
			public void todo() {
				try {
					if (dialog == null) {
						dialog = DialogUtil.showSpinnerProgressDialog(
								DishListActivity.this, "正在加载");
					}
					if (dialog.isShowing()) {
						//DialogUtil.cancelProgressDialog(dialog);
						return;
					}
					dialog.show();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}, new Deliver() {

			@Override
			public Object todo() {
				try {
					TotalUtils.Log("获得");
					mArrDishId = HttpUtil.getDishIdByClass(mDishClass);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					return false;
				} catch (IOException e) {
					TotalUtils.showToastInThread(DishListActivity.this,
							"网络错误！请确保手机已连入互联网。");
					e.printStackTrace();
					return false;
				} catch (NullPointerException e) {
					TotalUtils.showToastInThread(DishListActivity.this,
							"服务器响应异常，请稍后再试！");
					return false;
				}
				return true;
			}

		}, new AftDeliver() {

			@Override
			public void todo(Object result) {
				DialogUtil.cancelProgressDialog(dialog);
				if (!(Boolean) result) {
					// TotalUtils.showToast(DishListActivity.this,
					// "网络错误！请确保手机已连入互联网。");

				}
				TotalUtils.Log("刷新");
				mListDetailAdapter.setIdArray(mArrDishId);
				mListDetailAdapter.notifyDataSetChanged();
			}
		});

	}

	private void setListeners() {

		mListDetail.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i(DishListActivity.this.getApplication().getPackageName(),
						String.valueOf(position));
				Intent intent = new Intent(DishListActivity.this,
						DishDetailActivity.class);
				intent.putExtra(TotalUtils.STR_DISHID, (int) id);
				TotalUtils.Log("put的Id:", (int) id);
				DishListActivity.this.startActivity(intent);
			}
		});

	}

	private void init() {
		if(mArrDishList==null){
			mArrDishList=new ArrayList<DishListActivity>();
		}
		mArrDishList.add(this);
		mDishClass = getIntent().getIntExtra(
				CampusdishclientActivity.MSG_DISHCLASS, -1);
		if (mDishClass == -1) {
			TotalUtils.Log("错误,DishList未得到DishClass,已重置为1");
			mDishClass = 1;
		}
		if (mRefreshHandler == null) {
			mRefreshHandler = new RefreshHandler() {
				@Override
				public void refresh() {
					TotalUtils.Log("MainActivity中刷新列表！");
					DishListActivity.refresh();
				}
			};
			DishBuffer.getDishBuffer().setRefreshHandler(mRefreshHandler);
		}
		if (myAdapters == null) {
			myAdapters = new ArrayList<ListDetailAdapter>();
		}
		TotalUtils.Log("获得classId:", mDishClass);
		mArrDishId = new ArrayList<Integer>();
		mListDetail.setAdapter(mListDetailAdapter = new ListDetailAdapter(this,
				mArrDishId));
		myAdapters.add(mListDetailAdapter);

	}

	public static void clear(){
		for(DishListActivity a:mArrDishList){
			a.loadData();
			a.mListDetailAdapter.notifyDataSetChanged();
		}
		
	}
	private static ArrayList<ListDetailAdapter> myAdapters;

	protected static void refresh() {
		TotalUtils.Log("刷新列表！数量：", myAdapters.size());

		for (ListDetailAdapter list : DishListActivity.myAdapters) {
			list.notifyDataSetChanged();
		}
	}

	private void findViews() {
		mListDetail = (ListView) findViewById(R.id.main_listdetail);

	}

}
