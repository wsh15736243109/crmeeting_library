package com.cr.pn.netWork.http.ssl;

import com.cr.pn.netWork.http.okHttp.OkHttpHelper;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * Created by zy on 2018/8/13.
 */
public enum  Ssl {

    INSTANCE;

    private Ssl(){}

    /**
     * https网址跳过签名.
     * 访问https(如12306)异常，需在调用http
     * 调用此方法.
     */
    public void initSSL() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            X509TrustManager trustManager = new X509TrustManager() {

                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                    // TODO Auto-generated method stub

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                    // TODO Auto-generated method stub

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    // TODO Auto-generated method stub
                    return new X509Certificate[0];
                }
            };
            sslContext.init(null, new TrustManager[] {trustManager}, new SecureRandom());
            OkHttpClient.Builder builder = OkHttpHelper.getInstance().getClient().newBuilder();
            builder.sslSocketFactory(sslContext.getSocketFactory(),trustManager);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            OkHttpHelper.getInstance().initHttp(builder.build());
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
