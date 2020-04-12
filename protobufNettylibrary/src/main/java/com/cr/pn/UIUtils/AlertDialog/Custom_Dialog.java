package com.cr.pn.UIUtils.AlertDialog;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

/**
 * Created by zy on 2017/3/29.
 */

public class Custom_Dialog extends Dialog {

    private Context context;

    private AlertDialogWait.dismissListener listener;

    public Custom_Dialog(Context context) {
        super(context);
        this.context = context;
    }

    public Custom_Dialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected Custom_Dialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @NonNull
    public Context getCustomContext() {
        return context;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (listener!=null){
            listener.dismiss();
        }
    }

    public void destroy(){
        listener = null;
        context = null;
    }

    public void setDismissListener(AlertDialogWait.dismissListener listener) {
        this.listener = listener;
    }
}
