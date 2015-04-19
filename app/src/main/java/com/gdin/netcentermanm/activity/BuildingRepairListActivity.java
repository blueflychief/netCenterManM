package com.gdin.netcentermanm.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.gdin.netcentermanm.R;
import com.gdin.netcentermanm.utils.Utils;
import com.gdin.netcentermanm.utils.ViewHolder;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BuildingRepairListActivity extends Activity {

    private PullToRefreshListView refreshListView;
    private ListView listView;
    private BaseAdapter adapter;
    private List<AVObject> data = new ArrayList<>(0);
    private AVQuery query;
    private ImageView imgReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_repair_list);
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
        final String buildingId = getIntent().getExtras().getString("buildingId");
        refreshListView = (PullToRefreshListView) findViewById(R.id.lv_building_repair);

        //listView = refreshListView.getRefreshableView();
        AVObject building = new AVObject("buildings");
        building.setObjectId(buildingId);
        AVQuery inQuery = AVUser.getQuery();
        inQuery.whereEqualTo("building", building);
        AVUser.getQuery().whereEqualTo("building", building).findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {

            }
        });
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
                if (convertView == null) {
                    convertView = LayoutInflater.from(BuildingRepairListActivity.this).inflate(R.layout.my_repair_item, null);
                }
                TextView num = ViewHolder.get(convertView, R.id.tv_my_repair_num);
                TextView title = ViewHolder.get(convertView, R.id.tv_my_repair_title);
                TextView date = ViewHolder.get(convertView, R.id.tv_my_repair_date);
                AVObject repair = data.get(position);
                num.setText((position + 1) + "");
                title.setText(repair.getString("title"));
                date.setText(Utils.date2String(repair.getCreatedAt()));

                return convertView;
            }
        };
        query = AVQuery.getQuery("repair").whereMatchesQuery("user", inQuery);
        //query = AVQuery.getQuery("repair").whereEqualTo();
        refreshListView.setAdapter(adapter);
        refresh();
        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                refresh();
            }
        });
        refreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AVObject repair = data.get(position-1);
                String avId = repair.getObjectId();
              /*  String title = repair.getString("title");
                String reason = repair.getString("reason");
                String appointTime = repair.getString("appointTime");
                AVUser user = repair.getAVUser("user");
                String userName = user.getString("sName");
                AVUser managerUser = repair.getAVUser("manager");
                AVObject building = user.getAVObject("building");
                String house = user.getString("house");
                String buildingName = building.getString("name");
                String managerName = "";
                if(managerUser!=null){
                    managerName = managerUser.getString("sName");
                }*/
                Bundle bundle = new Bundle();
                bundle.putString("avId",avId);
               /* bundle.putString("title",title);
                bundle.putString("userName",userName);
                bundle.putString("buildingAvId",buildingId);
                bundle.putString("reason",reason);
                bundle.putString("appointTime",appointTime);
                bundle.putString("house",house);
                bundle.putString("buildingName",buildingName);
                bundle.putString("managerName",managerName);
                List<String> list = building.getList("managerUsers");
                ArrayList al = new ArrayList<String>();
                for(String s : list){
                    al.add(s);
                }
                bundle.putStringArrayList("managerUsers",al);*/
                Intent i = new Intent(BuildingRepairListActivity.this,CheckRepairActivity.class);
                i.putExtras(bundle);
                startActivity(i);

            }
        });
    }

    private void refresh(){
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException e) {
                if (e == null && avObjects != null && !avObjects.isEmpty()) {
                    data = avObjects;
                    adapter.notifyDataSetChanged();
                    //listView.setAdapter(adapter);
                }
                if(e!=null){
                    Log.d("",e.toString());
                }
                refreshListView.onRefreshComplete();
            }
        });
    }


}
