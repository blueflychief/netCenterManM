package com.gdin.netcentermanm.activity;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.gdin.netcentermanm.R;
import com.gdin.netcentermanm.R.layout;
import com.gdin.netcentermanm.utils.ViewHolder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class SchoolPlaceActivity extends Activity {

    private ListView lvSchoolPlace;
    private TextView tvAddSchoolPlace;
    private List<AVObject> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_place);

        initView();

    }

    private void initView(){
        lvSchoolPlace = (ListView) findViewById(R.id.lv_school_place);
        tvAddSchoolPlace = (TextView) findViewById(R.id.tv_add_school_place);
        AVQuery.getQuery("school_place").setCachePolicy(AVQuery.CachePolicy.CACHE_THEN_NETWORK).findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(final List<AVObject> avObjects, AVException e) {
                if(e!=null){
                    Log.d("", e.toString());
                }else{
                    data =avObjects;
                    lvSchoolPlace.setAdapter(new BaseAdapter() {
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
                            TextView textView = null;
                            if(convertView!=null){
                                textView = (TextView)convertView;
                            }else{
                                textView = new TextView(SchoolPlaceActivity.this);
                            }
                            textView.setTextSize(28);
                            textView.setText(data.get(position).getString("name"));
                            return textView;
                        }
                    });
                }

            }
        });

        lvSchoolPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SchoolPlaceActivity.this,BuildingListActivity.class);
                intent.putExtra("placeId",data.get(position).getObjectId());
                intent.putExtra("placeName",data.get(position).getString("name"));
                startActivity(intent);
            }
        });

        tvAddSchoolPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SchoolPlaceActivity.this,AddSchoolPlaceActivity.class));
            }
        });

    }
}
