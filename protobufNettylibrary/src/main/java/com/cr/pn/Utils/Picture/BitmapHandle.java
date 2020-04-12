package com.cr.pn.Utils.Picture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by zy on 2017/8/11.
 */

public class BitmapHandle {

    /**
     * @param image
     * Bitmap
     * @param ro
     * 缩放大小比例.
     * @return
     */
    public static Bitmap compressBitmapC(Bitmap image,int ro) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, os);
        if( os.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            os.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, os);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int be = ro;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        is = new ByteArrayInputStream(os.toByteArray());
        bitmap = BitmapFactory.decodeStream(is, null, newOpts);
        return bitmap;
    }


    /**
     * 质量压缩（Bitmap的大小不会变化，但保存为file文件时，文件大小会发生变化）
     * @param image
     * Bitmap
     * @param max
     * max
     * @return
     */
    public static Bitmap compressBitmapZ(Bitmap image,int max) {
        int maxSize = max;
        Bitmap result = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        result.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        while (baos.toByteArray().length / 1024 > maxSize) {
            baos.reset();
            quality -= 10;
            result.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        }
        return  result;
    }


    /**
     * 根据地址获取图片.
     * @param filePath
     * @return
     */
    public static Bitmap getBitmapFromFile(String filePath){
        return getBitmapFromFile(filePath,false);
    }

    /**
     * 通过文件路径读获取Bitmap防止OOM以及解决图片旋转问题
     * @param filePath
     * @return
     */
    public static Bitmap getBitmapFromFile(String filePath,boolean isXZ){
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        BitmapFactory.decodeFile(filePath, newOpts);
        newOpts.inJustDecodeBounds = false;//读取所有内容
        newOpts.inDither = false;
        newOpts.inPurgeable=true;
        newOpts.inInputShareable=true;
        newOpts.inTempStorage = new byte[32 * 1024];
        Bitmap bitmap = null;
        File file = new File(filePath);
        FileInputStream fs = null;
        try {
            fs = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if(fs!=null){
                bitmap = BitmapFactory.decodeFileDescriptor(fs.getFD(),null,newOpts);
                //旋转图片
                if (isXZ){
                    int photoDegree = readPictureDegree(filePath);
                    if(photoDegree != 0){
                        Matrix matrix = new Matrix();
                        matrix.postRotate(photoDegree);
                        // 创建新的图片
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    }
                }else {
                    Matrix matrix = new Matrix();
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0,
                            bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(fs!=null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    /**
     *
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
}
