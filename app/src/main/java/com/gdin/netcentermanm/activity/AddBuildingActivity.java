package com.gdin.netcentermanm.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class AddBuildingActivity extends Activity {

    private TextView tvPlaceName;
    private CircularProgressButton tvAddSchoolPlaceSubmit;
    private EditText etName;
    private List<AVObject> data = new ArrayList<>();
    private BaseAdapter adapter;
    private int index ;
    private ImageView imgReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_building);

        initView();

    }

    private void initView() {
        imgReturn = (ImageView) findViewById(R.id.img_return);
        imgReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final String placeId  = getIntent().getStringExtra("placeId");
        String placeName = getIntent().getStringExtra("placeName");
        tvPlaceName = (TextView) findViewById(R.id.tv_building_name);
        tvAddSchoolPlaceSubmit = (CircularProgressButton) findViewById(R.id.tv_add_building_submit);
        etName = (EditText) findViewById(R.id.et_add_building_name);

        tvPlaceName.setText(placeName);
        AVObject building = new AVObject("building");
        building.setObjectId(placeId);
/*        AVQuery.getQuery("building").whereEqualTo("place",building).setCachePolicy(AVQuery.CachePolicy.CACHE_THEN_NETWORK).findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException e) {
                if(e==null){
                    data = avObjects;
                    adapter.notifyDataSetChanged();
                }
            }
        });*/
/*        adapter = new BaseAdapter() {
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
                    tv = new TextView(AddBuildingActivity.this);
                }
                tv.setTextSize(28);
                tv.setText(data.get(position).getString("name"));
                return tv;
            }
        };*/

        /*tvPlaceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AddBuildingActivity.this).setAdapter(adapter,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvPlaceName.setText(data.get(which).getString("name"));
                        index = which;
                        dialog.cancel();
                    }
                }).show();
            }
        });*/

        tvAddSchoolPlaceSubmit.setIndeterminateProgressMode(true);
        tvAddSchoolPlaceSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placeName = tvPlaceName.getText().toString();
                String name = etName.getText().toString();
                if(TextUtils.isEmpty(placeName)||TextUtils.isEmpty(name)){
                    Toast.makeText(AddBuildingActivity.this,"请填写详细资料",Toast.LENGTH_LONG).show();
                    return;
                }
                if(tvAddSchoolPlaceSubmit.getProgress()==50){
                    return;
                }
                tvAddSchoolPlaceSubmit.setProgress(50);
                //tvAddSchoolPlaceSubmit.setClickable(false);
                AVObject place = new AVObject("school_place");
                place.setObjectId(placeId);
                AVObject building = new AVObject("buildings");
                building.put("name",name);
                building.put("place",place);
                building.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e==null){
                            tvAddSchoolPlaceSubmit.setProgress(100);
                            Toast.makeText(AddBuildingActivity.this,"添加成功",Toast.LENGTH_LONG).show();
                        }else{
                            tvAddSchoolPlaceSubmit.setProgress(-1);
                            Toast.makeText(AddBuildingActivity.this,"添加失败",Toast.LENGTH_LONG).show();
                        }
                        //tvAddSchoolPlaceSubmit.setClickable(true);

                    }
                });
            }
        });
    }
}
