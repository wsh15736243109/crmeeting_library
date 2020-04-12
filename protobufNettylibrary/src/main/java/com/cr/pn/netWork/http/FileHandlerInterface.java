package com.cr.pn.netWork.http;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by zy on 2018/7/9.
 * http文件上传下载接口.
 * 为了返回数据灵活性
 * 所有返回全部为ResponseBody,
 * 请自行进行数据转为实体类.
 */
public interface FileHandlerInterface {

    /**************************************************上传文件*****************************************/

    /**
     * 上传文件，附带参数.
     * @param url
     * @param file
     * @param data
     * @return
     */
    @Multipart
    @POST()
    Observable<ResponseBody> uploadFileParameter(@Url String url,
                                                 @Part MultipartBody.Part file, @PartMap Map<String, RequestBody> data);

    /**
     * 上传文件,不带参数
     * @param url
     * @param file
     * @return
     */
    @Multipart
    @POST()
    Observable<ResponseBody> uploadFile(@Url String url,
                                        @Part MultipartBody.Part file);

    /**
     * 上传多个文件
     * @param url
     * @param files
     * @return
     */
    @Multipart
    @POST()
    Observable<ResponseBody> uploadFiles(@Url String url,
                                         @Part List<MultipartBody.Part> files);

    /**
     * 上传多个文件，附带参数.
     * @param url
     * @param files
     * @param data
     * @return
     */
    @Multipart
    @POST()
    Observable<ResponseBody> uploadFilesParameter(@Url String url,
                                                  @Part List<MultipartBody.Part> files, @PartMap Map<String, RequestBody> data);


    /*************************************************下载文件*******************************************/

    /**
     * 下载文件,get方式访问.
     * @param url
     * @return
     */
    @Streaming
    @GET()
    Observable<ResponseBody> downFileGet(@Url String url);

    /**
     * 下载文件,get方式访问，附带参数.
     * @param url
     * @return
     */
    @Streaming
    @GET()
    Observable<ResponseBody> downFileGetParameter(@Url String url, @QueryMap Map<String, String> data);

    /**
     * 下载文件,post方式访问.
     * @param url
     * @return
     */
    @Streaming
    @POST()
    Observable<ResponseBody> downFilePost(@Url String url);

    /**
     * 下载文件,post方式访问,附带参数.
     * @param url
     * @return
     */
    @Streaming
    @POST()
    Observable<ResponseBody> downFilePostParameter(@Url String url, @QueryMap Map<String, String> data);

}
