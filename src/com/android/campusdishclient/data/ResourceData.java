package com.android.campusdishclient.data;

import com.android.campusdishclient.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class ResourceData {
	private static ResourceData mResourceData;
	private Context mContext;
	private Bitmap[] mArrBitmap;
	private ResourceData(Context context){
		mContext=context;
		getData();
	}
	public Bitmap getBitmap(final int id){
		return mArrBitmap[id];
	}
	private void getData(){
		mArrBitmap=new Bitmap[10];
		mArrBitmap[BITMAP_NOPICTURE]=((BitmapDrawable)(mContext.getResources().getDrawable(R.drawable.nopic))).getBitmap();
	}
	
	public static ResourceData getResourceData(Context pContext){
		if(mResourceData==null){
			if(pContext==null){
				throw new NullPointerException("ResourceData为null");
			}else{
				mResourceData=new ResourceData(pContext);
			}
		}
		return mResourceData;
	}
	
	public static final int BITMAP_NOPICTURE=0;
	public static final String SETTING_SERVERIP="setting_serverIp";
	public static String[]  arrDishClass={"鲁菜","川菜","粤菜","苏菜","闽菜","浙菜","湘菜","徽菜","酒水","凉菜","汤"};
	public static int[] arrDishClassId={1,2,3,4,5,6,7,8,12,13,14};
}
