package com.cr.pn.netWork.http.HttpServiceHelper;


import androidx.collection.ArrayMap;

import com.cr.pn.exception.ApiException;
import com.cr.pn.netWork.http.FileHandlerInterface;
import com.cr.pn.netWork.http.RxUtils.BodyUtils;
import com.cr.pn.netWork.http.headMap.HeadMap;
import com.cr.pn.netWork.http.okhttpInterceptor.Interceptor.InterceptorManager;
import com.cr.pn.netWork.http.progressListener.ProgressListener;
import com.cr.pn.netWork.http.retrofitInit.RetrofitInit;
import com.cr.pn.rxjava.apiObserver.ApiObserverFileDown;
import com.cr.pn.rxjava.apiObserver.ApiObserverFileUpload;
import com.cr.pn.rxjava.httpRxjava.NetRxHelper;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.functions.Function;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by zy on 2018/7/6.
 * http接口访问辅助类.
 */

public class HttpServiceHelper<T>  {

    private T t = null;

    private FileHandlerInterface fileInterface;

    private NetRxHelper netRxHelper;

    public HttpServiceHelper(T t){
        this.t = t;
        this.netRxHelper = new NetRxHelper();
    }

    public HttpServiceHelper(){
        this.netRxHelper = new NetRxHelper();
    }

    /**
     * 获取Rxjava具体实现.
     * @return
     */
    public NetRxHelper getNetRxHelper() {
        return netRxHelper;
    }

    /**
     * 返回文件http处理接口实例
     * @return
     */
    public FileHandlerInterface getFileInterface() {
        return fileInterface;
    }

    /**
     * 初始化通用网络文件处理接口
     */
    public void initFileInterface(){
        fileInterface = RetrofitInit.getInstance().createServiceInterface(FileHandlerInterface.class);
    }

    /**
     * 返回网络接口实例.
     * @return
     */
    public T getT() {
        return t;
    }

    /**
     * 返回网络接口操作实例.
     * @return
     */
    public HttpServiceHelper httpServiceHelper(){
        return this;
    }

    /******************************************************网络数据获取***************************************************/

    /**
     * Rxjava+Retrofit获取数据,订阅者和被订阅者的泛型默认相同.
     * @param observable
     * @param observer
     * @param <D>
     */
    public <D> void httpRequest(Observable<D> observable, Observer<D> observer){
        observer = structure(observer);
        netRxHelper.DataAcquisition(observable,observer);
    }

    /**
     * Rxjava+Retrofit获取数据.
     * 数据获取,订阅者和被订阅者的泛型默认不相同，但转换时会默认相同.
     * @param observable
     * @param observer
     * @param <T>
     */
    public <T,F> void httpRequestT2D(Observable<T> observable, Observer<F> observer){
        observer = structure(observer);
        httpRequestT2D(observable,observer,null);
    }

    /**
     * Rxjava+Retrofit获取数据.
     * 数据获取,订阅者和被订阅者的泛型不同，并自定义转换.
     * @param observable
     * @param observer
     * @param <T>
     */
    public <T,F> void httpRequestT2D(Observable<T> observable, Observer<F> observer, Function<T,F> function){
        observer = structure(observer);
        netRxHelper.DataAcquisitionFunction(observable,observer,function);
    }

    /************************************************下载文件*********************************************/

    /**
     * 下载文件get方法.
     */
    public void downFileGet(ApiObserverFileDown observer){
        if (fileInterface != null){
            String url = "";
            try {
                url = observer.getDownFileBuilder().getUrl();
            } catch (ApiException e) {
                observer.onError(e);
            }
            String path = observer.getDownFileBuilder().getFilePath();
            Map<String,String> map = observer.getDownFileBuilder().getMap();
            observer = (ApiObserverFileDown) structure(observer);
            String range = "bytes="+observer.getDownFileBuilder().getRange();
            observer.setRange(range);
            if (map!=null&&map.size()>0){
                netRxHelper.downFile(fileInterface.downFileGetParameter(url,map),observer,path);
            }else {
                netRxHelper.downFile(fileInterface.downFileGet(url),observer,path);
            }
        }
    }

    /**
     * 下载文件post方法.
     */
    public void downFilePost(ApiObserverFileDown observer){
        if (fileInterface != null){
            String url = "";
            try {
                url = observer.getDownFileBuilder().getUrl();
            } catch (ApiException e) {
                observer.onError(e);
            }
            observer = (ApiObserverFileDown) structure(observer);
            String path = observer.getDownFileBuilder().getFilePath();
            Map<String,String> map = observer.getDownFileBuilder().getMap();
            String range = "bytes="+observer.getDownFileBuilder().getRange();
            observer.setRange(range);
            if (map!=null&&map.size()>0){
                netRxHelper.downFile(fileInterface.downFilePostParameter(url,map),observer,path);
            }else {
                netRxHelper.downFile(fileInterface.downFilePost(url),observer,path);
            }
        }
    }

    /******************************************************文件上传*******************************************/

    /**
     * 文件上传,含参.
     */
    public void uploadFile(ApiObserverFileUpload observer){
        if (fileInterface != null) {
            String url = "";
            try {
                url = observer.getUploadBuilder().getUrl();
            } catch (ApiException e) {
                observer.onError(e);
            }
            observer = (ApiObserverFileUpload) structure(observer);
            List<MultipartBody.Part> files = observer.getUploadBuilder().getFiles();
            MultipartBody.Part file = observer.getUploadBuilder().getFile();
            Map<String,String> map =  observer.getUploadBuilder().getData();
            Observable<ResponseBody> observable = null;
            if (file != null){
                if(map==null||map.size()==0){
                    observable = fileInterface.uploadFile(url,file);
                }else {
                    Map<String,RequestBody> bodyMap = new ArrayMap<>();
                    Iterator iter = map.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        String key = (String) entry.getKey();
                        String val = (String) entry.getValue();
                        bodyMap.put(key, BodyUtils.getRequestBodyText(val));
                    }
                    observable = fileInterface.uploadFileParameter(url,file,bodyMap);
                }
            }else if (files!=null&&files.size()>0){
                if(map==null||map.size()==0){
                    observable = fileInterface.uploadFiles(url,files);
                }else {
                    Map<String,RequestBody> bodyMap = new ArrayMap<>();
                    Iterator iter = map.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        String key = (String) entry.getKey();
                        String val = (String) entry.getValue();
                        bodyMap.put(key, BodyUtils.getRequestBodyText(val));
                    }
                    observable = fileInterface.uploadFilesParameter(url,files,bodyMap);
                }
            }else {
                try {
                    throw new ApiException("MultipartBody.Part is null");
                } catch (ApiException e) {
                    e.printStackTrace();
                    observer.onError(e);
                }
            }
            netRxHelper.DataAcquisition(observable, observer);
        }
    }

    public Observer structure(Observer observer){
        if (observer instanceof ProgressListener){
            InterceptorManager.getInstance().setHttpProgress((ProgressListener) observer);
        }
        if (observer instanceof HeadMap){
            InterceptorManager.getInstance().setHttpHead((HeadMap) observer);
        }
        return observer;
    }

}
