package com.cr.pn.rxjava.apiObserver;


import androidx.collection.SimpleArrayMap;

import com.cr.pn.Utils.ViewUtils.StringUtil;
import com.cr.pn.exception.ApiException;
import com.cr.pn.netWork.http.RxUtils.BodyUtils;
import com.cr.pn.netWork.http.progressListener.ProgressListener;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

/**
 * Created by zy on 2018/7/10.
 * Rxjava文件上传订阅
 */

public abstract class ApiObserverFileUpload extends ApiObserverProgress<ResponseBody> {

    private UploadBuilder uploadBuilder;

    private ProgressListener observerFileUpload;

    public ApiObserverFileUpload(){
       this(true);
    }

    public ApiObserverFileUpload(boolean isOpenWait) {
        super(isOpenWait);
        init();
    }

    public ApiObserverFileUpload(SimpleArrayMap<String, String> map) {
        this(map,true);
    }

    public ApiObserverFileUpload(SimpleArrayMap<String, String> map,boolean isOpenWait) {
        super(map,isOpenWait);
        init();
    }

    private void init(){
        observerFileUpload = this;
        uploadBuilder = setUploadBuilder(this);
    }

    public ProgressListener getObserverFileUpload() {
        return observerFileUpload;
    }

    public abstract UploadBuilder setUploadBuilder(ApiObserverFileUpload observerFileUpload);

    public UploadBuilder getUploadBuilder() {
        return uploadBuilder;
    }

    public class UploadBuilder{

        //url
        private String url;

        //okhttp上传文件工具类
        private List<MultipartBody.Part> files;
        private MultipartBody.Part file;

        //参数.
        private Map<String, String> data;

        /**
         * 自定义所有参数.
         * 可使用BodyUtils类里面的getRequestBodyUpload进行获取RequestBody
         * @param url
         * @param files
         * @param data
         */
        public UploadBuilder(String url, List<MultipartBody.Part> files, Map<String, String> data){
            this.url = url;
            this.files = files;
            this.data = data;
        }

        /**
         * 默认所有参数.
         * @param url
         * @param files
         * @param data
         */
        public UploadBuilder(String url, LinkedHashMap<String,File> files, Map<String, String> data){
            this(url,BodyUtils.filesToMultipartBodyParts(files,observerFileUpload),data);
        }

        /******************单文件上传构建******************/

        /**
         * 自定义所有参数.
         * 可使用BodyUtils类里面的getRequestBodyUpload进行获取RequestBody
         * @param url
         * @param file
         * @param data
         */
        public UploadBuilder(String url, MultipartBody.Part file, Map<String, String> data){
            this.url = url;
            this.file = file;
            this.data = data;
        }

        /**
         * 默认所有.
         * @param url
         * @param file
         * @param fileKey
         * @param data
         */
        public UploadBuilder(String url, File file, String fileKey, Map<String, String> data){
            this(url,BodyUtils.filesToMultipartBodyParts(file,fileKey,observerFileUpload),data);
        }

        public String getUrl() throws ApiException {
            if (StringUtil.isEmpty(url)){
                throw new ApiException("url is null");
            }
            return url;
        }

        public List<MultipartBody.Part> getFiles() {
            return files;
        }

        public Map<String, String> getData() {
            return data;
        }

        public MultipartBody.Part getFile() {
            return file;
        }
    }

}
