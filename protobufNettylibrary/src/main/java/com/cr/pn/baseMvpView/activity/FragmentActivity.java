package com.cr.pn.baseMvpView.activity;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.cr.pn.R;
import com.cr.pn.UIUtils.AlertDialog.AlertDialogWait;
import com.cr.pn.UIUtils.userOperation.PressTFinish.PreTFinish;
import com.cr.pn.Utils.ViewUtils.AdaptationUtil;
import com.cr.pn.ViewInjectLayout;
import com.cr.pn.activityManager.ActivityManager;
import com.cr.pn.configure.ActivityConfigure;
import com.cr.pn.configure.DefaultConfig;

import butterknife.ButterKnife;

/**
 * Created by zy on 2017/7/7.
 */

public abstract class FragmentActivity extends androidx.fragment.app.FragmentActivity implements View.OnClickListener{


    public Context context = null;

    private LinearLayout parentLinearLayout;

    private LinearLayout bar_linearLayout;

    public Application application;

    public int REQUEST_CODE_PERMISSION = 1;

    public ActivityConfigure activityConfigure;

    private PreTFinish preTFinish;

    private AlertDialogWait alertDialogWait;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        this.application = getApplication();
       /* if(ActivityManager.getInstance().getAppStatus()==AppStatusConstant.normal) {
            AppGcHandler();
        }*/
        alertDialogWait = new AlertDialogWait(context);
        initActivityConfigure();
        super.setContentView(R.layout.base_layout);
        setContentView(analysisLayout());
        ButterKnife.bind(this);
        ActivityManager.getInstance().addActivity(this);
        initView();
        initWidget();
    }

    /**
     * 初始化activity配置.
     */
    private void initActivityConfigure(){
        activityConfigure = (ActivityConfigure) DefaultConfig.activityConfigure.clone();
        activityConfigure.initActionTitleBarView(context);
        beforeContentView(activityConfigure);
        preTFinish = new PreTFinish(1500);
    }

    /**
     * 全屏显示
     */
    public void setFullActivity(){
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) bar_linearLayout.getLayoutParams();
        lp.topMargin = 0;
        bar_linearLayout.setLayoutParams(lp);
    }

    /**
     * 取消全屏显示
     */
    public void setNoFullActivity(){
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) bar_linearLayout.getLayoutParams();
        lp.topMargin = AdaptationUtil.barHeight(context);
        bar_linearLayout.setLayoutParams(lp);
    }

    /**
     * 返回当前是否为全屏等模式.
     * @return
     */
//    public BarHide getBarHide(){
//        return immersionBar.getBarHide();
//    }

    public void setContentView(int resId) {
        ViewGroup group = findViewById(android.R.id.content);
        group.removeAllViews(); //首先先移除在根布局上的组件
        LayoutInflater inflater = LayoutInflater.from(this);
        parentLinearLayout = new LinearLayout(this);
        parentLinearLayout.setOrientation(LinearLayout.VERTICAL);
        if (activityConfigure.getMainBg()!=0){
            parentLinearLayout.setBackgroundResource(activityConfigure.getMainBg());
        }
        bar_linearLayout = new LinearLayout(this);
        bar_linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        View resView = inflater.inflate(resId, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,1);
        parentLinearLayout.addView(bar_linearLayout);
        parentLinearLayout.addView(resView, layoutParams);
        group.addView(parentLinearLayout);
        initActionBar();
        setFullActivity();//设置全屏显示
    }

    public void hideMain(){
        parentLinearLayout.setVisibility(View.GONE);
    }

    public void showMain(){
        parentLinearLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 初始化配置actionBar.
     */
    private void initActionBar(){
        LinearLayout.LayoutParams lp;
        if (activityConfigure.isDefaultBar()){
            bar_linearLayout.setBackgroundColor(ContextCompat.getColor(this, activityConfigure.getBarColor()));
            bar_linearLayout.addView(activityConfigure.getActionTitleBarView().getView(),LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            lp =  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,activityConfigure.getBarHeight());
        }else {
            lp =  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        }
        lp.topMargin = AdaptationUtil.barHeight(context);
        bar_linearLayout.setLayoutParams(lp);
    }


    /**
     * 获取activity头部注解的布局文件
     * @return
     */
    public int analysisLayout(){
        Class<?> clazz = this.getClass();
        ViewInjectLayout viewInjectLayout = clazz.getAnnotation(ViewInjectLayout.class);
        if (viewInjectLayout != null) {
            return viewInjectLayout.value();
        }else{
            return -1;
        }
    }

    /**
     * 在调用setContentView方法之前调用.
     */
    public void beforeContentView(ActivityConfigure activityConfigure){

    }

    /**
     * 获取一个Intent.
     * @return
     */
    public Intent getNewIntent(){
        return new Intent();
    }

    /**
     * Activity跳转.
     * @return
     */
    public void activityTo(Class<?> cls,Bundle bundle){
        Intent intent = new Intent(context,cls);
        if (bundle!=null){
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void activityTo(Class<?> cls){
        activityTo(cls,null);
    }

    /**
     * 获取一个Handler Message.
     * @return
     */
    public Message getHandlerMessage(){
        return new Message();
    }

    /**
     * Activity跳转.
     * @return
     */
    public void activityToForResult(Class<?> cls,Bundle bundle,int requestCode){
        Intent intent = new Intent(context,cls);
        if (bundle!=null){
            intent.putExtras(bundle);
        }
        startActivityForResult(intent,requestCode);
    }

    /**
     * Activity跳转.
     * @return
     */
    public void activityToForResult(Class<?> cls,int requestCode){
        activityToForResult(cls,null,requestCode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        alertDialogWait.destroy();
        application = null;
        context = null;
//        ImmersionBar.with(this).destroy();
        ActivityManager.getInstance().finishActivity(this);
//        ViewGroup group = findViewById(android.R.id.content);
//        group.removeAllViews();
//        super.setContentView(R.layout.empty_view);
    }

    /*********************************重写方法*************************************/

    @Override
    public void onClick(View v) {
        widgetClick(v);
    }

    /**
     * 在调用setContentView方法之前调用.
     */
    public void beforeContentView(){

    }

    /**
     * findViewById
     * @param resId
     * @param <T>
     * @return
     */
    public <T extends View> T $(int resId) {
        return (T) super.findViewById(resId);
    }

    /**
     * 控件点击事件.
     * @param v
     */
    public abstract void widgetClick(View v);

    /**
     * 初始化操作.
     */
    public abstract void initView();

    /**
     * 初始化控件事件.
     */
    public abstract void initWidget();

    /*******************避免屏幕旋转时activity被销毁********************/

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /*********************app被销毁时处理*****************************/
    public void AppGcHandler(){

    }

    @Override
    public void onBackPressed() {
        if(preTFinish.pressDown(System.currentTimeMillis())){
            overApp();
        }
    }

    public void overApp(){
        finish();
    }

    /*********************获取对象********************/
    public AlertDialogWait getAlertDialogWait(){
        return alertDialogWait;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            ActivityManager.getInstance().finishActivity();
        }
        return super.onKeyDown(keyCode, event);
    }
}
