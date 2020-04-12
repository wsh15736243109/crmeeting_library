package com.cr.pn.netWork.http.okhttpInterceptor.Interceptor;

import com.cr.pn.netWork.http.headMap.HeadMap;
import com.cr.pn.netWork.http.okHttp.OkHttpHelper;
import com.cr.pn.netWork.http.okhttpInterceptor.HeadInterceptor;
import com.cr.pn.netWork.http.okhttpInterceptor.LogInterceptor;
import com.cr.pn.netWork.http.okhttpInterceptor.ProgressHttpInterceptor;
import com.cr.pn.netWork.http.progressListener.ProgressListener;

import java.util.LinkedList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Created by zy on 2018/7/31.
 * 拦截管理.
 */
public class InterceptorManager {

    private static InterceptorManager INSTANCE;

    private ProgressHttpInterceptor progressHttpInterceptor = null;

    private HeadInterceptor headInterceptor = null;

    private LogInterceptor logInterceptor = null;

    private List<Interceptor> interceptors;

    public static InterceptorManager getInstance() {
        if (INSTANCE == null) {
            synchronized (InterceptorManager.class) {
                INSTANCE = new InterceptorManager();
            }
        }
        return INSTANCE;
    }

    private InterceptorManager() {
        progressHttpInterceptor = new ProgressHttpInterceptor();
        headInterceptor = new HeadInterceptor();
        logInterceptor = new LogInterceptor();
        interceptors = new LinkedList<>();
        interceptors.add(progressHttpInterceptor);
        interceptors.add(headInterceptor);
        interceptors.add(logInterceptor);
    }

    /**
     * 设置拦截器.
     */
    public void structureBuilder() {
        OkHttpClient.Builder newBuilder = OkHttpHelper.getInstance().getInitBuilder();
        structureBuilder(newBuilder);
    }

    /**
     * 设置拦截器.
     */
    public void structureBuilder(OkHttpClient.Builder builder) {
        List<Interceptor> interceptorList = builder.interceptors();
        for (Interceptor interceptor : interceptors) {
            if (!interceptorList.contains(interceptor)) {
                builder.addInterceptor(interceptor);
            }
        }
        OkHttpHelper.getInstance().initBuilder(builder);
    }

    /**
     * 设置http进度拦截器.
     *
     * @param progressListener
     */
    public void setHttpProgress(ProgressListener progressListener) {
        progressHttpInterceptor.setProgressListener(progressListener);
    }

    /**
     * 设置http请求头拦截器.
     *
     * @param httpHead
     */
    public void setHttpHead(HeadMap httpHead) {
        headInterceptor.setHeadMaps(httpHead);
    }

    /**
     * 添加拦截器.
     *
     * @param interceptor
     */
    public void setInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
        structureBuilder();
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }
}
