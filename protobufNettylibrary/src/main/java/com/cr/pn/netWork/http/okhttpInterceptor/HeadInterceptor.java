package com.cr.pn.netWork.http.okhttpInterceptor;

import com.cr.pn.netWork.http.headMap.HeadMap;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zy on 2018/7/31.
 * 请求头拦截.
 */
public class HeadInterceptor implements Interceptor {


    private ConcurrentLinkedQueue<HeadMap> headMaps;

    public HeadInterceptor(){
        headMaps = new ConcurrentLinkedQueue<>();
    }

    public void setHeadMaps(HeadMap headMap) {
        headMaps.add(headMap);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Iterator map1it=chain.request().headers().names().iterator();
        Request.Builder requestBuilder = chain.request().newBuilder();
        while(map1it.hasNext()){
            Map.Entry<String, String> entry=(Map.Entry<String, String>) map1it.next();
            requestBuilder.addHeader(entry.getKey(),entry.getValue());
        }
        if (!headMaps.isEmpty()){
            requestBuilder = headMaps.poll().requestHead(requestBuilder);
        }
        return chain.proceed(requestBuilder.build());
    }

}
