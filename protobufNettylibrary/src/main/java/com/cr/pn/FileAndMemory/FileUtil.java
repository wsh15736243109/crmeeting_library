package com.cr.pn.FileAndMemory;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.cr.pn.Utils.MyException.MyException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

/**
  * 类名称：FileUtil
  * 类描述：获取某某路径下的文件及文件夹的大小
  * 作者：FCFRT
  * 创建时间： 2017/7/18-11:17
  * 邮箱：CQZZ_ZZPT@163.COM
  * 修改简介：
  */
public class FileUtil {

    public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormetFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormetFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     *
     * @param
     * @return
     * @throws Exception
     */
    public static long getFileSize(File file) throws Exception {
        long size = -1;
        if (file.exists()) {
            size = file.length();
        }
        return size;
    }

    /**
     * 获取指定文件大小
     *
     * @param
     * @return
     * @throws Exception
     */
    public static long getFileSize(String path) throws Exception {
        File file = new File(path);
        return getFileSize(file);
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    private static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

     /**
      * 创建文件夹.
      * @param sDir
      */
     public static File createFile(String sDir){
         File destDir = new File(sDir);
         if (!destDir.exists()) {
             destDir.mkdirs();
         }
         return destDir;
     }

     /**
      * 删除文件夹.
      */
     public static boolean delete(String sDir){
         if(isCreate(sDir)){
             File file = new File(sDir);
             return file.delete();
         }
         return true;
     }

     /**
      * 删除文件夹(包括内部所有文件).
      */
     public static void deleteAll(String sDir){
         if (isCreate(sDir)){
             File file = new File(sDir);
             File[] files = file.listFiles();
             if (files!=null){
                 for (int i = 0;i<files.length;i++){
                     if (files[i].listFiles()!=null){
                         deleteAll(files[i].getPath());
                     }else {
                         delete(files[i].getPath());
                     }
                 }
             }
             file.delete();
         }
     }

     /**
      * 拷贝文件.
      */
     public static void copyFile(String oldFile,String newFile){
         if (!isCreate(oldFile)){
             new MyException("没有该文件");
         }else {
             try {
                 FileInputStream fileInputStream = new FileInputStream(new File(oldFile));
                 FileOutputStream fileOutputStream = new FileOutputStream(new File(newFile));
                 byte[] bytes = new byte[2014];
                 while(fileInputStream.read(bytes)!=-1){
                     fileOutputStream.write(bytes);
                 }
                 fileInputStream.close();
                 fileOutputStream.close();
             } catch (FileNotFoundException e) {
                 e.printStackTrace();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
     }

     /**
      * 判断文件夹是否创建
      * @param sDir
      * @return
      */
     public static boolean isCreate(String sDir){
         File destDir = new File(sDir);
         if (destDir.exists()) {
             return true;
         }
         return false;
     }

     /**
      * 判断是否有sdCard.
      * @return
      */
     public static boolean isSdCard(){
         String status = Environment.getExternalStorageState();
         if (status.equals(Environment.MEDIA_MOUNTED)) {
             return true;
         } else {
             return false;
         }
     }

     /**
      * 读取txt文件内容.
      * @param fileName
      * 文件夹名称.
      * @return
      * @throws Exception
      */
     public static String readTxtFile(File fileName)throws Exception{
         String result="";
         FileReader fileReader=null;
         BufferedReader bufferedReader=null;
         try{
             fileReader=new FileReader(fileName);
             bufferedReader=new BufferedReader(fileReader);
             try{
                 String read="";
                 while((read=bufferedReader.readLine())!=null){
                     result+=read;
                 }
             }catch(Exception e){
                 e.printStackTrace();
             }
         }catch(Exception e){
             e.printStackTrace();
         }finally{
             if(bufferedReader!=null){
                 bufferedReader.close();
             }
             if(fileReader!=null){
                 fileReader.close();
             }
         }
         return result;
     }

     /**
      * 写入txt文件.
      * @param content
      * 写入内容.
      * @param fileName
      * 文件夹名称.
      * @param append
      * true：不删除源有内容，false：删除源有内容
      */
     public static void writeTxtFile(String content,File fileName,boolean append)throws Exception{
         FileWriter fileWriter = new FileWriter(fileName,append);
         fileWriter.write(content);
         fileWriter.flush();
         fileWriter.close();
     }

     /**
      * Uri转File路径.
      * @param context
      * @param uri
      * @return
      */
     public static String getRealFilePath(Context context, Uri uri ) {
         if ( null == uri ) return null;
         String scheme = uri.getScheme();
         String data = null;
         if ( scheme == null )
             data = uri.getPath();
         else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
             data = uri.getPath();
         } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
             Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
             if ( null != cursor ) {
                 if ( cursor.moveToFirst() ) {
                     int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                     if ( index > -1 ) {
                         data = cursor.getString( index );
                     }
                 }
                 cursor.close();
             }
         }
         return data;
     }

    /**
     * 获取缓存目录
     * @param context
     * @return
     */
    public static String getCachePath(Context context ){
        String cachePath ;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //外部存储可用
            cachePath = context.getExternalCacheDir().getPath() ;

        }else {
            //外部存储不可用
            cachePath = context.getCacheDir().getPath() ;
        }
        return cachePath ;
    }

    /***
     * 获取文件类型
     *
     * @param paramString
     * @return
     */
    public  static String getFileType(String paramString) {
        String str = "";

        if (TextUtils.isEmpty(paramString)) {
            Log.d("print", "paramString---->null");
            return str;
        }
        Log.d("print", "paramString:" + paramString);
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            Log.d("print", "i <= -1");
            return str;
        }

        str = paramString.substring(i + 1);
        Log.d("print", "paramString.substring(i + 1)------>" + str);
        return str;
    }
 }