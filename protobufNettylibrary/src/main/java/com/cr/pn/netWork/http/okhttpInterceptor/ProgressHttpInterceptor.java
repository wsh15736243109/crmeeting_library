package com.cr.pn.netWork.http.okhttpInterceptor;

import com.cr.pn.netWork.http.body.ProgressResponseBody;
import com.cr.pn.netWork.http.progressListener.ProgressListener;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by zy on 2018/7/9.
 */

public class ProgressHttpInterceptor implements Interceptor {

    private ConcurrentLinkedQueue<ProgressListener> progressListeners;

    public ProgressHttpInterceptor() {
        progressListeners = new ConcurrentLinkedQueue<>();
    }

    /**
     * 设置当前进度获取类
     * @param progressListener
     */
    public void setProgressListener(ProgressListener progressListener) {
        progressListeners.add(progressListener);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        ProgressListener progressListener = null;
        if (!progressListeners.isEmpty()){
            progressListener = progressListeners.poll();
        }
        ProgressResponseBody progressResponseBody = new ProgressResponseBody(response.body(),progressListener);
        progressListener = null;
        return response.newBuilder().body(progressResponseBody).build();
    }
}
