package com.gdin.netcentermanm.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.gdin.netcentermanm.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class HouseListActivity extends Activity  {

    private ListView lvHouse;
    private TextView tvAddHouse;
    private TextView tvManager;
    private BaseAdapter adapter;
    private List<AVObject> data = new ArrayList<>();
    private AlertDialog.Builder builder;
    private List<AVUser> userData;
    private String[] names;
    private boolean[] isCheckeds;
    private String[] objId;
    private TextView tvCheckRepair;
    private ImageView imgReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_list);

        initView();
    }
    private void initView(){
        imgReturn = (ImageView) findViewById(R.id.img_return);
        imgReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        builder = new AlertDialog.Builder(HouseListActivity.this);
        final String placeName = getIntent().getStringExtra("placeName");
        final String buildingId = getIntent().getStringExtra("buildingId");
        final String buildingName = getIntent().getStringExtra("buildingName");
        Log.d("获取到的数据",placeName+"   "+buildingId+"    "+buildingName);
        lvHouse = (ListView) findViewById(R.id.lv_house);
        tvAddHouse = (TextView) findViewById(R.id.tv_add_house);
        tvManager = (TextView) findViewById(R.id.tv_add_manager);
        tvCheckRepair = (TextView) findViewById(R.id.tv_check_repair);
        AVObject building = new AVObject("buildings");
        building.setObjectId(buildingId);
        adapter = new BaseAdapter() {
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
                TextView tv = null;
                if(convertView!=null){
                    tv = (TextView)convertView;
                }else{
                    tv = new TextView(HouseListActivity.this);
                }
                tv.setTextSize(28);
                tv.setText(data.get(position).getString("name"));
                return tv;
            }
        };
        lvHouse.setAdapter(adapter);
        AVQuery.getQuery("houses").whereEqualTo("building", building).setCachePolicy(AVQuery.CachePolicy.CACHE_THEN_NETWORK).findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException e) {
                if (e == null&&avObjects!=null) {
                    Log.d("houses size",avObjects.size()+"");
                    data = avObjects;
                    adapter.notifyDataSetChanged();
                }else{
                    if(e!=null)Log.d("",e.toString());
                }
            }
        });

        tvAddHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HouseListActivity.this,AddHouseActivity.class);
                intent.putExtra("buildingId", buildingId);
                intent.putExtra("buildingName", buildingName);
                intent.putExtra("placeName", placeName);
                startActivity(intent);
            }
        });

        AVUser user = AVUser.getCurrentUser();
        if(user!=null&&user.getBoolean("managerRoot")){
            tvAddHouse.setVisibility(View.VISIBLE);
            tvManager.setVisibility(View.VISIBLE);
        }else{
            tvAddHouse.setVisibility(View.GONE);
            tvManager.setVisibility(View.GONE);
        }

/*        AVUser.getQuery().whereEqualTo("isManager",true).include("buildings").findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> avUsers, AVException e) {
                if (e == null) {
                    Log.d("获取的数据", "avUsers  size:" + avUsers.size());
                    userData = avUsers;
                    names = new String[userData.size()];
                    isCheckeds = new boolean[userData.size()];
                    for (int i = 0; i < userData.size(); i++) {
                        AVUser user = userData.get(i);
                        names[i] = user.getUsername();
                        if (user.getList("buildings") != null && user.getList("buildings").contains(buildingId)) {
                            isCheckeds[i] = true;
                        }
                    }
                }
            }
        });*/
        AVQuery.getQuery("buildings").whereEqualTo("objectId",buildingId).findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(final List<AVObject> avObjects, AVException e) {
                if(e==null&&!avObjects.isEmpty()){
                    AVObject building = avObjects.get(0);
                    final List list = building.getList("managerUsers");
                    AVUser.getQuery().whereEqualTo("isManager", true).findInBackground(new FindCallback<AVUser>() {
                        @Override
                        public void done(List<AVUser> avUsers, AVException e) {
                            if(e==null){
                                names = new String[avUsers.size()];
                                isCheckeds = new boolean[avUsers.size()];
                                objId = new String[avUsers.size()];
                                for(int i = 0;i<avUsers.size();i++){
                                    AVUser user = avUsers.get(i);
                                    names[i] = user.getUsername();
                                    objId[i] = user.getObjectId();
                                    if(list!=null&&list.contains(user.getObjectId())){
                                        isCheckeds[i] = true;
                                    }
                                }
                                Log.d("",Arrays.toString(names)+ "  "+Arrays.toString(isCheckeds));
                                tvManager.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        });

        tvManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(HouseListActivity.this).setTitle("请指定网管").setMultiChoiceItems(names, isCheckeds, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        isCheckeds[which] = isChecked;
                    }
                }).setNegativeButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List d = new ArrayList();
                        for(int i = 0;i<isCheckeds.length;i++){
                            if(isCheckeds[i]){
                                d.add(objId[i]);
                            }
                        }
                        AVObject building = new AVObject("buildings");
                        building.setObjectId(buildingId);
                        building.put("managerUsers",d);
                        building.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if(e==null){
                                    Toast.makeText(HouseListActivity.this,"修改成功",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create().show();
            }
        });

    }

}
