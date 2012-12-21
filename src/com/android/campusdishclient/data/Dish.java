package com.android.campusdishclient.data;



import android.graphics.Bitmap;

public class Dish {
	private int mDishID=0;
	private String mDishImagePath="";
	private Bitmap mDishImage;
	private String mDishName="正在加载";
	private String mDishDetail="正在加载";
	private double mDishGrade=0.0;

	public double getDishGrade() {
		return mDishGrade;
	}
	public void setDishGrade(double pDishGrade) {
		this.mDishGrade = pDishGrade;
	}
	private float mPrice=0.0f;
	
	public Dish(int dishId){
		mDishID=dishId;
	}
	public Dish(Dish dish){
		mDishID=dish.getDishID();
		copy(dish);
	}
	public Bitmap getDishImage() {
		return mDishImage;
	}
	public void setDishImage(Bitmap mDishImage) {
		this.mDishImage = mDishImage;
	}
	public String getDishDetail() {
		return mDishDetail;
	}
	public void setDishDetail(String mDishDetail) {
		this.mDishDetail = mDishDetail;
	}
	public String getDishName() {
		return mDishName;
	}
	public void setDishName(String mDishName) {
		this.mDishName = mDishName;
	}
	public String getDishImagePath() {
		return mDishImagePath;
	}
	public void setDishImagePath(String pDishImagePath) {
		this.mDishImagePath = pDishImagePath;
	}
	public int getDishID() {
		return mDishID;
	}
	public void copy(Dish dish) {
		mDishID=dish.mDishID;
		mDishImagePath=dish.mDishImagePath;
		mDishImage=dish.mDishImage;
		mDishName=dish.mDishName;
		mDishDetail=dish.mDishDetail;
		mPrice=dish.mPrice;
		mDishGrade=dish.mDishGrade;
	}
	public float getPrice() {
//		TotalUtils.Log("返回价格："+String.valueOf(mPrice));
		return mPrice;
	}
	public void setPrice(float pPrice) {
		this.mPrice = pPrice;
//		TotalUtils.Log("价格被设为："+String.valueOf(this.mPrice));
	}
}
