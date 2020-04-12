package com.cr.pn;

import android.content.Context;
import android.widget.Toast;

/**
 * @author wsh
 * @version 1.0
 * @date 2020/4/10 18:14
 */
public class Main {
    public static void test(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
