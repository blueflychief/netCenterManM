package com.gdin.netcentermanm.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.dd.CircularProgressButton;
import com.gdin.netcentermanm.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManagerInfoActivity extends Activity  {

    private ImageView imgReturn;
    private TextView tvManagerInfoNum;
    private TextView tvManagerInfoName;
    private RadioGroup rgAddManagerRoot;
    private RadioButton rbManagerTrue;
    private RadioButton rbManagerFalse;
    private CircularProgressButton tvUserInfoSave;
    private RadioGroup rgAddManagerEnable;
    private RadioButton rbEnableTrue;
    private RadioButton rbEnableFalse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_info);

        initView();
    }
    private void initView(){
        Bundle bundle = getIntent().getExtras();
        final String id = bundle.getString("id");
        String name = bundle.getString("name");
        String sName = bundle.getString("sName");
        boolean isRoot = bundle.getBoolean("isRoot");
        boolean enable = bundle.getBoolean("enable");
        imgReturn = (ImageView) findViewById(R.id.img_return);
        imgReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvManagerInfoNum = (TextView)findViewById( R.id.tv_manager_info_num );
        tvManagerInfoName = (TextView)findViewById( R.id.tv_manager_info_name );
        rgAddManagerRoot = (RadioGroup)findViewById( R.id.rg_add_manager_root );
        rbManagerTrue = (RadioButton)findViewById( R.id.rb_manager_true );
        rbManagerFalse = (RadioButton)findViewById( R.id.rb_manager_false );
        tvUserInfoSave = (CircularProgressButton)findViewById( R.id.tv_user_info_save );
        rgAddManagerEnable = (RadioGroup)findViewById( R.id.rg_add_manager_enable );
        rbEnableTrue = (RadioButton)findViewById( R.id.rb_enable_true );
        rbEnableFalse = (RadioButton)findViewById( R.id.rb_enable_false );
        tvManagerInfoNum.setText(name);
        tvManagerInfoName.setText(sName);
        if(isRoot){
            rbManagerTrue.setChecked(true);
        }else{
            rbManagerFalse.setChecked(true);
        }
        if(enable){
            rbEnableTrue.setChecked(true);
        }else{
            rbEnableFalse.setChecked(true);
        }
        tvUserInfoSave.setIndeterminateProgressMode(true);
        tvUserInfoSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AVUser currentUser = AVUser.getCurrentUser();
                tvUserInfoSave.setProgress(50);
                AVUser user = new AVUser();
                user.setObjectId(id);
                switch (rgAddManagerEnable.getCheckedRadioButtonId()){
                    case  R.id.rb_enable_true :
                        user.put("enable",true);
                    break;
                    case  R.id.rb_enable_false :
                        user.put("enable",false);
                    break;
                }

                switch (rgAddManagerRoot.getCheckedRadioButtonId()){
                    case  R.id.rb_manager_true :
                        user.put("managerRoot",true);
                        break;
                    case  R.id.rb_manager_false :
                        user.put("managerRoot",false);
                        break;
                }

                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e==null){
                            tvUserInfoSave.setProgress(100);
                        }else{
                            tvUserInfoSave.setProgress(-1);
                        }
                        AVUser.getCurrentUser().changeCurrentUser(currentUser,true);
                    }
                });

            }
        });

    }

}
