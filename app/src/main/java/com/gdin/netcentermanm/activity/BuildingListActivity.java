package com.gdin.netcentermanm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.gdin.netcentermanm.utils.ViewHolder;

import java.util.List;

public class BuildingListActivity extends Activity  {

    private ListView listView;
    private TextView tvAddBuilding;
    private List<AVObject> data;
    private ImageView imgReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);

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
        final String schoolPlaceId = getIntent().getStringExtra("placeId");
        final String placeName = getIntent().getStringExtra("placeName");
        listView = (ListView) findViewById(R.id.listView);
        tvAddBuilding = (TextView) findViewById(R.id.tv_add_building);
        AVObject place = new AVObject("school_place");
        place.setObjectId(schoolPlaceId);
        AVQuery.getQuery("buildings").whereEqualTo("place", place).setCachePolicy(AVQuery.CachePolicy.CACHE_THEN_NETWORK).findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(final List<AVObject> avObjects, AVException e) {
                if (e == null) {
                    data = avObjects;
                    listView.setAdapter(new BaseAdapter() {
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
                        public View getView(final int position, View convertView, ViewGroup parent) {
                            if (convertView == null) {
                                convertView = LayoutInflater.from(BuildingListActivity.this).inflate(R.layout.building_list_item,null);
                            }
                            TextView tvTitle = ViewHolder.get(convertView,R.id.tv_building_list_item_title);
                            TextView tvCheck = ViewHolder.get(convertView,R.id.tv_building_list_item_check);
                            tvTitle.setText(data.get(position).getString("name"));
                            tvCheck.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(BuildingListActivity.this,BuildingRepairListActivity.class);
                                    intent.putExtra("buildingId",data.get(position).getObjectId());
                                    startActivity(intent);
                                }
                            });
                            return convertView;
                        }
                    });
                } else {
                    Log.d("", e.toString());
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(BuildingListActivity.this, HouseListActivity.class);
                i.putExtra("buildingId",data.get(position).getObjectId());
                i.putExtra("buildingName",data.get(position).getString("name"));
                i.putExtra("placeName",placeName);
                startActivity(i);
            }
        });
        tvAddBuilding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(BuildingListActivity.this,AddBuildingActivity.class);
                intent.putExtra("placeId",schoolPlaceId);
                intent.putExtra("placeName",placeName);
                startActivity(intent);
            }
        });
        AVUser user = AVUser.getCurrentUser();
        if(user!=null&&user.getBoolean("managerRoot")){
            tvAddBuilding.setVisibility(View.VISIBLE);
        }else{
            tvAddBuilding.setVisibility(View.GONE);
        }
    }

}
