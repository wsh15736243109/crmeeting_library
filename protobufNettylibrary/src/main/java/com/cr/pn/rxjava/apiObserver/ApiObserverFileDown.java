package com.cr.pn.rxjava.apiObserver;


import androidx.collection.SimpleArrayMap;

import com.cr.pn.Utils.ViewUtils.StringUtil;
import com.cr.pn.exception.ApiException;

import java.io.File;
import java.util.Map;

/**
 * Created by zy on 2018/7/9.
 * Rxjava文件下载订阅.
 */

public abstract class ApiObserverFileDown extends ApiObserverProgress<File> {

    private DownFileBuilder downFileBuilder;

    public ApiObserverFileDown(){
        this(true);
    }

    public ApiObserverFileDown(boolean isOpenWait) {
        super(isOpenWait);
        downFileBuilder = setDownFileBuilder();
    }

    public ApiObserverFileDown(SimpleArrayMap<String, String> map) {
        this(map,true);
    }

    public ApiObserverFileDown(SimpleArrayMap<String, String> map, boolean isOpenWait) {
        super(map,isOpenWait);
        downFileBuilder = setDownFileBuilder();
    }

    public void setRange(String value){
        if (arrayMap == null){
            arrayMap = new SimpleArrayMap<>();
        }
        arrayMap.put("RANGE",value);
    }

    /**
     * 取消任务.
     */
    @Override
    public void cancel() {
        super.cancel();
    }

    public DownFileBuilder getDownFileBuilder() {
        return downFileBuilder;
    }

    public abstract DownFileBuilder setDownFileBuilder();

    public class DownFileBuilder{

        //url
        private String url;

        //文件地址
        private String filePath;

        //参数
        private Map<String,String> map;

        /**
         * 设置下载区间
         * 列如：1173546-2173546或者1173546-
         */
        private String range = "0-";

        public DownFileBuilder(String url,String filePath,Map<String,String> map,String range){
            this.url = url;
            this.filePath = filePath;
            this.map = map;
            if (!StringUtil.isEmpty(range)){
                this.range = range;
            }
        }

        /**
         * 创建传参的构建器.
         * @param url
         * @param filePath
         * @param map
         */
        public DownFileBuilder(String url,String filePath,Map<String,String> map){
            this(url,filePath,map,"");
        }

        /**
         * 创建断点续传的构建器.
         * @param url
         * @param filePath
         * @param range
         */
        public DownFileBuilder(String url,String filePath,String range){
            this(url,filePath,null,range);
        }

        /**
         * 创建普通下载构建器
         * @param url
         * @param filePath
         */
        public DownFileBuilder(String url,String filePath){
            this(url,filePath,"");
        }

        public String getUrl() throws ApiException {
            if (StringUtil.isEmpty(url)){
                throw new ApiException("url is null");
            }
            return url;
        }

        public String getRange() {
            return range;
        }

        public Map<String, String> getMap() {
            return map;
        }

        public String getFilePath() {
            return filePath;
        }

    }
}
