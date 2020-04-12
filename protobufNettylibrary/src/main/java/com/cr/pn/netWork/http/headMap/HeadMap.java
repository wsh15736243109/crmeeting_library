package com.cr.pn.netWork.http.headMap;

import okhttp3.Request;

/**
 * Created by zy on 2018/7/31.
 */

public interface HeadMap {

    public Request.Builder requestHead(Request.Builder builder);
}
