package com.gdin.netcentermanm.activity;

import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.dd.CircularProgressButton;
import com.gdin.netcentermanm.R;

public class AddSchoolPlaceActivity extends Activity  {

    private CircularProgressButton tvAddSchoolPlaceSubmit;
    private EditText etName;
    private ImageView imgReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_school_place);

        initView();
    }

    private void initView(){
        imgReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvAddSchoolPlaceSubmit = (CircularProgressButton) findViewById(R.id.tv_add_school_place_submit);
        etName = (EditText) findViewById(R.id.et_add_school_place_name);
        tvAddSchoolPlaceSubmit.setIndeterminateProgressMode(true);
        tvAddSchoolPlaceSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(AddSchoolPlaceActivity.this,"请填写校区名",Toast.LENGTH_LONG).show();
                    return ;
                }

                if(tvAddSchoolPlaceSubmit.getProgress()==50){
                    return;
                }
                tvAddSchoolPlaceSubmit.setIndeterminateProgressMode(true);
                tvAddSchoolPlaceSubmit.setClickable(false);
                AVObject avObject = new AVObject("school_place");
                avObject.put("name",name);
                avObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e==null){
                            tvAddSchoolPlaceSubmit.setProgress(100);
                            Toast.makeText(AddSchoolPlaceActivity.this,"添加成功",Toast.LENGTH_LONG).show();
                        }else{
                            tvAddSchoolPlaceSubmit.setProgress(-1);
                            Log.d("", e.toString());
                            Toast.makeText(AddSchoolPlaceActivity.this,"添加失败",Toast.LENGTH_LONG).show();
                        }
                        tvAddSchoolPlaceSubmit.setClickable(true);
                    }
                });
            }
        });
    }
}
