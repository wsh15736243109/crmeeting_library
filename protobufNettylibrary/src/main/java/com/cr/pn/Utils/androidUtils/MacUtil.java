package com.cr.pn.Utils.androidUtils;

import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * @author wsh
 * @version 1.0
 * @date 2020/3/24 16:54
 */
public class MacUtil {
    public static String getMac() {
        String mac = readAddress("cat /sys/class/net/eth0/address");
        if (TextUtils.isEmpty(mac)) {
            mac = readAddress("cat /sys/class/net/wlan0/address");
        }
        return mac;
    }

    /**
     * 这是使用adb shell命令来获取mac地址的方式
     *
     * @return
     */
    public static String readAddress(String cmd) {
        String macSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec(cmd);
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim().toUpperCase();// 去空格 转大写
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }

}
