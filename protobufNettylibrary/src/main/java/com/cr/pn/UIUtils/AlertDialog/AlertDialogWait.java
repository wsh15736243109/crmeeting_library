package com.cr.pn.UIUtils.AlertDialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cr.pn.R;
import com.cr.pn.Utils.ViewUtils.StringUtil;

/**
 * Created by zy on 2017/2/7.
 */

public class AlertDialogWait{

    private Custom_Dialog dialog;
    private Window window;
    private WindowManager windowManager;
    private WindowManager.LayoutParams lp;
    private DialogInterface.OnKeyListener onKeyListener;
    private Context context;
    private int animationResId = R.style.dialogWindowAnim;

    public AlertDialogWait(Context context) {
        this.context = context;
        createBuilder();
    }

    /**
     * 初始化dialog及其相关对象.
     */
    private void createBuilder(){
        dialog = new Custom_Dialog(context,R.style.dialog);
        window = dialog.getWindow();
        window.setWindowAnimations(animationResId);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        lp = window.getAttributes();
    }

    /**
     * 设置动画效果
     * @param id
     */
    public void setAnimationResId(int id){
        animationResId = id;
        if (window!=null){
            window.setWindowAnimations(animationResId);
        }
    }

    /**
     * 自定义Dialog的OnKey事件.
     * 在调用Dialog之前调用.
     * @param onKeyListener
     */
    public void CustomDialogOnKey(DialogInterface.OnKeyListener onKeyListener){
        this.onKeyListener = onKeyListener;
    }

    /**
     * 等待框.
     * @param layout
     * progressbar样式.
     * @return
     */
    public Window showWait(int layout,double width,double height){
        Window window = CustomDialog(layout,width,height,false);
        return window;
    }

    /**
     * 等待框.
     */
    public Progress showWait(String title){
        Window window = showWait(R.layout.dialog_wait,0.4,0.3);
        ImageView progress = (ImageView) window.findViewById(R.id.loading_progress);
        Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.rotate);
        progress.startAnimation(operatingAnim);
        TextView tvLoad = (TextView) window.findViewById(R.id.tvLoad);
        if (!StringUtil.isEmpty(title)){
            tvLoad.setVisibility(View.VISIBLE);
        }
        tvLoad.setText(title);
        return new Progress(tvLoad);
    }

    /**
     * 提示框.
     * @param text
     * 提示内容.
     * @param title
     * 提示标题.
     */
    public void showPrompt(String text, String title){
        showPrompt(text, title, R.layout.dialog_prompt, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     *提示框.
     * @param text
     * 提示内容.
     * @param title
     * 提示标题.
     * @param resId
     * 自定义点击事件.
     */
    public void showPrompt(String text, String title, int resId, View.OnClickListener onClickListener){
        Window window = CustomDialog(resId,0.7,0.3,true);
        Button dismiss = (Button) window.findViewById(R.id.dismiss);
        dismiss.setOnClickListener(onClickListener);
        TextView prompt_tv = (TextView)window.findViewById(R.id.prompt);
        TextView title_tv = (TextView) window.findViewById(R.id.title);
        prompt_tv.setText(text);
        title_tv.setText(title+"");
    }

    /**
     * 选择框.
     * @param text
     * 内容.
     * @param title
     * 标题.
     * 点击事件.
     */
    public void showChoice( String text,String title, final onChooseClick onChooseClick){
        showChoice(text,title,R.layout.dialog_choice,0.7,0.3,onChooseClick);
    }

    /**
     * 选择框.
     * @param text
     * 内容.
     * @param title
     * 标题.
     * 点击事件.
     */
    public void showChoice( String text,String title, int resId,double width,double height,final onChooseClick onChooseClick){
        Window window = CustomDialog(resId,width,height,true);
        Button no = (Button) window.findViewById(R.id.no);
        Button yes = (Button) window.findViewById(R.id.yes);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChooseClick.onClickO(v);
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChooseClick.onClickT(v);
            }
        });
        TextView prompt_tv = (TextView)window.findViewById(R.id.prompt);
        TextView title_tv = (TextView) window.findViewById(R.id.title);
        prompt_tv.setText(text);
        title_tv.setText(title+"");
    }

    /**
     * 自定义Dialog.
     * @param layout
     * 自定义layout.
     * @param width
     * 宽度.
     * @param height
     * 高度.
     * @param isOutSide
     * 是否允许点击阴影部分.
     * @return
     * 返回Window对象，用于控件findViewById.
     */
    public Window CustomDialog(int layout, double width, double height,boolean isOutSide){
        View view = LinearLayout.inflate(context,layout,null);
        return CustomDialog(view,width,height,isOutSide);
    }

    /**
     * 自定义Dialog.
     * @param layout
     * 自定义layout.
     * @param width
     * 宽度.
     * @param height
     * 高度.
     * @param isOutSide
     * 是否允许点击阴影部分.
     * @return
     * 返回Window对象，用于控件findViewById.
     */
    public Window CustomDialog(View layout, double width, double height,boolean isOutSide){
        show();
        dialog.setCanceledOnTouchOutside(isOutSide);
        if (onKeyListener!=null){
            dialog.setOnKeyListener(onKeyListener);
        }else {
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return false;
                }
            });
        }
        lp.width = (int) (windowManager.getDefaultDisplay().getWidth() * width);
        lp.height = (int) (windowManager.getDefaultDisplay().getHeight() * height);
        window.setAttributes(lp);
        window.setContentView(layout);
        return window;
    }


    /**
     * 显示.
     */
    public void show(){
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        if (dialog.getCustomContext() instanceof Activity){
            if (((Activity)dialog.getCustomContext()).isFinishing()) {
                return;
            }
        }
        try {
            dialog.show();
        }catch (Exception e){

        }
    }

    /**
     * 设置Dialog消失监听.
     */
    public void setDismissListener(AlertDialogWait.dismissListener listener){
        dialog.setDismissListener(listener);
    }

    /**
     * 取消.
     */
    public void dismiss(){
        if (onKeyListener!=null){
            onKeyListener=null;
        }
        dialog.dismiss();
    }

    public void destroy(){
        context = null;
        dialog.destroy();
        dialog = null;
    }

    /**
     * 选择框事件.
     */
    public interface onChooseClick {
        void onClickT(View v);
        void onClickO(View v);
    }

    /**
     * 等待框进度设置.
     */
    public class Progress{
        private TextView progress;
        private Progress(TextView progress){
            this.progress = progress;
        }
        public void setProgress(String p){
            progress.setText(p);
        }
    }

    /**
     * Dialog取消监听.
     */
    public interface dismissListener{
        public void dismiss();
    }
}
