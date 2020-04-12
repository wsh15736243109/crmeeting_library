package com.cr.pn.netWork.http.RxUtils;

import com.cr.pn.netWork.http.body.UploadRequestBody;
import com.cr.pn.netWork.http.callback.FilesToPartsCallback;
import com.cr.pn.netWork.http.progressListener.ProgressListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by zy on 2018/7/9.
 * okhttpBody工具类.
 */
public class BodyUtils {

    /**
     * 多文件转MultipartBody.Part列表
     * @param files
     * @return
     */
    public static List<MultipartBody.Part> filesToMultipartBodyParts(LinkedHashMap<String,File> files) {
        return filesToMultipartBodyParts(files,null,null);
    }

    /**
     * 多文件转MultipartBody.Part列表
     * @param files
     * @param progressListener
     * 设置进度检测.
     * @return
     */
    public static List<MultipartBody.Part> filesToMultipartBodyParts(LinkedHashMap<String,File> files, ProgressListener progressListener) {
        return filesToMultipartBodyParts(files,null,progressListener);
    }

    /**
     * 多文件转MultipartBody.Part列表
     * 自定义fileMediaType.
     * @param files
     * @param progressListener
     * 设置进度检测.
     * @return
     */
    public static List<MultipartBody.Part> filesToMultipartBodyParts(LinkedHashMap<String,File> files, FilesToPartsCallback partsCallback, ProgressListener progressListener) {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        Iterator iter = files.entrySet().iterator();
        while (iter.hasNext()) {
            String fileMediaType = "application/otcet-stream";
            Map.Entry entry = (Map.Entry) iter.next();
            String key = (String) entry.getKey();
            File val = (File) entry.getValue();
            if (partsCallback != null){
                fileMediaType = partsCallback.setFileMediaType(key);
            }
            MultipartBody.Part part = filesToMultipartBodyParts(val,key,fileMediaType,progressListener);
            parts.add(part);
        }
        return parts;
    }

    /**
     * 文件转MultipartBody.Part,设置进度监听器.
     * @return
     */
    public static MultipartBody.Part filesToMultipartBodyParts(File file, String key, String fileMediaType, ProgressListener progressListener) {
        RequestBody requestBody = RequestBody.create(MediaType.parse(fileMediaType), file);
        UploadRequestBody uploadRequestBody = new UploadRequestBody(requestBody,progressListener);
        MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), uploadRequestBody);
        return part;
    }

    /**
     * 文件转MultipartBody.Part,设置进度监听器.
     * @return
     */
    public static MultipartBody.Part filesToMultipartBodyParts(File file,String key, ProgressListener progressListener) {
        String fileMediaType = "application/otcet-stream";
        return filesToMultipartBodyParts(file,key,fileMediaType,progressListener);
    }

    /**
     * 文件转MultipartBody.Part,不设置进度监听器.
     * @return
     */
    public static MultipartBody.Part filesToMultipartBodyParts(File file,String key) {
        String fileMediaType = "application/otcet-stream";
        return filesToMultipartBodyParts(file,key,fileMediaType,null);
    }


    /**
     * 获取一个文件上传时的RequestBody,自定义描述.
     * @return
     */
    public static RequestBody getRequestBodyUpload(String description){
        return getRequestBody("multipart/form-data",description);
    }

    /**
     * 获取一个文件上传时的RequestBody.
     * @return
     */
    public static RequestBody getRequestBodyUpload(){
        return getRequestBodyUpload("this a description");
    }

    /**
     * 获取一个RequestBody,自定义mediaType.
     * @param value
     * @param mediaType
     * @return
     */
    public static RequestBody getRequestBody(String value,String mediaType){
        return RequestBody.create(MediaType.parse(mediaType), value);
    }

    /**
     * 获取一个Payload传参的RequestBody.
     * @param value
     * 需要上传的参数.
     * @return
     */
    public static RequestBody getRequestBodyPayload(String value){
        return getRequestBody(value,"application/json; charset=utf-8");
    }

    /**
     * 获取一个RequestBody,默认为text类型.
     * @param value
     * @return
     */
    public static RequestBody getRequestBodyText(String value){
        return getRequestBody(value,"text/plain");

    }

}
