package com.cr.pn.configure;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.cr.pn.viewUI.actionBar.ActionTitleBarView;

/**
 * Created by zy on 2018/7/4.
 * activity配置.
 */
public class ActivityConfigure{

    /**
     * 是否使用默认ActionBar.
     */
    private boolean defaultBar;

    /**
     *  应用Actionbar背景颜色
     */
    private int barColor;

    /**
     * 应用Actionbar高度.
     * 默认60dp.
     */
    private int barHeight;

    /**
     * actionBar自定义View.
     */
    private int resId;

    private ActionTitleBarView actionTitleBarView;

    /**
     * 标题栏的颜色.
     * 使用资源文件中color资源(R.color.****)
     */
    private int titleViewColor;

    /**
     * 标题栏透明度.
     */
    private float titleBarAlpha;

    /**
     * 导航栏颜色
     */
    private int navigationColor;

    /**
     * 导航栏透明度.
     */
    private float navigationBarAlpha;

    /**
     * 导航栏是否与界面重叠.
     */
    private boolean isFullScreen;

    /**
     * 设置背景
     */
    private int mainBg = 0;

    public boolean isDefaultBar() {
        return defaultBar;
    }

    public ActivityConfigure setDefaultBar(boolean defaultBar) {
        this.defaultBar = defaultBar;
        return this;
    }

    public int getBarColor() {
        return barColor;
    }

    public ActivityConfigure setBarColor(int barColor) {
        this.barColor = barColor;
        return this;
    }

    public int getTitleViewColor() {
        return titleViewColor;
    }

    public ActivityConfigure setTitleViewColor(int titleViewColor) {
        this.titleViewColor = titleViewColor;
        return this;
    }

    public float getTitleBarAlpha() {
        return titleBarAlpha;
    }

    public ActivityConfigure setTitleBarAlpha(float titleBarAlpha) {
        this.titleBarAlpha = titleBarAlpha;
        return this;
    }

    public int getNavigationColor() {
        return navigationColor;
    }

    public ActivityConfigure setNavigationColor(int navigationColor) {
        this.navigationColor = navigationColor;
        return this;
    }

    public float getNavigationBarAlpha() {
        return navigationBarAlpha;
    }

    public ActivityConfigure setNavigationBarAlpha(float navigationBarAlpha) {
        this.navigationBarAlpha = navigationBarAlpha;
        return this;
    }

    public boolean isFullScreen() {
        return isFullScreen;
    }

    public ActivityConfigure setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
        return this;
    }

    public int getBarHeight() {
        return barHeight;
    }

    public ActivityConfigure setBarHeight(int barHeight) {
        this.barHeight = barHeight;
        return this;
    }

    public ActionTitleBarView getActionTitleBarView() {
        return actionTitleBarView;
    }

    public ActivityConfigure setResId(int resId) {
        this.resId = resId;
        return this;
    }

    public void initActionTitleBarView(Context context){
        if (actionTitleBarView == null){
            View view = LayoutInflater.from(context).inflate(resId,null);
            actionTitleBarView = new ActionTitleBarView(view,this);
        }
    }

    public int getMainBg() {
        return mainBg;
    }

    public ActivityConfigure setMainBg(int mainBg) {
        this.mainBg = mainBg;
        return this;
    }

    public ActivityConfigure clone(){
        ActivityConfigure activityConfigure = new ActivityConfigure();
        activityConfigure.setDefaultBar(defaultBar)
                .setTitleViewColor(getTitleViewColor())
                .setBarColor(getBarColor())
                .setMainBg(getMainBg())
                .setNavigationColor(getNavigationColor())
                .setTitleBarAlpha(getTitleBarAlpha())
                .setNavigationBarAlpha(getNavigationBarAlpha())
                .setFullScreen(isFullScreen)
                .setBarHeight(getBarHeight())
                .setResId(resId);
        return activityConfigure;
    }

}
