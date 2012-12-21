package com.android.campusdishclient;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.android.campusdishclient.data.Cart;
import com.android.campusdishclient.data.Dish;
import com.android.campusdishclient.util.BackgroundUtil;
import com.android.campusdishclient.util.DialogUtil;
import com.android.campusdishclient.util.HttpUtil;
import com.android.campusdishclient.util.TotalUtils;
import com.android.campusdishclient.util.BackgroundUtil.AftDeliver;
import com.android.campusdishclient.util.BackgroundUtil.Deliver;
import com.android.campusdishclient.util.BackgroundUtil.PreDeliver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ShowCartActivity extends Activity {

	private int mSelectDishId = -1;
	private MyAdapter mApapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.showcartlayout);
		findViews();

	}

	private ListView list_main;
	private TextView text_showPrice;
	private Button btn_submit;
	
	private EditText text_inputTableNumber;
	private TextView text_orderContent;
	private int tableNum = -1;
	private ProgressDialog mProgressDialog;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, MENU_RESETCART, 0, "清空菜单");
//		menu.add(0,MENU_SETTODEFAULT,0,"设为默认菜单");
//		menu.add(0,MENU_GETDEFAULT,0,"使用默认菜单");
//		menu.add
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case MENU_RESETCART:
			DialogUtil.showAffirmDialog(ShowCartActivity.this, "确认", "确认清空菜单？",
					true, new DialogUtil.Handler() {
						@Override
						public void todo(DialogInterface dialog, int which) {
							Cart.getCart().reset();
							TotalUtils.showToast(ShowCartActivity.this,
									"清空菜单成功！");
							refresh();
						}
					});
			break;
		}
		return true;
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("选择操作");
		menu.add(0, MENU_DELETEDISH, 0, "删除");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		super.onContextItemSelected(item);
		switch (item.getItemId()) {
		case MENU_DELETEDISH:
			if (Cart.getCart().deleteDish(mSelectDishId)) {
				TotalUtils.showToast(ShowCartActivity.this, "删除成功！");
			} else {
				TotalUtils.showToast(ShowCartActivity.this, "删除失败！");
			}
			refresh();
			break;
		case MENU_GETDEFAULT:
			
			break;
		case MENU_SETTODEFAULT:
			break;
		}
		return true;
	}

	public void refresh() {
		text_showPrice.setText("￥" + String.valueOf(Cart.getCart().getPrice()));
		mApapter.refreshList();
	}

	private void findViews() {
		list_main = (ListView) findViewById(R.id.showcart_mainList);
		list_main.setTextFilterEnabled(true);

		list_main.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				mSelectDishId = (int) id;
				return false;
			}
		});
		btn_submit = (Button) findViewById(R.id.showcart_button_submit);
		btn_submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (checkIsNothing()) {
					TotalUtils.showToast(ShowCartActivity.this, "未选择任何商品！");
					return;
				}
				doSubmit();
			}
		});
		text_showPrice = (TextView) findViewById(R.id.showcart_text_price);
		text_orderContent=(TextView) findViewById(R.id.showcart_text_comment);
		text_orderContent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				setContent();
			}
		});
		text_orderContent.setText(Cart.getCart().getContent().trim().equals("")?"无":Cart.getCart().getContent().trim());
		mApapter = new MyAdapter(this);
		list_main.setAdapter(mApapter);
		refresh();
		registerForContextMenu(list_main);
	}

	private void setContent(){
		final EditText text=new EditText(this);
		text.setMaxLines(5);

		String str=text_orderContent.getText().toString().trim();
		text.setText(str.equals("无")?"":str);
		DialogUtil.showViewDialog(this, "备注", true, text, new DialogUtil.Handler() {
			
			@Override
			public void todo(DialogInterface dialog, int which) {
				String str=text.getText().toString().trim();
				text_orderContent.setText(str.equals("")?"无":str);
				Cart.getCart().setContent(str.equals("")?"无":str);
			}
		});
	}
	
	private void startSubmit(final int pTableNum) {

		PreDeliver preDeliver = new PreDeliver() {

			@Override
			public void todo() {
				mProgressDialog = DialogUtil.showSpinnerProgressDialog(
						ShowCartActivity.this, "正在提交，请稍后。");
				mProgressDialog.show();
			}
		};
		Deliver deliver = new Deliver() {

			@Override
			public Object todo() {
				try {
					Map<String,String> map=Cart.getCart().getMap();
					map.put("tablenumber", String.valueOf(pTableNum));
					String s = HttpUtil.postRequest(HttpUtil.BASE_URL
							+ "AddOrder.aspx", map);
					if (s.toLowerCase().indexOf("false")>0) {
						return false;
					}
				} catch (ClientProtocolException e) {

					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
				return true ;
			}
		};
		AftDeliver aftDeliver = new AftDeliver() {

			@Override
			public void todo(Object result) {
				DialogUtil.cancelProgressDialog(mProgressDialog);
				// 登录成功
				if ((Boolean) result) {
					TotalUtils.showToast(ShowCartActivity.this, "提交成功！");
					//提交完成清空菜单
					Cart.getCart().reset();
					refresh();
				} else {
					// 登录失败
					TotalUtils.showToast(ShowCartActivity.this, "提交失败！");
				}
			}
		};
		BackgroundUtil.doInBackGround(preDeliver, deliver, aftDeliver);
	}
	private void doSubmit() {
		LinearLayout l1=new LinearLayout(this);
		l1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 120));
		l1.setOrientation(LinearLayout.VERTICAL);
		text_inputTableNumber=new EditText(this);
		text_inputTableNumber.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
		text_inputTableNumber.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		l1.addView(text_inputTableNumber);
		DialogUtil.showViewDialog(this, "请输入桌号", true, l1,
				new DialogUtil.Handler() {
					@Override
					public void todo(DialogInterface dialog, int which) {
						String s=text_inputTableNumber.getText().toString();
						if(s.equals("")){
							TotalUtils.showToast(ShowCartActivity.this, "请输入桌号！");
						}
						try{
							tableNum=Integer.parseInt(s);
						}catch (NumberFormatException e){
							TotalUtils.showToast(ShowCartActivity.this, "请输入数字！");
							return;
						}
						startSubmit(tableNum);
					}
				});
		
	}

	private boolean checkIsNothing() {
		if (Cart.getCart().getCartSize() == 0) {
			return true;
		}
		return false;
	}

	private boolean checkIsOnLine() {
		return false;
	}
	private static final int MENU_RESETCART = Menu.FIRST;
	private static final int MENU_DELETEDISH = Menu.FIRST + 1;
	private static final int MENU_SETTODEFAULT = Menu.FIRST + 2;
	private static final int MENU_GETDEFAULT = Menu.FIRST + 3;
//	private static final int MENU_ADDCONTENT
}



class MyAdapter extends BaseAdapter {
	private Dish[] mDish;
	private Context mContext;
	private DecimalFormat df;

	public MyAdapter(Context context) {
		mContext = context;
		df = new DecimalFormat("0.##");
		refreshList();
	}

	public void refreshList() {
		mDish = Cart.getCart().getAllDish();

		this.notifyDataSetChanged();
	}

	public int getCount() {

		return mDish.length;
	}

	@Override
	public Object getItem(int position) {

		return null;
	}

	@Override
	public long getItemId(int position) {

		return mDish[position].getDishID();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder v;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.showcart_list_item,
					null);
			v = new ViewHolder(convertView);
			convertView.setTag(v);
		} else {
			v = (ViewHolder) convertView.getTag();
		}
		v.text_dishName.setText(mDish[position].getDishName());
		v.text_dishPrice.setText("￥" + df.format(mDish[position].getPrice()));
		return convertView;
	}

	class ViewHolder {
		private TextView text_dishName, text_dishPrice;

		private ViewHolder(View v) {
			text_dishName = (TextView) v
					.findViewById(R.id.showcart_list_text_dishname);
			text_dishPrice = (TextView) v
					.findViewById(R.id.showcart_list_text_dishprice);
		}
	}
}
