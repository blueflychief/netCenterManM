package com.gdin.netcentermanm.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.gdin.netcentermanm.R;

import java.util.ArrayList;
import java.util.List;

public class ManagerListActivity extends ActionBarActivity {

    private ListView lvManager;
    private TextView tvAddManager;
    private BaseAdapter adapter;
    private List<AVObject> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_list);

        initView();
    }

    private void initView(){
        lvManager = (ListView) findViewById(R.id.lv_manager);
        tvAddManager = (TextView) findViewById(R.id.tv_add_manager);
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
                if (convertView != null) {
                    tv = (TextView) convertView;
                } else {
                    tv = new TextView(ManagerListActivity.this);
                }
                tv.setTextSize(28);
                tv.setText(data.get(position).getString("name"));
                return tv;
            }
        };
        lvManager.setAdapter(adapter);
        AVQuery.getQuery("manager").findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException e) {
                if(e==null){
                    data = avObjects;
                    adapter.notifyDataSetChanged();
                }else{
                    Log.d("", e.toString());
                }
            }
        });

        tvAddManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManagerListActivity.this,AddManagerActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manager_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
