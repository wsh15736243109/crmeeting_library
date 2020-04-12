package com.cr.pn.Utils.log;

import android.text.TextUtils;

import com.cr.pn.FileAndMemory.FileUtil;
import com.cr.pn.netWork.http.okhttpInterceptor.LogInterceptor;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.io.File;

/**
 * Created by zy on 2018/1/13.
 */
public class MyLog {

    private static boolean isPrinting = false;

    /**
     * 初始化Logger,返回设置类.
     * @return
     */
    public static void initLogger(String tag){
        setIsPrinting(true);
        initLogger(tag,LogInterceptor.Level.BODY);
    }

    /**
     * 初始化Logger,返回设置类.
     * @return
     */
    public static void initLogger(String tag,LogInterceptor.Level level){
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .methodCount(4)
                .showThreadInfo(true)
                .tag(tag)
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
        LogInterceptor.level = level;
        setIsPrinting(true);
    }

    /**
     * 保存Log日志到文件.
     * @param cacheDiaPath
     * @param message
     */
    public static void saveFile(String cacheDiaPath, String message){
        if (TextUtils.isEmpty(cacheDiaPath)) {
            return;
        }
        try {
            FileUtil.writeTxtFile(message,new File(cacheDiaPath),true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**********orhanobut 日志打印,详细使用请搜索orhanobut***********/
    public static void loggerD(Object log){
        if (!isIsPrinting()){
            return;
        }
        Logger.d(log);
    }

    public static void loggerD(String message,Object... log){
        if (!isIsPrinting()){
            return;
        }
        Logger.d(message,log);
    }

    public static void loggerV(String message,Object... log){
        if (!isIsPrinting()){
            return;
        }
        Logger.v(message,log);
    }

    public static void loggerE(String message,Object... log){
        if (!isIsPrinting()){
            return;
        }
        Logger.e(message,log);
    }

    public static void loggerE(Throwable throwable, String message,Object... log){
        if (!isIsPrinting()){
            return;
        }
        Logger.e(throwable,message,log);
    }

    public static void loggerT(String log){
        if (!isIsPrinting()){
            return;
        }
        Logger.t(log);
    }

    public static void loggerW(String message,Object... log){
        if (!isIsPrinting()){
            return;
        }
        Logger.w(message,log);
    }

    public static void loggerWTF(String message,Object... log){
        if (!isIsPrinting()){
            return;
        }
        Logger.wtf(message,log);
    }

    public static void loggerJson(String json){
        if (!isIsPrinting()){
            return;
        }
        Logger.json(json);
    }

    public static void loggerXml(String xml){
        if (!isIsPrinting()){
            return;
        }
        Logger.xml(xml);
    }

    public static boolean isIsPrinting() {
        return isPrinting;
    }

    public static void setIsPrinting(boolean isPrinting) {
        MyLog.isPrinting = isPrinting;
    }
}
