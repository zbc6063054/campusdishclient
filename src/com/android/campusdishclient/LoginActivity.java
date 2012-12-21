package com.android.campusdishclient;

import com.android.campusdishclient.util.BackgroundUtil;
import com.android.campusdishclient.util.BackgroundUtil.AftDeliver;
import com.android.campusdishclient.util.BackgroundUtil.Deliver;
import com.android.campusdishclient.util.BackgroundUtil.PreDeliver;
import com.android.campusdishclient.util.DialogUtil;
import com.android.campusdishclient.util.TotalUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.loginlayout);
		getViews();
	}

	private EditText mLoginText;
	private EditText mLoginPassword;
	private Button mbLogin;

	private void getViews() {
		mLoginText = (EditText) this.findViewById(R.id.login_number);
		mLoginPassword = (EditText) this.findViewById(R.id.login_password);
		mbLogin = (Button) this.findViewById(R.id.login_button);
		mbLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checkInput()){
					login();
				}
			}
		});
	}
    private boolean checkInput(){
    	if (mLoginText.getText().toString().equals("")){
    		TotalUtils.showToast(this, "请输入用户名！");
    		mLoginText.requestFocus();
    		return false;
    	}
    	if(mLoginPassword.getText().toString().equals("")){
    		TotalUtils.showToast(this, "请输入密码！");
    		mLoginPassword.requestFocus();
    		return false;
    	}
    	return true;
    }
	ProgressDialog mProgressDialog;

	private void login() {
		PreDeliver preDeliver = new PreDeliver() {

			@Override
			public void todo() {
				mProgressDialog = DialogUtil.showSpinnerProgressDialog(
						LoginActivity.this, "正在登录，请稍后。");
				mProgressDialog.show();
			}
		};
		Deliver deliver = new Deliver() {

			@Override
			public Object todo() {

				return false;
			}
		};
		AftDeliver aftDeliver = new AftDeliver() {

			@Override
			public void todo(Object result) {
				DialogUtil.cancelProgressDialog(mProgressDialog);
				// 登录成功
				if ((Boolean) result) {
					TotalUtils.showToast(LoginActivity.this, "登录成功！");
					LoginActivity.this.finish();
				} else {
				// 登录失败
					TotalUtils.showToast(LoginActivity.this, "登录失败！");
				}

			}
		};
		BackgroundUtil.doInBackGround(preDeliver, deliver, aftDeliver);

	}
}
