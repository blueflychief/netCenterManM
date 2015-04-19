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
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.dd.CircularProgressButton;
import com.gdin.netcentermanm.R;

import java.util.ArrayList;
import java.util.List;

public class AddManagerActivity extends Activity {

    private CircularProgressButton tvAddManager;
    private EditText etName;
    private EditText etPass;
    private EditText etUserName;
    private ImageView imgReturn;
    private RadioGroup rgRoot;
    private AVUser user = AVUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manager);

       initView();
    }

    private void initView(){
        rgRoot = (RadioGroup) findViewById(R.id.rg_add_manager_root);
        imgReturn = (ImageView) findViewById(R.id.img_return);
        imgReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvAddManager = (CircularProgressButton) findViewById(R.id.tv_add_manager);
        etName = (EditText) findViewById(R.id.et_manager_name);
        etPass = (EditText) findViewById(R.id.et_manager_pass);
        etUserName = (EditText) findViewById(R.id.et_manager_user_name);

        tvAddManager.setIndeterminateProgressMode(true);
        tvAddManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String pass = etPass.getText().toString();
                String userName = etUserName.getText().toString();
                if(TextUtils.isEmpty(name)||TextUtils.isEmpty(pass)||TextUtils.isEmpty(userName)){
                    Toast.makeText(AddManagerActivity.this,"请填写完整资料",Toast.LENGTH_LONG).show();
                    return ;
                }
                //tvAddManager.setClickable(false);
                tvAddManager.setProgress(50);
                AVUser manager = new AVUser();
                manager.setUsername(name);
                manager.setPassword(pass);
                manager.put("sName",userName);
                manager.put("isManager",true);
                int id = rgRoot.getCheckedRadioButtonId();
                switch (id){
                    case R.id.rb_manager_true:
                        manager.put("managerRoot",true);
                        break;
                    case R.id.rb_manager_false:
                        manager.put("managerRoot",false);
                        break;
                }

               /* manager.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            tvAddManager.setProgress(100);
                           // AVUser.logOut();
                            Toast.makeText(AddManagerActivity.this, "添加成功", Toast.LENGTH_LONG).show();
                        } else {
                            tvAddManager.setProgress(100);
                            Toast.makeText(AddManagerActivity.this, "添加失败", Toast.LENGTH_LONG).show();
                            Log.d("", e.toString());
                        }
                        //tvAddManager.setClickable(true);
                    }
                });*/
                manager.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            tvAddManager.setProgress(100);
                            Log.d("11",""+ AVUser.getCurrentUser().getUsername());
                            Toast.makeText(AddManagerActivity.this, "添加成功", Toast.LENGTH_LONG).show();
                            AVUser.getCurrentUser().changeCurrentUser(user,true);
                            Log.d("22",""+ AVUser.getCurrentUser().getUsername());
                        } else {
                            tvAddManager.setProgress(100);
                            Toast.makeText(AddManagerActivity.this, "添加失败", Toast.LENGTH_LONG).show();
                            Log.d("", e.toString());
                        }
                    }
                });

            }
        });
    }



}
