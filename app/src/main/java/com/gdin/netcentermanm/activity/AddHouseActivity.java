package com.gdin.netcentermanm.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.dd.CircularProgressButton;
import com.gdin.netcentermanm.R;

import java.util.ArrayList;
import java.util.List;

public class AddHouseActivity extends Activity  {

    private TextView tvPlaceName;
    private TextView tvBuildingName;
    private CircularProgressButton tvAddHouseSubmit;
    private EditText etHouseName;

   // private BaseAdapter placeAdapter;
   // private List<AVObject> placeData = new ArrayList<>();
    //private List<AVObject> buildingData = new ArrayList<>();
   // private BaseAdapter buildingAdapter;

    private int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_house);

        initView();
    }

    private void initView(){
        final String buildingId = getIntent().getStringExtra("buildingId");
        final String buildingName = getIntent().getStringExtra("buildingName");
        final String placeName = getIntent().getStringExtra("placeName");
        tvPlaceName = (TextView) findViewById(R.id.tv_place_name);
        tvBuildingName = (TextView) findViewById(R.id.tv_building_name);
        tvAddHouseSubmit = (CircularProgressButton) findViewById(R.id.tv_add_house_submit);
        etHouseName = (EditText) findViewById(R.id.et_house_name);

        tvBuildingName.setText(buildingName);
        tvPlaceName.setText(placeName);
        /*AVQuery.getQuery("school_place").setCachePolicy(AVQuery.CachePolicy.CACHE_THEN_NETWORK).findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException e) {
                if(e==null){
                    placeData = avObjects;
                    placeAdapter.notifyDataSetChanged();
                }
            }
        });
        tvPlaceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AddHouseActivity.this).setAdapter(placeAdapter,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvPlaceName.setText(placeData.get(which).getString("name"));
                        AVQuery.getQuery("buildings").setCachePolicy(AVQuery.CachePolicy.CACHE_THEN_NETWORK).findInBackground(new FindCallback<AVObject>() {
                            @Override
                            public void done(List<AVObject> avObjects, AVException e) {
                                if(e==null){
                                    buildingData = avObjects;
                                    buildingAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                        dialog.cancel();
                    }
                }).show();
            }
        });

        placeAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return placeData.size();
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
                    tv = new TextView(AddHouseActivity.this);
                }
                tv.setTextSize(28);
                tv.setText(placeData.get(position).getString("name"));
                return tv;
            }
        };

        buildingAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return buildingData.size();
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
                    tv = new TextView(AddHouseActivity.this);
                }
                tv.setTextSize(28);
                tv.setText(buildingData.get(position).getString("name"));
                return tv;
            }
        };

        tvBuildingName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AddHouseActivity.this).setAdapter(buildingAdapter,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvBuildingName.setText(buildingData.get(which).getString("name"));
                        index = which;
                        dialog.cancel();
                    }
                }).show();
            }
        });*/

        tvAddHouseSubmit.setIndeterminateProgressMode(true);
        tvAddHouseSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                if(index<0){
                    Toast.makeText(AddHouseActivity.this,"请选择楼栋",Toast.LENGTH_LONG).show();
                    return ;
                }*/
                String name = etHouseName.getText().toString();
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(AddHouseActivity.this,"请填写房号",Toast.LENGTH_LONG).show();
                    return;
                }
                //tvAddHouseSubmit.setClickable(false);
                tvAddHouseSubmit.setProgress(50);
                AVObject house = new AVObject("houses");
                AVObject building = new AVObject("buildings");
                building.setObjectId(buildingId);
                house.put("name",name);
                house.put("building",building);
                house.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e==null){
                            Toast.makeText(AddHouseActivity.this,"添加成功",Toast.LENGTH_LONG).show();
                            tvAddHouseSubmit.setProgress(100);
                        }else{
                            tvAddHouseSubmit.setProgress(-1);
                            Toast.makeText(AddHouseActivity.this,"添加失败",Toast.LENGTH_LONG).show();
                        }
                        tvAddHouseSubmit.setClickable(true);
                    }
                });

            }
        });
    }
}
