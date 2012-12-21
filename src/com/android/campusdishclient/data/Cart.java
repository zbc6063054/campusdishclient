package com.android.campusdishclient.data;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;


public class Cart {

	private ArrayList<Dish> mDishList;
	private ArrayList<Integer> mDishIdList;
	private static Cart mCart;
	private float mPrice=0.0f;
	private String mContent="无";
	private Cart(){
		mDishList=new ArrayList<Dish>();
		mDishIdList=new ArrayList<Integer>();
	}
	public static Cart getCart(){
		if(mCart==null){
			mCart=new Cart();
		}
		return mCart;    
	}
	public static void clear(){
		mCart=null;
	}
	public void addToCart(Dish dish){
		mDishList.add(dish);  
		mDishIdList.add(dish.getDishID());
		calculatePrice();
	}
	public void reset(){
		mDishList.clear();
		mDishIdList.clear();
		calculatePrice();
	}
	public int getCartSize(){
		return mDishList.size();
	}
	public Dish getDish(int index){
		return mDishList.get(index);
	}
//	public Dish[] getDishs(){
//		Dish[] pdish=new Dish[5];			               	 	
//		mDishList.toArray(pdish);
//		return pdish;
//	}
	public void setContent(String pContent){
		mContent=pContent;
	}
	public String getContent(){
		return mContent;
	}
	public boolean deleteDish(int pDishId){
		int index=mDishIdList.indexOf(pDishId);
		
		if(index==-1){
			return false;
		}
		mDishIdList.remove(index);
		mDishList.remove(index);
		calculatePrice();
		return true;
	}
	public final Dish[] getAllDish(){
		int s=mDishList.size();
		final Dish[] dish = new Dish[s] ;
		for(int i=0;i<s;i++){
			dish[i]=new Dish(mDishList.get(i));
		}
		return dish;
	}
	public float getPrice() {
		return mPrice;
	}
	private void calculatePrice(){
		mPrice=0.0f;
		for(Dish dish:mDishList){
			mPrice+=dish.getPrice();
		}
	}
	public Map<String,String> getMap(){
		Map<String,String> map=new IdentityHashMap<String,String>();
		for(int i:mDishIdList){
			map.put(new String(KEY_DISHID), String.valueOf(i));
		}
		map.put(KEY_ORDERCONTENT, mContent);
		return map;
		
	}
	public static final String KEY_DISHID="DishId";
	public static final String KEY_ORDERCONTENT="ordercontent";
}
