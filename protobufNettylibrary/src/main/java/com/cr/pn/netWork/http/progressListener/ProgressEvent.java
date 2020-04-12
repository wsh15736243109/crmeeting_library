package com.cr.pn.netWork.http.progressListener;

import com.cr.pn.beanBase.BeanBase;

/**
 * Created by zy on 2018/7/10.
 */

public class ProgressEvent extends BeanBase{

    private long bytesRead;

    private long contentLength;

    private boolean done;

    private String clazzName;

    public ProgressEvent(long bytesRead, long contentLength, boolean done,String clazzName){
        this.bytesRead = bytesRead;
        this.contentLength = contentLength;
        this.done = done;
        this.clazzName = clazzName;
    }

    public ProgressEvent(){}

    public long getBytesRead() {
        return bytesRead;
    }

    public void setBytesRead(long bytesRead) {
        this.bytesRead = bytesRead;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getClazzName() {
        return clazzName;
    }
}