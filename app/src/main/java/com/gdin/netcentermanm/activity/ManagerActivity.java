package com.gdin.netcentermanm.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.dd.CircularProgressButton;
import com.gdin.netcentermanm.MainActivity;
import com.gdin.netcentermanm.R;

public class ManagerActivity extends Activity {
    private ImageView imgReturn;
    private TextView tvManagerName;
    private EditText etManagerPassword;
    private Switch switchPassword;
    private CircularProgressButton tvManagerSubmit;

    public ManagerActivity() {
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manager);
        initView();
	}

    private void initView(){
        AVUser user = AVUser.getCurrentUser();
        imgReturn = (ImageView)findViewById( R.id.img_return );
        tvManagerName = (TextView)findViewById( R.id.tv_manager_name );
        etManagerPassword = (EditText)findViewById( R.id.et_manager_password );
        switchPassword = (Switch)findViewById( R.id.switch_password );
        tvManagerSubmit = (CircularProgressButton)findViewById( R.id.tv_manager_submit );

        tvManagerName.setText(user.getUsername());

        tvManagerSubmit.setIndeterminateProgressMode(true);
        imgReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        switchPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("dddddd", ""+isChecked);
                if(isChecked){
                    etManagerPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    etManagerPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        etManagerPassword.addTextChangedListener(new TextWatcher() {

            String pass = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                pass = etManagerPassword.getText().toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!pass.equals(etManagerPassword.getText().toString())){
                    tvManagerSubmit.setVisibility(View.VISIBLE);
                    tvManagerSubmit.setProgress(0);
                }
            }
        });


        tvManagerSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvManagerSubmit.getProgress()==-1){
                    return;

                }

                String pass = etManagerPassword.getText().toString();
                if(TextUtils.isEmpty(pass)){
                    Toast.makeText(ManagerActivity.this,"密码不能为空",Toast.LENGTH_LONG).show();
                    return;
                }
                if(pass.length()<6){
                    Toast.makeText(ManagerActivity.this,"请输入长度大于6位的密码",Toast.LENGTH_LONG).show();
                }

                AVUser user = AVUser.getCurrentUser();
                if(user==null){
                    Toast.makeText(ManagerActivity.this,"账号无效，请退出重新登录",Toast.LENGTH_LONG).show();
                }else{
                    tvManagerSubmit.setProgress(50);
                    user.setPassword(pass);
                    user.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            tvManagerSubmit.setProgress(100);
                        }
                    });
                }

            }
        });

    }
}
