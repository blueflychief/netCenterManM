package com.gdin.netcentermanm.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.gdin.netcentermanm.MainActivity;
import com.gdin.netcentermanm.R;

import java.util.ArrayList;
import java.util.List;

public class ManagerListActivity extends ActionBarActivity {

    private ListView lvManager;
    private TextView tvAddManager;
    private BaseAdapter adapter;
    private List<AVUser> data = new ArrayList<>();
    private ImageView imgReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_list);

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
                tv.setText(data.get(position).getUsername());
                return tv;
            }
        };
        lvManager.setAdapter(adapter);
        AVUser.getQuery().whereEqualTo("isManager",true).addAscendingOrder("username").findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> avUsers, AVException e) {
                if (e == null) {
                    data = avUsers;
                    adapter.notifyDataSetChanged();
                } else {
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
        lvManager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                AVUser user =   data.get(position);
                bundle.putString("id",user.getObjectId());
                bundle.putString("name",user.getUsername());
                bundle.putString("sName",user.getString("sName"));
                bundle.putBoolean("isRoot",user.getBoolean("managerRoot"));
                bundle.putBoolean("enable",user.getBoolean("enable"));
                Intent intent = new Intent(ManagerListActivity.this,ManagerInfoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
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
