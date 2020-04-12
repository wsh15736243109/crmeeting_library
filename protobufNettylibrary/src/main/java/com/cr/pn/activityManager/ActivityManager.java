package com.cr.pn.activityManager;

import android.app.Activity;
import android.content.Context;

import com.cr.pn.activityManager.AppStatus.AppStatusConstant;

import java.util.List;
import java.util.Stack;

/**
 * Created by zy on 2017/2/8.
 */

public class ActivityManager {

    private static Stack<Activity> activityStack;
    private static ActivityManager activityManager;

    private AppStatusConstant appStatus;

    private ActivityManager() {
    }

    public static ActivityManager getInstance() {
        if (activityManager == null) {
            synchronized (ActivityManager.class) {
                if (activityManager == null) {
                    activityManager = new ActivityManager();
                    activityManager.setAppStatus(AppStatusConstant.normal);
                }
            }
        }
        return activityManager;
    }

    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    public Activity currentActivity() {
        if (activityStack == null || activityStack.size() == 0) {
            return null;
        }
        return activityStack.lastElement();
    }

    public Activity getActivity(Class clazz) {
        for (int i = 0; i < activityStack.size(); i++) {
            if (activityStack.get(i).getClass().getName().equals(clazz.getName())) {
                return activityStack.get(i);
            }
        }
        return null;
    }

    public void finishActivity() {
        finishActivity(activityStack.lastElement());
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    @SuppressWarnings("deprecation")
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            android.app.ActivityManager activityManager = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前app状态
     *
     * @return
     */
    public AppStatusConstant getAppStatus() {
        return this.appStatus;
    }

    /**
     * 设置app当前状态.
     *
     * @param appStatus
     */
    public void setAppStatus(AppStatusConstant appStatus) {
        this.appStatus = appStatus;
    }

    public static void cleanMemory(Context context) {
        android.app.ActivityManager activityManager = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        android.app.ActivityManager.RunningAppProcessInfo runningAppProcessInfo = null;
        List<android.app.ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList = activityManager.getRunningAppProcesses();
        for (int i = 0; i < runningAppProcessInfoList.size(); ++i) {
            runningAppProcessInfo = runningAppProcessInfoList.get(i);
            String processName = runningAppProcessInfo.processName;
            //调用杀掉进程的方法
            System.out.println("---> 开始清理:" + processName);
        }
    }
}
