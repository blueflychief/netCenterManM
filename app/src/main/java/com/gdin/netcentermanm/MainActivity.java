package com.gdin.netcentermanm;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.gdin.netcentermanm.activity.LoginActivity;
import com.gdin.netcentermanm.activity.ManagerActivity;
import com.gdin.netcentermanm.activity.ManagerListActivity;
import com.gdin.netcentermanm.activity.SchoolPlaceActivity;
import com.gdin.netcentermanm.utils.Utils;
import com.gdin.netcentermanm.utils.ViewHolder;

import java.util.List;

public class MainActivity extends Activity {

    private PopupWindow popupWindow;
    private ImageView imgMenu;
    private ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        initView();
	}

    private void initView(){
        lv = (ListView) findViewById(R.id.lv_main_news);
        popupWindow = new PopupWindow(MainActivity.this);
        LinearLayout ll = (LinearLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.ll_man_menu,null);
        TextView tvEdit = (TextView) ll.findViewById(R.id.tv_main_menu_edit);
        TextView tvInfo = (TextView) ll.findViewById(R.id.tv_main_menu_info);
        TextView tvLogout = (TextView) ll.findViewById(R.id.tv_main_menu_logout);

        final TextView tvNews = (TextView) ll.findViewById(R.id.tv_main_menu_news);
        final TextView tvManagerList = (TextView) ll.findViewById(R.id.tv_main_menu_manager_list);
        popupWindow.setContentView(ll);
        imgMenu = (ImageView) findViewById(R.id.img_main_menu);
        popupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        //popupWindow.setBackgroundDrawable(null);
        imgMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AVUser user = AVUser.getCurrentUser();
                if(user==null){
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }else{
                    if(user.getBoolean("managerRoot")){
                        tvManagerList.setVisibility(View.VISIBLE);
                        tvNews.setVisibility(View.VISIBLE);
                    }else{
                        tvNews.setVisibility(View.GONE);
                        tvManagerList.setVisibility(View.GONE);
                    }
                    popupWindow.showAsDropDown(imgMenu);
                }
            }
        });
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVUser.getCurrentUser().logOut();
                popupWindow.dismiss();
            }
        });
        tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SchoolPlaceActivity.class));
                popupWindow.dismiss();
            }
        });
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ManagerActivity.class));
                popupWindow.dismiss();
            }
        });
        tvManagerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ManagerListActivity.class));
                popupWindow.dismiss();
            }
        });

        AVQuery.getQuery("managerNews").findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(final List<AVObject> avObjects, AVException e) {
                if(e==null){
                    lv.setAdapter(new BaseAdapter() {
                        @Override
                        public int getCount() {
                            return avObjects.size();
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
                            if(convertView==null){
                                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.rl_main_news_list_item,null);
                            }
                            TextView tvTitle = ViewHolder.get(convertView,R.id.tv_news_item_title);
                            TextView tvDate = ViewHolder.get(convertView,R.id.tv_news_item_date);
                            AVObject object = avObjects.get(position);
                            if(object.getBoolean("top")){
                                tvTitle.setTextColor(Color.parseColor("#FFA01815"));
                            }else{
                                tvTitle.setTextColor(Color.parseColor("#333333"));
                            }
                            tvTitle.setText(object.getString("title"));
                            tvDate.setText(Utils.date2String(object.getUpdatedAt(),"yy-MM-dd"));
                            return convertView;
                        }
                    });
                }
            }
        });
    }
}
