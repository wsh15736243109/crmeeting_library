package com.cr.pn.netWork.http.retrofitInit;

import com.cr.pn.netWork.http.HttpServiceHelper.HttpServiceHelper;
import com.cr.pn.netWork.http.okHttp.OkHttpHelper;
import com.cr.pn.netWork.http.retrofitConverter.JsonConverterFactory;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by zy on 2018/7/2.
 * Retrofit构建
 */
public class RetrofitInit {

    //接口根地址
    public static String BASE_URL = "https://www.baidu.com";

    private static RetrofitInit INSTANCE;

    private static Retrofit retrofit;

    public static RetrofitInit getInstance(){
        if (INSTANCE == null){
            synchronized (RetrofitInit.class){
                if (INSTANCE == null){
                    INSTANCE = new RetrofitInit();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 初始化okHttp、retrofit.
     */
    private RetrofitInit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkHttpHelper.getInstance().getClient())
                .addConverterFactory(JsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public Retrofit getClient(){
        return retrofit;
    }

    /**
     * 获取根地址.
     * @return
     */
    public String getBASE_URL() {
        return BASE_URL;
    }

    /**
     * 返回根地址.
     * @param BASE_URL
     */
    public void setBASE_URL(String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }

    /**
     * 获取接口类实例化操作类对象.
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> HttpServiceHelper<T> createService(Class<T> clazz) {
        return new HttpServiceHelper<T>(createServiceInterface(clazz)).httpServiceHelper();
    }

    /**
     * 获取接口类实例化类对象.
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T createServiceInterface(Class<T> clazz) {
        return retrofit.create(clazz);
    }

}
