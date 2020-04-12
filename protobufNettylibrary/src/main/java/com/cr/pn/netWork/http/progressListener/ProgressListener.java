package com.cr.pn.netWork.http.progressListener;

/**
 * Created by zy on 2018/7/6.
 */

public interface ProgressListener {

    public void onProgress(long bytesRead, long contentLength, boolean done);

    public void startHttp(long contentLength);
}
