package com.cr.pn.netWork.http.body;

import com.cr.pn.netWork.http.progressListener.ProgressListener;

import java.io.IOException;

import io.reactivex.annotations.Nullable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by zy on 2018/7/10.
 */

public class UploadRequestBody extends RequestBody {

    private RequestBody requestBody;

    private BufferedSink bufferedSink;

    private ProgressListener progressListener;

    public UploadRequestBody(RequestBody requestBody , ProgressListener progressListener){
        this.requestBody = requestBody;
        this.progressListener = progressListener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    /**
     * Writes the content of this request to {@code out}.
     *
     * @param sink
     */
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (sink instanceof Buffer) {
            // Log Interceptor
            requestBody.writeTo(sink);
            return;
        }
//        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(sink(sink));
//        }
        //写入
        requestBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
    }

    /**
     * 写入，回调进度接口
     * @param sink Sink
     * @return Sink
     */
    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long bytesWritten = 0;
            //总字节长度，避免多次调用contentLength()方法
            long contentLength = 0;
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = requestBody.contentLength();
                }
                //增加当前写入的字节数
                bytesWritten += byteCount;
                if (progressListener != null){
                    progressListener.onProgress(bytesWritten,contentLength,bytesWritten == contentLength);
                }
            }
        };
    }

}
