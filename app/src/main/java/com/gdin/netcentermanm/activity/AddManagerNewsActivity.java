package com.gdin.netcentermanm.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.dd.CircularProgressButton;
import com.gdin.netcentermanm.R;

public class AddManagerNewsActivity extends Activity {

    private ImageView imgReturn;
    private EditText etManagerNewsTitle;
    private RadioGroup rgAddManagerNewsTop;
    private RadioButton rbTopTrue;
    private RadioButton rbTopFalse;
    private EditText etManagerNewsContent;
    private CircularProgressButton tvAddManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manager_news);

       initView();
    }

    private void initView(){
        imgReturn = (ImageView)findViewById( R.id.img_return );
        etManagerNewsTitle = (EditText)findViewById( R.id.et_manager_news_title );
        rgAddManagerNewsTop = (RadioGroup)findViewById( R.id.rg_add_manager_news_top );
        rbTopTrue = (RadioButton)findViewById( R.id.rb_top_true );
        rbTopFalse = (RadioButton)findViewById( R.id.rb_top_false );
        etManagerNewsContent = (EditText)findViewById( R.id.et_manager_news_content );
        tvAddManager = (CircularProgressButton)findViewById( R.id.tv_add_manager );

        imgReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvAddManager.setIndeterminateProgressMode(true);
        tvAddManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvAddManager.getProgress()==50){
                    return;
                }
                AVObject object = new AVObject("managerNews");
                String title = etManagerNewsTitle.getText().toString();
                String content = etManagerNewsContent.getText().toString();
                if(TextUtils.isEmpty(title)||TextUtils.isEmpty(content)){
                    Toast.makeText(AddManagerNewsActivity.this,"请填写完整信息",Toast.LENGTH_LONG).show();
                    return;
                }
                tvAddManager.setProgress(50);
                object.put("title",title);
                object.put("content",content);
                object.put("createUser",AVUser.getCurrentUser());
                if(rbTopFalse.isChecked()){
                    object.put("top",false);
                }else{
                    object.put("top",true);
                }
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e==null){
                            tvAddManager.setProgress(100);
                        }else{
                            tvAddManager.setProgress(-1);
                        }
                    }
                });
            }
        });

    }



}
