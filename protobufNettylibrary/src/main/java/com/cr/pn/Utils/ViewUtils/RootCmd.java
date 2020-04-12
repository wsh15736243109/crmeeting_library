package com.cr.pn.Utils.ViewUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;

/**
 * Created by zy on 2017/7/4.
 */

public final class RootCmd {

    /**
     * cmd获取mac
     * @return
     */
    public static String getMac(){
        String mac = "cat /sys/class/net/wlan0/address ";
        return rootCmd(mac);
    }

    /**
     * 判断是否有root.
     * @return
     */
    public static boolean is_root() {

        boolean res = false;
        try {
            if ((!new File("/system/bin/su").exists()) &&
                    (!new File("/system/xbin/su").exists())) {
                res = false;
            } else {
                res = true;
            }
            ;
        } catch (Exception e) {

        }
        return res;

    }

    /**
     * 写root命令
     * @param cmd
     * @return
     * 返回结果.
     */
    public static String rootCmd(String cmd){
        Runtime r;
        Process p;
        String str = "";
        try {
            r= Runtime.getRuntime();
            p=r.exec("su");
            OutputStream os=p.getOutputStream();
            DataOutputStream dos=new DataOutputStream(os);
            InputStreamReader ir = new InputStreamReader(p.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            dos.writeBytes(cmd+"\n");
            dos.writeBytes("exit\n");
            dos.flush();
            for (; null != str;) {
                str = input.readLine();
                if (str != null) {
                    str = str.trim();// 去空格
                    break;
                }
            }
            ir.close();
            input.close();
            dos.close();
            os.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

}