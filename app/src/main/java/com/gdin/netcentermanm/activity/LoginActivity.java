package com.gdin.netcentermanm.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.dd.CircularProgressButton;
import com.gdin.netcentermanm.MyApplication;
import com.gdin.netcentermanm.R;
import com.gdin.netcentermanm.R.layout;

public class LoginActivity extends Activity {
	private EditText etUserLoginStudentNum;
	private EditText etUserLoginPassword;
	private CircularProgressButton tvUserLoginSubmit;
    private ImageView imgReturn;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layout.user_login);
		initViews();
	}


	private void initViews() {
        imgReturn = (ImageView) findViewById(R.id.img_return);
        imgReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
		etUserLoginStudentNum = (EditText)findViewById( R.id.et_user_login_student_num );
		etUserLoginPassword = (EditText)findViewById( R.id.et_user_login_password );
		tvUserLoginSubmit = (CircularProgressButton) findViewById( R.id.tv_user_login_submit );

		tvUserLoginSubmit.setIndeterminateProgressMode(true);
		tvUserLoginSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String name = etUserLoginStudentNum.getText().toString();
				String pass = etUserLoginPassword.getText().toString();
				if(tvUserLoginSubmit.getProgress()==100){
					return ;
				}
				if(TextUtils.isEmpty(name)||TextUtils.isEmpty(pass)){
					Toast.makeText(LoginActivity.this,"请填写有效信息",Toast.LENGTH_LONG).show();
					return;
				}
				tvUserLoginSubmit.setProgress(50);
				AVUser.logInInBackground(name, pass, new LogInCallback<AVUser>() {
					@Override
					public void done(AVUser avUser, AVException e) {
						if(e==null){
                            if(!avUser.getBoolean("isManager")||!avUser.getBoolean("enable")){
                                Toast.makeText(LoginActivity.this,"无效的账号",Toast.LENGTH_LONG).show();
                               tvUserLoginSubmit.setProgress(-1);
                               return;
                            }
							tvUserLoginSubmit.setProgress(100);
							avUser.put("installationId", MyApplication.installationId);
							avUser.saveInBackground();
                            finish();
						}else{
							tvUserLoginSubmit.setProgress(-1);
							Log.d("avos",e.toString());
						}
					}
				});
			}
		});
	}

}
