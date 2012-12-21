package com.android.campusdishclient;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.campusdishclient.data.Dish;
import com.android.campusdishclient.data.DishBuffer;
import com.android.campusdishclient.util.TotalUtils;

public class ListDetailAdapter extends BaseAdapter{
 
	Context context;
	ArrayList<Integer> arrDishId;
	public ListDetailAdapter(Context ctx,ArrayList<Integer> pArrDishId){
		context=ctx;
		setIdArray(pArrDishId);
	}
	public int getCount() {

		return arrDishId.size();
	}   
	public void setIdArray(ArrayList<Integer> pArrDishId){
		arrDishId=pArrDishId;
	}
	public Object getItem(int position) {
		return DishBuffer.getDishBuffer().getDish(arrDishId.get(position));
	}

	public long getItemId(int position) {
		return arrDishId.get(position);
	}
	private Dish getDish(int pPosition){
		TotalUtils.Log("List获得dish,"+DishBuffer.getDishBuffer().getDish(arrDishId.get(pPosition)).getDishName());
		return DishBuffer.getDishBuffer().getDish(arrDishId.get(pPosition));
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView==null){
			convertView=View.inflate(context, R.layout.mainlistlayout, null);
			viewHolder=new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else{                                          
			viewHolder=(ViewHolder)convertView.getTag();
		}
		viewHolder.text.setText(getDish(position).getDishName());
		viewHolder.img.setImageBitmap(getDish(position).getDishImage());
		viewHolder.text_Price.setText("￥"+String.valueOf(getDish(position).getPrice()));
		return convertView;
	}
	static class ViewHolder{	
		private ImageView img;
		private TextView text;
		private TextView text_Price;
		public ViewHolder(View v){
			img=(ImageView)v.findViewById(R.id.main_listdetail_image);
			text=(TextView)v.findViewById(R.id.main_listdetail_text);
			text_Price=(TextView)v.findViewById(R.id.main_listdetail_textPrice);
//			img.setAdjustViewBounds(true);
		}
	}
}