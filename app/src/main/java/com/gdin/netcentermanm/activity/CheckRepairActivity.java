package com.gdin.netcentermanm.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CheckRepairActivity extends Activity {
    private TextView tvCheckRepairTitle;
    private TextView tvCheckRepairUserName;
    private TextView tvCheckRepairBuilding;
    private TextView tvCheckRepairHouse;
    private TextView tvCheckRepairTime;
    private TextView tvCheckRepairReason;
    private TextView tvCheckRepairManagerName;
    private TextView tvCheckRepairProgress;
    private TextView tvCheckRepairAppointtime;
    private TextView tvCheckRepairResolveTime;
    private TextView tvCommit;
    private TextView tvReturn;

    private String date = "";
    private String time = "";

    private List<AVUser> managerUsers;
    private String[] userNames;
    private String[] ids;
    private boolean[] isCheckeds;
    private List<String> managerUserObjId = new ArrayList<>();


    String avId ="";
    String  title=  "";
    String  reason= "";
    String  appointTime= "";
    String  house= "";
    String buildingName ="";
    String managerName ="";
    String userName ="";
    String buildingId ="";
    String progress = "";
    List managerUserIds =null;
    String appointmentTime = "";
    String resolveTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_repair);
        initView();
    }


    private void initView(){
        Bundle bundle = getIntent().getExtras();
        avId = bundle.getString("avId");
        tvCheckRepairTitle = (TextView) findViewById(R.id.tv_check_repair_title);
        tvCheckRepairUserName = (TextView) findViewById(R.id.tv_check_repair_user_name);
        tvCheckRepairBuilding = (TextView) findViewById(R.id.tv_check_repair_building);
        tvCheckRepairHouse = (TextView) findViewById(R.id.tv_check_repair_house);
        tvCheckRepairTime = (TextView) findViewById(R.id.tv_check_repair_time);
        tvCheckRepairReason = (TextView) findViewById(R.id.tv_check_repair_reason);
        tvCheckRepairManagerName = (TextView) findViewById(R.id.tv_check_repair_manager_name);
        tvCheckRepairProgress = (TextView) findViewById(R.id.tv_check_repair_progress);
        tvCheckRepairAppointtime = (TextView) findViewById(R.id.tv_check_repair_appointtime);
        tvCheckRepairResolveTime = (TextView) findViewById(R.id.tv_check_repair_resolve_time);
        tvCommit = (TextView) findViewById(R.id.tv_check_repair_commit);
        tvReturn = (TextView) findViewById(R.id.tv_check_repair_return);
        AVQuery.getQuery("repair").whereEqualTo("objectId",avId).include("user").include("user.building").findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException e) {
                if(e==null&&avObjects!=null&&!avObjects.isEmpty()){
                    AVObject repair = avObjects.get(0);
                    AVUser user = repair.getAVUser("user");
                    AVObject building = user.getAVObject("building");
                    buildingId = building.getObjectId();
                    userName = user.getString("sName");
                    progress = repair.getString("progress");
                    title = repair.getString("title");
                    reason = repair.getString("reason");
                    appointmentTime = repair.getString("appointmentMTime");
                    resolveTime = repair.getString("resolveTime");

                    appointTime =  repair.getString("appointTime");
                    managerUserIds = repair.getList("managerUsers");
                    buildingName = building.getString("name");
                    house = user.getString("house");


                    tvCheckRepairTitle.setText(title);
                    tvCheckRepairUserName.setText(userName);
                    tvCheckRepairBuilding.setText(buildingName);
                    tvCheckRepairHouse.setText(house);
                    tvCheckRepairReason.setText(reason);
                    //tvCheckRepairManagerName.setText(managerName);
                    tvCheckRepairProgress.setText(progress);
                    if("完成".equals(progress)){
                        tvCheckRepairResolveTime.setVisibility(View.VISIBLE);
                    }
                    tvCheckRepairTime.setText(appointTime);
                    tvCheckRepairResolveTime.setText(resolveTime);
                    tvCheckRepairAppointtime.setText(appointmentTime);

                    AVUser.getQuery().whereEqualTo("isManager", true).findInBackground(new FindCallback<AVUser>() {
                        @Override
                        public void done(List<AVUser> avUsers, AVException e) {
                            if (e == null) {
                                managerUsers = avUsers;
                                userNames = new String[avUsers.size()];
                                ids = new String[avUsers.size()];
                                isCheckeds = new boolean[avUsers.size()];
                                String strs = "";
                                for(int i = 0;i<avUsers.size();i++){
                                    userNames[i] = avUsers.get(i).getUsername();
                                    ids[i] = avUsers.get(i).getObjectId();
                                    if(managerUserIds!=null&&managerUserIds.contains(avUsers.get(i).getObjectId())){
                                        isCheckeds[i] = true;
                                        strs = strs + avUsers.get(i).getUsername()+",";
                                    }
                                }
                                //managerUserObjId.addAll(Arrays.asList(ids));
                                if(strs.length()>0)tvCheckRepairManagerName.setText(strs.substring(0,strs.length()-1));
                                //tvCheckRepairManagerName.setVisibility(View.VISIBLE);
                               /* names = new String[avUsers.size()];
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
                                Log.d("", Arrays.toString(names)+ "  "+Arrays.toString(isCheckeds));
                                tvManager.setVisibility(View.VISIBLE);*/
                            }
                        }
                    });

                }
            }
        });

        tvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVObject repair = new AVObject("repair");
                repair.setObjectId(avId);
                repair.put("managerUsers",managerUserObjId);
                repair.put("progress",tvCheckRepairProgress.getText().toString());
                repair.put("resolveTime",tvCheckRepairResolveTime.getText().toString());
                repair.put("appointmentMTime",tvCheckRepairAppointtime.getText().toString());
                repair.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e==null){
                            Toast.makeText(CheckRepairActivity.this,"修改完成",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(CheckRepairActivity.this,"修改失败",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        tvCheckRepairProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CheckRepairActivity.this);
                builder.setTitle("请选择报修进度");
                final String[] titles = new String[]{"受理","完成"};
                builder.setItems(titles,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvCheckRepairProgress.setText(titles[which]);
                        if(which==1){
                            tvCheckRepairResolveTime.setVisibility(View.VISIBLE);
                        }else{
                            tvCheckRepairResolveTime.setVisibility(View.INVISIBLE);
                        }
                    }
                });
                builder.show();
            }
        });

        tvCheckRepairAppointtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                final Calendar c = Calendar.getInstance(Locale.CHINA);
                DatePickerDialog datePickerDialog = new DatePickerDialog(CheckRepairActivity.this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date = year+"-"+monthOfYear+"-"+dayOfMonth;
                        Log.d("",""+year+"    "+monthOfYear+"    "+dayOfMonth);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(CheckRepairActivity.this,new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Log.d("",""+hourOfDay+"     "+minute);
                                time = hourOfDay+":"+minute;
                                tvCheckRepairAppointtime.setText(date+" "+time);
                            }
                        },c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true);
                        //timePickerDialog.create();
                        timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE,"确定",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        timePickerDialog.show();
                    }
                },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DATE));
                //datePickerDialog.create();
                Calendar cal = Calendar.getInstance();
                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
                long time = cal.getTimeInMillis();
                datePickerDialog.getDatePicker().setMinDate(time);
                datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE,"确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                datePickerDialog.show();
            }
        });

        tvCheckRepairResolveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                final Calendar c = Calendar.getInstance(Locale.CHINA);
                DatePickerDialog datePickerDialog = new DatePickerDialog(CheckRepairActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date = year + "-" + monthOfYear + "-" + dayOfMonth;
                        Log.d("", "" + year + "    " + monthOfYear + "    " + dayOfMonth);
                        TimePickerDialog timePickerDialog = new TimePickerDialog(CheckRepairActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Log.d("", "" + hourOfDay + "     " + minute);
                                time = hourOfDay + ":" + minute;
                                tvCheckRepairResolveTime.setText(date + " " + time);
                            }
                        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
                        //timePickerDialog.create();
                        timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        timePickerDialog.show();
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                //datePickerDialog.create();
                Calendar cal = Calendar.getInstance();
                cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
                long time = cal.getTimeInMillis();
                datePickerDialog.getDatePicker().setMinDate(time);
                datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                datePickerDialog.show();
            }
        });

        tvCheckRepairManagerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CheckRepairActivity.this);
                builder.setMultiChoiceItems(userNames,isCheckeds,new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        isCheckeds[which] = isChecked;
                    }
                });
                builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str = "";
                        for(int i = 0;i<isCheckeds.length;i++){
                            if(isCheckeds[i]){
                                str = str+userNames[i]+",";
                                managerUserObjId.add(ids[i]);
                            }else{
                                managerUserIds.remove(ids[i]);
                            }
                        }
                        str = str.substring(0,str.length()-1);
                        tvCheckRepairManagerName.setText(str);
                    }
                });
                builder.show();
            }
        });






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_check_repair, menu);
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
