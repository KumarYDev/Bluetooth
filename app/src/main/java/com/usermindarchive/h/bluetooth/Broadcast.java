package com.usermindarchive.h.bluetooth;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by HERO on 12/20/2017.
 */

public class Broadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String state= intent.getAction();
        Toast.makeText(context,"Action."+intent.getAction(),Toast.LENGTH_LONG).show();
        Log.e("Broadcast","Action."+intent.getAction());

//        if (appStatus(context)) {
//            packagem(context);
//        }

        if(state.equals("android.intent.action.ACTION_POWER_CONNECTED")) {
            if (appStatus(context)) {
                packagem(context);
            }
        }
        if(state.equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
            if (!appStatus(context)) {
                killAppBypackage(context.getPackageName(),context);
                System.exit(0);
            }
        }


        }

    public void packagem(Context context){
        PackageManager pm = context.getPackageManager();
        try
        {
            String packageName = context.getPackageName();
            Intent launchIntent = pm.getLaunchIntentForPackage(packageName);
            context.startActivity(launchIntent);
        }
        catch (Exception e1)
        {
        }
    }
    public boolean appStatus(Context context){
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }

    public void appkill(Context context){

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();

            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {


                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {

                    for (String activeProcess : processInfo.pkgList) {

                        if (activeProcess.equals(context.getPackageName())) {

                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);

            ComponentName componentInfo = taskInfo.get(0).topActivity;

            if (componentInfo.getPackageName().equals(context.getPackageName())) {



            }
        }

    }

    private void killAppBypackage(String packageTokill,Context context){

        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = context.getPackageManager();
        //get a list of installed apps.
        packages = pm.getInstalledApplications(0);


        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        mActivityManager.killBackgroundProcesses(packageTokill);
        String myPackage = context.getApplicationContext().getPackageName();

        for (ApplicationInfo packageInfo : packages) {

//            if((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM)==1) {
//                continue;
//            }
//            if(packageInfo.packageName.equals(myPackage)) {
//                continue;
//            }
            if(packageInfo.packageName.equals(packageTokill)) {
                mActivityManager.killBackgroundProcesses(packageInfo.packageName);
            }

        }

    }
}
