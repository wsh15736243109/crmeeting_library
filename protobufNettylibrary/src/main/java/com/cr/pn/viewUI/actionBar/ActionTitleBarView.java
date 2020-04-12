package com.cr.pn.viewUI.actionBar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.cr.pn.R;
import com.cr.pn.Utils.ViewUtils.AdaptationUtil;
import com.cr.pn.configure.ActivityConfigure;

/**
 * Created by zy on 2018/7/4.
 */

public class ActionTitleBarView {

    /**
     * actionBarView
     */
    private View view;

    private LinearLayout action_title_bar;

    /**
     * actionBar左边按钮
     */
    private TextView title_left;
    public static int title_left_Id = 0;

    /**
     * actionBar菜单按钮
     */
    private Button title_menu_btn;
    public static int title_menu_btn_Id = 0;

    /**
     * actionBar标题按钮
     */
    private Button title_btn;
    public static int title_btn_Id = 0;

    private ActivityConfigure activityConfigure;

    public ActionTitleBarView(View view,ActivityConfigure activityConfigure){
        this.view = view;
        this.activityConfigure = activityConfigure;
        initView();
    }

    private void initView(){
        if (activityConfigure.isDefaultBar()){
            action_title_bar = view.findViewById(R.id.action_title_bar);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            action_title_bar.setLayoutParams(layoutParams);

            title_left = view.findViewById(R.id.title_left);
            title_menu_btn = view.findViewById(R.id.title_menu_btn);
            title_btn = view.findViewById(R.id.title_btn);

            title_btn_Id = title_btn.getId();
            title_left_Id = title_left.getId();
            title_menu_btn_Id = title_menu_btn.getId();

            int resId = R.drawable.returnicon;
            Drawable drawableLeft = view.getResources().getDrawable(
                    resId);
            title_left.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                    null, null, null);
        }
    }

    /**
     * 设置标题点击事件
     */
    public void initOnClickTitle(View.OnClickListener clickListener){
        title_btn.setOnClickListener(clickListener);
    }

    /**
     * 设置左边返回点击事件
     */
    public void initOnClickLeft(View.OnClickListener clickListener){
        title_left.setOnClickListener(clickListener);
    }

    /**
     * 设置菜单返回点击事件
     */
    public void initOnClickMenu(View.OnClickListener clickListener){
        title_menu_btn.setOnClickListener(clickListener);
    }

    /*********************左边****************************/

    /**
     * 设置ActionBar左边图标
     * @param resId
     * @param context
     */
    public void setTitleLeftBg(int resId, Context context){
        if (resId <0){
            title_left.setCompoundDrawablesWithIntrinsicBounds(null,
                    null, null, null);
            return;
        }
        if (resId==0){
            resId = R.drawable.returnicon;
        }
        Drawable drawableLeft = context.getResources().getDrawable(
                resId);
        title_left.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                null, null, null);
    }


    /**
     * 设置ActionBar左边文字
     * @param text
     */
    public void setTitleLeftText(String text){
        title_left.setText(text);
    }

    /**
     * 设置ActionBar左边文字大小
     * 值使用dimen资源
     */
    public void setTitleLeftTextSizeDefault(int resID,Context context){
        setTitleLeftTextSize(context.getResources().getDimensionPixelSize(resID));
    }

    /**
     * 设置ActionBar左边文字大小
     */
    public void setTitleLeftTextSize(int size){
        title_left.setTextSize(AdaptationUtil.px2sp(size));
    }

    /**
     * 设置ActionBar左边文字颜色
     */
    public void setTitleLeftTextColor(int resId,Context context){
        title_left.setTextColor(ContextCompat.getColor(context, resId));
    }

    /*************************标题*****************************/

    /**
     * 设置ActionBar标题文字
     * @param text
     */
    public void setTitleText(String text){
        title_btn.setText(text);
    }

    /**
     * 设置ActionBar标题文字大小
     * 值使用dimen资源
     */
    public void setTitleTextSizeDefault(int resID,Context context){
        setTitleTextSize(context.getResources().getDimensionPixelSize(resID));
    }

    /**
     * 设置ActionBar标题文字大小
     */
    public void setTitleTextSize(int size){
        title_btn.setTextSize(AdaptationUtil.px2sp(size));
    }

    /**
     * 设置ActionBar标题文字颜色
     */
    public void setTitleTextColor(int resId,Context context){
        title_btn.setTextColor(ContextCompat.getColor(context, resId));
    }

    /**
     * 修改标题栏背景图片.
     * @param resId
     */
    public void changeTitleBg(int resId){
        title_btn.setBackgroundResource(resId);
    }

    /********************************右边*********************************/



    /**
     * 修改右边按钮背景图片.
     * @param resId
     */
    public void setTitleMenuBg(int resId){
        title_menu_btn.setBackgroundResource(resId);
    }

    /**
     * 修改右边文字
     * @param text
     */
    public void setMenuText(String text){
        title_menu_btn.setText(text);
    }

    /**
     * 设置右边标题文字大小
     */
    public void setMenuTextSize(int size){
        title_menu_btn.setTextSize(AdaptationUtil.px2sp(size));
    }

    /**
     * 设置右边标题文字大小
     * 值使用dimen资源
     */
    public void setMenuTextSizeDefault(int resID,Context context){
        setMenuTextSize(context.getResources().getDimensionPixelSize(resID));
    }

    /**
     * 设置右边标题文字颜色
     */
    public void setMenuTextColor(int resId,Context context){
        title_menu_btn.setTextColor(ContextCompat.getColor(context, resId));
    }

    public void setMenuHeight(int resIdH){
        int size = title_menu_btn.getContext().getResources().getDimensionPixelOffset(resIdH);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) title_menu_btn.getLayoutParams();
        params.height= size;
        title_menu_btn.setLayoutParams(params);
    }

    public void setMenuWith(int resIdW){
        int size = title_menu_btn.getContext().getResources().getDimensionPixelOffset(resIdW);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) title_menu_btn.getLayoutParams();
        params.width = size;
        title_menu_btn.setLayoutParams(params);
    }

    /**
     * 关闭菜单按钮
     */
    public void closeMenu(){
        title_menu_btn.setVisibility(View.INVISIBLE);
    }

    /**
     * 打开菜单按钮
     */
    public void openMenu(){
        title_menu_btn.setVisibility(View.VISIBLE);
    }


    public View getView() {
        return view;
    }

    /**
     * 修改标题栏view
     * @param titleView
     */
    public void changeTitleView(View titleView){
        action_title_bar.removeViewAt(1);
        action_title_bar.addView(titleView,1);
    }

    /**
     * 修改菜单栏view
     * @param menuView
     */
    public void changeMenuView(View menuView){
        action_title_bar.removeViewAt(2);
        action_title_bar.addView(menuView,2);
    }

    /**
     * 修改返回view
     * @param leftView
     */
    public void changeLeftView(View leftView){
        action_title_bar.removeViewAt(0);
        action_title_bar.addView(leftView,0);
    }

    public void setView(View view) {
        this.view = null;
        this.view = view;
    }
}
