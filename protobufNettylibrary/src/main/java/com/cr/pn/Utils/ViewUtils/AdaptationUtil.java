package com.cr.pn.Utils.ViewUtils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * Created by zy on 2017/4/8.
 * 适配相关工具类.
 */
public class AdaptationUtil {

    static float scale = 0;
    static float scaledDensity = 0;
    static int statusbarheight = 0;

    public static void init(Context context){
        if (null == context) {
            return;
        }
        scale = context.getResources().getDisplayMetrics().density;
        scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        statusbarheight = getStatusBarHeight(context);
    }

    /**
     * 根据手机的分辨率从 dp(像素) 的单位 转成为 px
     */
    public static int dip2px(float dpValue) {
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(float pxValue) {
        return (int) (pxValue / scaledDensity + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(float spValue) {
        return (int) (spValue * scaledDensity + 0.5f);
    }

    /**
     * 获取屏幕高宽px.
     * @return
     * DisplayMetrics.widthPixels
     * 宽度
     * DisplayMetrics.heightPixels
     * 高度
     */
    public static DisplayMetrics getDisplay(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 获取设备最小宽度
     * @param activity
     * @return
     */
    public static float getDpSmallest(Activity activity){
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int widthPixels = dm.widthPixels;
        float density = dm.density;
        float widthDP = widthPixels / density;
        return widthDP;
    }

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        if (statusbarheight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusbarheight = context.getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (statusbarheight == 0) {
            statusbarheight = AdaptationUtil.dip2px(25);
        }
        return statusbarheight;
    }

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int barHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取view的坐标,包含状态栏
     * @param view
     * @return
     */
    public static int[] getViewStatusBarX_Y(View view){
        int[] location = new  int[2] ;
        view.getLocationOnScreen(location);
        return location;
    }

    /**
     * 获取view的坐标,不包含状态栏
     * @param view
     * @return
     */
    public static int[] getViewInWindowBarX_Y(View view){
        int[] location = new  int[2] ;
        view.getLocationInWindow(location);
        return location;
    }
}
