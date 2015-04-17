package com.gdin.netcentermanm;

import android.app.Application;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

public class MyApplication extends Application{
    public static String installationId = "";
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
        AVOSCloud.initialize(this,
                "n672vu0h76ujkvbszsoe0yhedsuepp5i9rt9dp2k7t1p42zd",
                "wp5jgq58xhoj4h7mcnr29wxoe8ifxasui717sjs15gyzj06f");
        AVInstallation.getCurrentInstallation().saveInBackground(
                new SaveCallback() {
                    public void done(AVException e) {
                        if (e == null) {
                            // 保存成功
                            installationId = AVInstallation
                                    .getCurrentInstallation()
                                    .getInstallationId();
                            AVUser user = AVUser.getCurrentUser();
                            if(user!=null){
                                user.put("installationId",installationId);
                                user.saveInBackground();
                            }
                            // 关联 installationId 到用户表等操作……
                        } else {
                            // 保存失败，输出错误信息
                        }
                    }
                });
	}

}
