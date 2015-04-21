package com.gdin.netcentermanm.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.dd.CircularProgressButton;
import com.gdin.netcentermanm.R;

public class ManagerNewsInfoActivity extends Activity  {

    private ImageView imgReturn;
    private EditText etManagerNewsInfoTitle;
    private RadioGroup rgManagerNewsTop;
    private RadioButton rbManagerNewsTopTrue;
    private RadioButton rbManagerNewsTopFalse;
    private RadioGroup rgManagerNewsEnable;
    private RadioButton rbManagerNewsEnableTrue;
    private RadioButton rbManagerNewsEnableFalse;
    private EditText etManagerNewsInfoContent;
    private CircularProgressButton cpbManagerNewsCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_news_info);

        initView();
    }
    private void initView(){
        Bundle bundle = getIntent().getExtras();
        final String id = bundle.getString("id");
        String title = bundle.getString("title");
        String content = bundle.getString("content");
        boolean top = bundle.getBoolean("top");
        boolean enable = bundle.getBoolean("enable");
        imgReturn = (ImageView) findViewById(R.id.img_return);
        imgReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etManagerNewsInfoTitle = (EditText)findViewById( R.id.et_manager_news_info_title );
        rgManagerNewsTop = (RadioGroup)findViewById( R.id.rg_manager_news_top );
        rbManagerNewsTopTrue = (RadioButton)findViewById( R.id.rb_manager_news_top_true );
        rbManagerNewsTopFalse = (RadioButton)findViewById( R.id.rb_manager_news_top_false );
        rgManagerNewsEnable = (RadioGroup)findViewById( R.id.rg_manager_news_enable );
        rbManagerNewsEnableTrue = (RadioButton)findViewById( R.id.rb_manager_news_enable_true );
        rbManagerNewsEnableFalse = (RadioButton)findViewById( R.id.rb_manager_news_enable_false );
        etManagerNewsInfoContent = (EditText)findViewById( R.id.et_manager_news_info_content );
        cpbManagerNewsCommit = (CircularProgressButton)findViewById( R.id.cpb_manager_news_commit );
        rgManagerNewsEnable.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                cpbManagerNewsCommit.setVisibility(View.VISIBLE);
            }
        });
        rgManagerNewsTop.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                cpbManagerNewsCommit.setVisibility(View.VISIBLE);
            }
        });
        etManagerNewsInfoTitle.setText(title);
        etManagerNewsInfoContent.setText(content);
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                cpbManagerNewsCommit.setVisibility(View.VISIBLE);
            }
        };
        etManagerNewsInfoContent.addTextChangedListener(watcher);
        etManagerNewsInfoTitle.addTextChangedListener(watcher);

        if(top){
            rbManagerNewsTopTrue.setChecked(true);
        }else{
            rbManagerNewsTopFalse.setChecked(true);
        }
        if(enable){
            rbManagerNewsEnableTrue.setChecked(true);
        }else{
            rbManagerNewsEnableFalse.setChecked(true);
        }
        cpbManagerNewsCommit.setIndeterminateProgressMode(true);
        cpbManagerNewsCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etManagerNewsInfoTitle.getText().toString();
                String content = etManagerNewsInfoContent.getText().toString();
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
                    Toast.makeText(ManagerNewsInfoActivity.this, "信息不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                AVObject object = new AVObject("managerNews");
                object.setObjectId(id);
                cpbManagerNewsCommit.setProgress(50);
                switch (rgManagerNewsEnable.getCheckedRadioButtonId()) {
                    case R.id.rb_manager_news_enable_true:
                        object.put("enable", true);
                        break;
                    case R.id.rb_manager_news_enable_false:
                        object.put("enable", false);
                        break;
                }

                switch (rgManagerNewsTop.getCheckedRadioButtonId()) {
                    case R.id.rb_manager_news_top_true:
                        object.put("top", true);
                        break;
                    case R.id.rb_manager_news_top_false:
                        object.put("top", false);
                        break;
                }
                object.put("title", title);
                object.put("content", content);

                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            cpbManagerNewsCommit.setProgress(100);
                        } else {
                            cpbManagerNewsCommit.setProgress(-1);
                        }
                    }
                });

            }
        });

    }

}
