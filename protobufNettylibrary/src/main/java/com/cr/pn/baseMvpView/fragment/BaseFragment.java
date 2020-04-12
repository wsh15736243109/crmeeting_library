package com.cr.pn.baseMvpView.fragment;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cr.pn.ViewInjectLayout;
import com.cr.pn.baseMvpView.activity.FragmentActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by zy on 2017/7/15.
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    private View view;
    public Application application;
    public Context context;
    private Unbinder unbinder;

    public BaseFragment(){}

    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(analysisLayout(),null);
        }
        unbinder = ButterKnife.bind(this, view);
        initCreateView();
        return view;
    }

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

    @Override
    public final void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        application = this.getActivity().getApplication();
        context = this.getActivity();
        initView();
        initWidget();
    }

    @Override
    public void onClick(View v) {
        widgetClick(v);
    }

    /**
     * 控件点击事件.
     * @param v
     */
    public abstract void widgetClick(View v);

    /**
     * 初始化操作
     */
    public abstract void initView();

    /**
     * onCreateView调用时调用.
     */
    public void initCreateView(){

    }

    /**
     * 初始化控件事件.
     */
    public abstract void initWidget();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        view = null;
        application = null;
        context = null;
    }

    /**
     * 返回activity.
     * @return
     */
    public FragmentActivity getBaseActivity(){
        if (getActivity() instanceof FragmentActivity){
            return (FragmentActivity) getActivity();
        }else {
            return null;
        }
    }

}
