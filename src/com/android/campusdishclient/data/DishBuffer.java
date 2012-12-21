package com.android.campusdishclient.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.http.client.ClientProtocolException;

import com.android.campusdishclient.util.HttpUtil;
import com.android.campusdishclient.util.TotalUtils;

import android.os.AsyncTask;

public class DishBuffer {
	private static DishBuffer msDishBuffer;
	private HashMap<Integer,Dish> mDishMap;
	private DishGetter mDishGetter;
	private RefreshHandler mRefreshHandler;
	
	private DishBuffer(){
		mDishMap=new HashMap<Integer,Dish>();
		mDishGetter=new DishGetter();
	}
	public static DishBuffer getDishBuffer(){
		if(msDishBuffer==null){
			msDishBuffer=new DishBuffer();
		}
		return msDishBuffer;
	}
	public static void clear(){
		msDishBuffer=null;
	}
	public Dish getDish(int pDishId){
		Dish dish;
		if(!mDishMap.containsKey(pDishId)){//如果队列中不包含,则加入队列
			dish=new Dish(pDishId);
			//TODO
			mDishMap.put(pDishId,dish);
			mDishGetter.addToList(dish);
			TotalUtils.Log("DishBuffer 不包含:"+dish.getDishID());
		}else{
			dish=mDishMap.get(pDishId);
			TotalUtils.Log("DishBuffer 已包含:"+dish.getDishID());
		}
		return dish;
	}
	public void setRefreshHandler(RefreshHandler re){
		mRefreshHandler=re;
	}
	/**
	 * 刷新列表
	 */
	void refresh(){
		if(mRefreshHandler!=null){
			TotalUtils.Log("DishBuffer获得dish,刷新列表");
			mRefreshHandler.refresh();
		}
	}
	
	public interface RefreshHandler{
		public void refresh();
	} 
	
	private class DishGetter{
		ArrayList<Dish> mArr;
		BackGroundTask mBackGroundTask;
		private DishGetter(){
			mArr=new ArrayList<Dish>();
		}
		void addToList(Dish pDish){
			mArr.add(pDish);
			if(mBackGroundTask==null){
				mBackGroundTask=new BackGroundTask(pDish);
				mBackGroundTask.execute(pDish);
			}
		}
		void backGroundFinish(boolean result,Dish pDish){
			mArr.remove(pDish);
			mBackGroundTask=null;
			if(result==false){//网络错误，更新失败
				//TODO 
				TotalUtils.Log("网络错误，更新失败！");
			}else{
				TotalUtils.Log("获取Dish成功,id="+String.valueOf(pDish.getDishID())+",dishName="+pDish.getDishName());
			}
			refresh();
			if(!mArr.isEmpty()){
				mBackGroundTask=new BackGroundTask(mArr.get(0));
				mBackGroundTask.execute(mArr.get(0));
			}
		}
		class BackGroundTask extends AsyncTask<Dish, Integer, Boolean>{
			private Dish mDish;
			public BackGroundTask(Dish pDish) {
				mDish=pDish;
			}
			@Override
			protected Boolean doInBackground(Dish... params) {
				TotalUtils.Log("获取Dish中,id="+String.valueOf(params[0].getDishID()));
				try {
					params[0].copy(HttpUtil.getDishById(params[0].getDishID()));
					
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
					TotalUtils.Log("网络错误，获取Dish,id="+String.valueOf(params[0].getDishID()));
					params[0].setDishName("网络错误");
					params[0].setDishDetail("网络错误");
				}catch(Exception e){
					params[0].setDishName("网络错误");
					params[0].setDishDetail("网络错误");
				}
				publishProgress(0); 
				try {
					params[0].setDishImage(HttpUtil.getImageBitmap(params[0].getDishImagePath()));
					TotalUtils.Log("获取Image成功：id="+params[0].getDishID());
					return true;
			 	} catch (IOException e) {
				
					params[0].setDishImage(ResourceData.getResourceData(null).getBitmap(ResourceData.BITMAP_NOPICTURE));
					TotalUtils.Log("获取Image失败：id="+params[0].getDishID());
				} catch(Exception e){
					e.printStackTrace();
					TotalUtils.Log("获取Image失败：id="+params[0].getDishID());
				}
				return false;
			}
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}
			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				TotalUtils.Log("DishBuffer 获得Dish结束，返回：",mDish.getDishID());
				DishGetter.this.backGroundFinish(result, mDish);
			}

			@Override
			protected void onProgressUpdate(Integer... values) {
				// TODO Auto-generated method stub
				super.onProgressUpdate(values);
				refresh();
			}
		}
	}
}
