package com.cr.pn.netWork.http.body;

import com.cr.pn.netWork.http.progressListener.ProgressListener;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by zy on 2018/7/9.
 */

public class ProgressResponseBody extends ResponseBody {

    private ResponseBody responseBody;

    private BufferedSource bufferedSource;

    private ProgressListener progressListener;

    public ProgressResponseBody(ResponseBody responseBody,ProgressListener progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {

        return new ForwardingSource(source) {

            long totalBytesRead = 0L;

            //总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                long last = totalBytesRead;
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                if (contentLength == 0){
                    contentLength = contentLength();
                }
                if (last == 0){
                    if (progressListener != null){
                        progressListener.startHttp(responseBody.contentLength());
                    }
                }
                if (progressListener != null&&totalBytesRead>last){
                    progressListener.onProgress(totalBytesRead,contentLength,byteCount == -1);
                }
                return bytesRead;
            }
        };

    }
}
