package com.cr.pn.handler;

import android.app.Activity;
import android.app.Service;
import android.os.Handler;

import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;

/**
 * Created by zy on 2018/2/24.
 */

public class NoLeakHandler extends Handler {

    private WeakReference<Activity> mActivity;

    private WeakReference<Fragment> mFragment;

    private WeakReference<Service> mService;

    public NoLeakHandler(Activity activity){
        mActivity = new WeakReference<>(activity);
    }

    public NoLeakHandler(Fragment fragment){
        mFragment = new WeakReference<>(fragment);
    }

    public NoLeakHandler(Service service){
        mService = new WeakReference<Service>(service);
    }
}