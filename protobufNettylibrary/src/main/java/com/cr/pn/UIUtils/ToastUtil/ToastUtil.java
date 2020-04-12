package com.cr.pn.UIUtils.ToastUtil;

import android.content.Context;
import android.widget.Toast;

import com.cr.pn.Utils.ViewUtils.StringUtil;

/**
 * Created by zy on 2017/2/7.
 * ToastUtil.
 */
public class ToastUtil {

    /**
     * Toast提示.
     * @param context
     * @param text
     * 文本.
     */
    public static void showToast(Context context,String text){
        if (!StringUtil.isEmpty(text) && context != null){
            CustomToast.makeText(context,text+"",Toast.LENGTH_SHORT);
        }
    }

    /**
     * Toast提示.
     * @param context
     * @param id
     * 文本id.
     */
    public static void showToast(Context context,int id){
        CustomToast.makeText(context,context.getResources().getText(id)+"",Toast.LENGTH_SHORT);
    }
}
