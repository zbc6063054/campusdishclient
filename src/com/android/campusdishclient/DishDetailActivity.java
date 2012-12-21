package com.android.campusdishclient;

import com.android.campusdishclient.data.Cart;
import com.android.campusdishclient.data.Dish;
import com.android.campusdishclient.data.DishBuffer;
import com.android.campusdishclient.util.TotalUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class DishDetailActivity extends Activity {

	private int mDishId=-1;//菜ID
	private Dish mDish;
	private TextView text_dishName,text_dishDetail;
	private ImageView img_dishImg;
	private TextView text_dishPrice;
	private Button btn_addToCart;
	private RatingBar ra_Grade;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dishdetaillayout);
		findViews();
		init();
		show();//显示信息
	}
	private void show(){
		img_dishImg.setImageBitmap(mDish.getDishImage());
		text_dishDetail.setText(mDish.getDishDetail());
		text_dishName.setText(mDish.getDishName());
		text_dishPrice.setText("￥"+String.valueOf(mDish.getPrice()));
		ra_Grade.setRating((float)mDish.getDishGrade()/2);
//		ra_Grade.setRating(2.0f);
	}
	private void init() { 
		btn_addToCart.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Cart.getCart().addToCart(mDish);
				
				TotalUtils.showToast(getApplicationContext(), mDish.getDishName()+" 加入菜单成功！");
				
				DishDetailActivity.this.finish();
			}
		});
		Intent intent=getIntent();
		mDishId=intent.getIntExtra(TotalUtils.STR_DISHID, -1);
		TotalUtils.Log("获得的Id:",mDishId);
		mDish=DishBuffer.getDishBuffer().getDish(mDishId);
	}

	private void findViews() { 
		text_dishDetail=(TextView) findViewById(R.id.DishDetail_text_dishDetail);
		text_dishName=(TextView) findViewById(R.id.DishDetail_text_dishName);
		text_dishPrice=(TextView) findViewById(R.id.DishDetail_text_dishPrice);
		img_dishImg=(ImageView) findViewById(R.id.DishDetail_image_mainImage);
		btn_addToCart=(Button) findViewById(R.id.DishDetail_button_addToCart);
		ra_Grade=(RatingBar)findViewById(R.id.DishDetail_RatingBar_Grade);
		//ra_Grade.setMax(10);
		ra_Grade.setStepSize(0.5f);
		ra_Grade.setEnabled(false);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

}
