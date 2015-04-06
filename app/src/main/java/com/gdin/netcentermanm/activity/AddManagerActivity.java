package com.gdin.netcentermanm.activity;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.gdin.netcentermanm.R;

import java.util.ArrayList;
import java.util.List;

public class AddManagerActivity extends Activity {

    private TextView tvAddManager;
    private EditText etName;
    private EditText etPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manager);

       initView();
    }

    private void initView(){
        tvAddManager = (TextView) findViewById(R.id.tv_add_manager);
        etName = (EditText) findViewById(R.id.et_manager_name);
        etPass = (EditText) findViewById(R.id.et_manager_pass);

        tvAddManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String pass = etPass.getText().toString();
                if(TextUtils.isEmpty(name)||TextUtils.isEmpty(pass)){
                    Toast.makeText(AddManagerActivity.this,"请填写完整资料",Toast.LENGTH_LONG).show();
                    return ;
                }
                tvAddManager.setClickable(false);
                AVUser manager = new AVUser();
                manager.setUsername(name);
                manager.setPassword(pass);
                manager.put("isManager",true);
                manager.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                           // AVUser.logOut();
                            Toast.makeText(AddManagerActivity.this, "添加成功", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(AddManagerActivity.this, "添加失败", Toast.LENGTH_LONG).show();
                            Log.d("", e.toString());
                        }
                        tvAddManager.setClickable(true);
                    }
                });

            }
        });
    }



}
