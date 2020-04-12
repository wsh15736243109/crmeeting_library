package com.cr.pn.Utils.Media;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import androidx.collection.ArrayMap;

import java.util.Map;

/**
 * Created by zy on 2017/7/21.
 */

public class GetMedia {

    /**
     * 读取手机中Video的文件.
     * @param context
     * @return
     */
    public static Map<String, String[]> getVideoPath(Activity context){
        Map<String,String[]> pathInfor = new ArrayMap<>();
        String[] path;
        String[] title1;
        String[] timeMax;
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DURATION}, null, null, null);
        context.startManagingCursor(cursor);
        if (cursor == null || cursor.getCount() == 0) {
            path = new String[1];
            title1 = new String[1];
            timeMax = new String[1];
            title1[0] = "没有视频信息";
            path[0] = "未在视频路径找到音乐";
            timeMax[0]="0";
            pathInfor.put("path",path);
            pathInfor.put("title",title1);
            pathInfor.put("playTime",timeMax);
        } else {
            cursor.moveToFirst();
            path = new String[cursor.getCount()];
            title1 = new String[cursor.getCount()];
            timeMax = new String[cursor.getCount()];
            for (int i = 0; i < cursor.getCount(); i++) {
                path[i] = cursor.getString(0);
                int s = cursor.getString(1).length()-4;
                title1[i] = cursor.getString(1).substring(0,s);
                timeMax[i] = cursor.getInt(2)+"";
                cursor.moveToNext();
                pathInfor.put("path",path);
                pathInfor.put("title",title1);
                pathInfor.put("playTime",timeMax);
            }
        }
        return pathInfor;
    }

    /**
     * 读取手机中音频的文件.
     * @param context
     * @return
     */
    public static Map<String, String[]> getAudioPath(Activity context){
        Map<String,String[]> pathInfor = new ArrayMap<>();
        String[] path;
        String[] title1;
        String[] timeMax;
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Media.DATA,MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DURATION}, null, null, null);
        context.startManagingCursor(cursor);
        if (cursor == null || cursor.getCount() == 0) {
            path = new String[1];
            title1 = new String[1];
            timeMax = new String[1];
            title1[0] = "没有音乐信息";
            path[0] = "未在音乐路径找到音乐";
            timeMax[0]="0";
            pathInfor.put("path",path);
            pathInfor.put("title",title1);
            pathInfor.put("playTime",timeMax);
        } else {
            cursor.moveToFirst();
            path = new String[cursor.getCount()];
            title1 = new String[cursor.getCount()];
            timeMax = new String[cursor.getCount()];
            for (int i = 0; i < cursor.getCount(); i++) {
                path[i] = cursor.getString(0);
                int s = cursor.getString(1).length()-4;
                title1[i] = cursor.getString(1).substring(0,s);
                timeMax[i] = cursor.getInt(2)+"";
                cursor.moveToNext();
                pathInfor.put("path",path);
                pathInfor.put("title",title1);
                pathInfor.put("playTime",timeMax);
            }
        }
       return pathInfor;
    }

}
