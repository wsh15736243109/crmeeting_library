package com.cr.pn.rxjava.httpRxjava;

import com.cr.pn.exception.ApiException;
import com.cr.pn.rxjava.apiObserver.ApiObserverFileDown;
import com.cr.pn.rxjava.apiObserver.ApiObserverProgress;
import com.cr.pn.rxjava.defaultTransformer.DefaultFunctionTransformer;
import com.cr.pn.rxjava.defaultTransformer.DefaultTransformer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * Created by zy on 2018/7/6.
 * RxJava取具体实现.
 */

public class NetRxHelper implements NetRxInterface {

    /**
     * 数据获取,订阅者和被订阅者的泛型默认相同.
     * @param observable
     */
    @Override
    public <T> void DataAcquisition(Observable<T> observable,Observer<T> observer) {
        observable.compose(new DefaultTransformer<T>())
                .subscribe(observer);
    }

    /**
     * 数据获取,订阅者和被订阅者的泛型默认不相同，但转换时会默认相同.
     * @param observable
     */
    @Override
    public <T,F> void DataAcquisitionFunction(Observable<T> observable, Observer<F> observer) {
        observable.compose(new DefaultFunctionTransformer<T, F>(null))
                .subscribe(observer);
    }

    /**
     * 数据获取,订阅者和被订阅者的泛型不同，并自定义转换.
     * @param observable
     */
    @Override
    public <T,F> void DataAcquisitionFunction(Observable<T> observable, Observer<F> observer, Function<T,F> function) {
        observable.compose(new DefaultFunctionTransformer<T, F>(function))
                .subscribe(observer);
    }

    /**
     * 文件下载时Rxjava处理转换.
     *
     * @param observable
     */
    @Override
    public <T,F> void downFile(final Observable<T> observable, final ApiObserverProgress<F> observer, final String path) {
        DataAcquisitionFunction(observable, observer, new Function<T, F>() {
            @Override
            public F apply(T t) throws Exception {
                File futureStudioIconFile = new File(path);
                try {
                    if (t instanceof ResponseBody){
                        ResponseBody responseBody = (ResponseBody) t;
                        InputStream inputStream = null;
                        OutputStream outputStream = null;
                        try {
                            byte[] fileReader = new byte[4096];
                            long fileSize = responseBody.contentLength();
                            long fileSizeDownloaded = 0;
                            inputStream = responseBody.byteStream();
                            boolean renew = false;
                            if (observer instanceof ApiObserverFileDown){
                                String range = ((ApiObserverFileDown) observer).getDownFileBuilder().getRange();
                                renew = !range.equals("0-");
                            }
                            outputStream = new FileOutputStream(futureStudioIconFile,renew);
                            while (true) {
                                int read = inputStream.read(fileReader);
                                if (read == -1) {
//                                    observer.onProgress(fileSize,fileSize,true);
                                    break;
                                }
                                outputStream.write(fileReader, 0, read);
                                fileSizeDownloaded += read;
                            }
                            outputStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                            throw e;
                        } finally {
                            if (inputStream != null) {
                                inputStream.close();
                            }
                            if (outputStream != null) {
                                outputStream.close();
                            }
                            responseBody.close();
                        }
                    }else {
                        try {
                            throw new ApiException("Observable<T>,T is not ResponseBody");
                        } catch (ApiException e) {
                            e.printStackTrace();
                            observer.onError(e);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
                return (F) futureStudioIconFile;
            }

        });
    }

}
