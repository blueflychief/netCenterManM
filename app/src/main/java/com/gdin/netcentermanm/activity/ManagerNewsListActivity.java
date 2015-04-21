package com.gdin.netcentermanm.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
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
import com.gdin.netcentermanm.utils.Utils;
import com.gdin.netcentermanm.utils.ViewHolder;

import java.util.List;

public class ManagerNewsListActivity extends Activity {

    private ImageView imgReturn;
    private ImageView imgAdd;
    private ListView lv;
    private List<AVObject> data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_news_list);

       initView();
    }

    private void initView(){
        imgReturn = (ImageView)findViewById( R.id.img_return );
        imgReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgAdd = (ImageView) findViewById(R.id.img_add);
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManagerNewsListActivity.this,AddManagerNewsActivity.class));
            }
        });

        lv = (ListView) findViewById(R.id.lv_manager_news_list);

    }


    @Override
    protected void onResume() {
        super.onResume();
        AVQuery.getQuery("managerNews").findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException e) {
                if (e == null){
                    data = avObjects;
                    lv.setAdapter(new BaseAdapter() {
                        @Override
                        public int getCount() {
                            return data.size();
                        }

                        @Override
                        public Object getItem(int position) {
                            return null;
                        }

                        @Override
                        public long getItemId(int position) {
                            return 0;
                        }

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            if (convertView == null) {
                                convertView = LayoutInflater.from(ManagerNewsListActivity.this).inflate(R.layout.rl_main_news_list_item, null);
                            }
                            TextView tvTitle = ViewHolder.get(convertView, R.id.tv_news_item_title);
                            TextView tvDate = ViewHolder.get(convertView, R.id.tv_news_item_date);
                            final AVObject object = data.get(position);
                            if (object.getBoolean("top")) {
                                tvTitle.setTextColor(Color.parseColor("#FFA01815"));
                            } else {
                                tvTitle.setTextColor(Color.parseColor("#333333"));
                            }
                            if (object.getBoolean("enable")) {
                                tvTitle.setTextColor(Color.parseColor("#FFDCDCDC"));
                            } else {
                                tvTitle.setTextColor(Color.parseColor("#333333"));
                            }
                            tvTitle.setText(object.getString("title"));
                            tvDate.setText(Utils.date2String(object.getUpdatedAt(), "yy-MM-dd"));
                            convertView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("id", object.getObjectId());
                                    bundle.putString("title", object.getString("title"));
                                    bundle.putString("content", object.getString("content"));
                                    bundle.putBoolean("enable", object.getBoolean("enable"));
                                    bundle.putBoolean("top", object.getBoolean("top"));
                                    Intent intent = new Intent(ManagerNewsListActivity.this, ManagerNewsInfoActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            });
                            return convertView;
                        }
                    });
                }
            }
        });
    }
}
