package com.cr.pn.rxjava.apiObserver;


import androidx.collection.SimpleArrayMap;

import com.cr.pn.exception.ApiException;
import com.cr.pn.netWork.http.headMap.HeadMap;
import com.google.gson.JsonSyntaxException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import okhttp3.Request;
import retrofit2.HttpException;

/**
 * Created by zy on 2018/6/9.
 */

public abstract class ApiObserver<T> extends RxObserver<T> implements HeadMap {

    public static final String ERROR_UNKNOWN = "网络异常，请检查网络!";
    public static final String ERROR_TIMEOUT = "连接超时!";
    public static final String ERROR_JSON = "数据解析异常!";
    public static final String ERROR_HTTP = "服务器错误!";

    public SimpleArrayMap<String,String> arrayMap;

    public T t;


    public ApiObserver(){
        this(false);//是否显示 网络加载框
    }

    public ApiObserver(boolean isOpenWait){
        super(isOpenWait);
    }

    /**
     * 请求头.
     * @param map
     */
    public ApiObserver(SimpleArrayMap<String,String> map){
        this(map,true);
    }

    public ApiObserver(SimpleArrayMap<String,String> map,boolean isOpenWait){
        super(isOpenWait);
        if (map != null&&map.size()>0){
            this.arrayMap = map;
        }
    }


    @Override
    public Request.Builder requestHead(Request.Builder builder) {
        if (arrayMap!=null&&arrayMap.size()>0){
            for (int i = 0;i<arrayMap.size();i++){
                String key = arrayMap.keyAt(i);
                String value = arrayMap.valueAt(i);
                builder.addHeader(key,value);
            }
        }
        return builder;
    }

    /**
     * api中网络请求错误
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        super.onError(e);
        if (e instanceof ApiException) {
            Error(e,e.getMessage());
        }else if (e instanceof ConnectException || e instanceof UnknownHostException) {
            Error(e,ERROR_UNKNOWN);
        } else if (e instanceof TimeoutException || e instanceof SocketTimeoutException) {
            Error(e,ERROR_TIMEOUT);
        } else if (e instanceof JsonSyntaxException) {
            Error(e,ERROR_JSON);
        } else if (e instanceof HttpException) {
            ((HttpException)e).printStackTrace();
            Error(e,ERROR_HTTP);
        } else {
            Error(e,e.getMessage());
        }
        e.printStackTrace();
    }
}
