package com.cr.pn.netWork.http.okHttp;

import com.cr.pn.netWork.http.okhttpInterceptor.Interceptor.InterceptorManager;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Created by zy on 2018/7/6.
 */

public class OkHttpHelper {

    private OkHttpClient client;

    //设置超时时间
    private long DEFAULT_TIMEOUT = 15000;

    private static OkHttpHelper INSTANCE;

    public static OkHttpHelper getInstance() {
        if (INSTANCE == null) {
            synchronized (OkHttpHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new OkHttpHelper();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 初始化okHttp、retrofit.
     */
    private OkHttpHelper() {
        if (client == null) {
            OkHttpClient.Builder build = new OkHttpClient.Builder()
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                    .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                    .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
            for (Interceptor interceptor : InterceptorManager.getInstance().getInterceptors()) {
                build.addInterceptor(interceptor);
            }
            client = build.build();
        }
    }

    /**
     * 重新初始化Builder
     *
     * @param builder
     */
    public void initBuilder(OkHttpClient.Builder builder) {
        if (builder == null) {
            throw new NullPointerException("OkHttpClient.Builder == null");
        }
        client = builder.build();
    }

    /**
     * 返回初始化的Builder
     *
     * @return
     */
    public OkHttpClient.Builder getInitBuilder() {
        return client.newBuilder();
    }

    public OkHttpClient getClient() {
        return client;
    }

    /**
     * 设置okHttp
     *
     * @param client
     */
    public void initHttp(OkHttpClient client) {
        this.client = client;
    }

}
