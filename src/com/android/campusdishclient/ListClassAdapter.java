package com.android.campusdishclient;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.campusdishclient.R;
import com.android.campusdishclient.data.DishBuffer;
import com.android.campusdishclient.util.HttpUtil;

public class ListClassAdapter extends BaseAdapter{
	
	ArrayList<Integer> arrClassId;
	private Context context;
	public ListClassAdapter(Context context){
		this.context=context;
		reload();
	}

	public void reload(){
		arrClassId=HttpUtil.getClassId(2);
		if(arrClassId==null){
			arrClassId=new ArrayList<Integer>();
		}
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	private String getString(int position){
		return DishBuffer.getDishBuffer().getDish(arrClassId.get(position)).getDishName();
	}
	private Bitmap getImage(int position){
		
		Bitmap d=DishBuffer.getDishBuffer().getDish(arrClassId.get(position)).getDishImage();
		return d;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView==null){
			convertView=View.inflate(context, R.layout.mainlistlayout, null);
			viewHolder=new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}else{                                          
			viewHolder=(ViewHolder)convertView.getTag();
		}
		viewHolder.text.setText(getString(position));
		viewHolder.img.setImageBitmap(getImage(position));
		return convertView;
	}
	static class ViewHolder{	
		private ImageView img;                
		private TextView text;
		public ViewHolder(View v){
			img=(ImageView)v.findViewById(R.id.main_listdetail_image);
			text=(TextView)v.findViewById(R.id.main_listdetail_text);
		}
	}
}