package com.cr.pn.netWork.http.okhttpInterceptor;


import com.cr.pn.Utils.ViewUtils.StringUtil;
import com.cr.pn.Utils.log.MyLog;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * An OkHttp interceptor which logs request and response information. Can be applied as an
 * {@linkplain OkHttpClient#interceptors() application interceptor} or as a
 * {@linkplain OkHttpClient#networkInterceptors() network interceptor}.
 * <p>
 * The format of the logs created by this class should not be considered stable and may change
 * slightly between releases. If you need a stable logging format, use your own interceptor.
 */
public final class LogInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public enum Level {
        /**
         * No logs.
         */
        NONE,
        /**
         * Logs request and response lines and their respective headers.
         */
        HEADERS,
        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         */
        BODY
    }

    public LogInterceptor() {

    }

    public static volatile Level level = Level.NONE;

    /**
     * Change the level at which this interceptor logs.
     */
    public LogInterceptor setLevel(Level level) {
        if (level == null) throw new NullPointerException("level == null. Use Level.NONE instead.");
        this.level = level;
        return this;
    }

    public Level getLevel() {
        return level;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (level == Level.NONE) {
            return chain.proceed(request);
        }

        boolean logBody = level == Level.BODY;
        boolean logHeaders = logBody || level == Level.HEADERS;

        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        String requestStartMessage =
                "--> " + request.method() + ' ' + request.url() + ' ' + protocol(protocol);
        if (!logHeaders && hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }
        MyLog.loggerD(requestStartMessage);
        if (logHeaders) {
            if (hasRequestBody) {
                // Request body headers are only present when installed as a network interceptor. Force
                // them to be included (when available) so there values are known.
                if (requestBody.contentType() != null) {
                    MyLog.loggerD("Content-Type: " + requestBody.contentType());
                }
                if (requestBody.contentLength() != -1) {
                    MyLog.loggerD("Content-Type: " + requestBody.contentType());
                }
            }

            Headers headers = request.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                String name = headers.name(i);
                // Skip headers from the request body as they are explicitly logged above.
                if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                    MyLog.loggerD(name + ": " + headers.value(i));
                }
            }

            if (!logBody || !hasRequestBody) {
                MyLog.loggerD("--> END " + request.method());
            } else if (bodyEncoded(request.headers())) {
                MyLog.loggerD("--> END " + request.method() + " (encoded body omitted)");
            } else {
                String contentString = request.header("Content-Type");
                if (!StringUtil.isEmpty(contentString)&&!isTextBody(contentString)) {
                    MyLog.loggerD("--> END " + request.method() + " (not text body)");
                } else {
                    Buffer buffer = new Buffer();
                    requestBody.writeTo(buffer);

                    Charset charset = UTF8;
                    MediaType contentType = requestBody.contentType();
                    if (contentType != null) {
                        contentType.charset(UTF8);
                    }

                    MyLog.loggerD(buffer.readString(charset));
                    MyLog.loggerD("--> END " + request.method()
                            + " (" + requestBody.contentLength() + "-byte body)");
                }
            }
        }

        long startNs = System.nanoTime();
        Response response = chain.proceed(request);
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();

        MyLog.loggerD("<-- " + protocol(response.protocol()) + ' ' + response.code() + ' '
                + response.message() + " (" + tookMs + "ms"
                + (!logHeaders ? ", " + responseBody.contentLength() + "-byte body" : "") + ')');

        if (logHeaders) {
            Headers headers = response.headers();
            for (int i = 0, count = headers.size(); i < count; i++) {
                MyLog.loggerD(headers.name(i) + ": " + headers.value(i));
            }
            boolean hasBody=hasBody(response);
            if (!logBody || !hasBody) {
                MyLog.loggerD("<-- END HTTP");
            } else if (bodyEncoded(response.headers())) {
                MyLog.loggerD("<-- END HTTP (encoded body omitted)");
            } else {
                String contentString = response.header("Content-Type");
                if (!StringUtil.isEmpty(contentString)&&!isTextBody(contentString)) {
                    MyLog.loggerD("<-- END HTTP (not text body)");
                } else {
                    BufferedSource source = responseBody.source();
                    source.request(Long.MAX_VALUE); // Buffer the entire body.
                    Buffer buffer = source.buffer();

                    Charset charset = UTF8;
                    MediaType contentType = responseBody.contentType();
                    if (contentType != null) {
                        charset = contentType.charset(UTF8);
                    }

                    if (responseBody.contentLength() != 0) {
                        MyLog.loggerD(buffer.clone().readString(charset));
                    }else{
                        MyLog.loggerD("<-- END HTTP body没有数据");
                    }
                    MyLog.loggerD("<-- END HTTP (" + buffer.size() + "-byte body)");
                }
            }
        }

        return response;
    }
    private boolean hasBody(Response response){
        if(response.request().method().equals("HEAD")) {
            return false;
        } else {
            int responseCode = response.code();
            return (responseCode < 100 || responseCode >= 200) && responseCode != 204 && responseCode != 304?true: contentLength(response) != -1L || "chunked".equalsIgnoreCase(response.header("Transfer-Encoding"));
        }
    }
    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    private static String protocol(Protocol protocol) {
        return protocol == Protocol.HTTP_1_0 ? "HTTP/1.0" : "HTTP/1.1";
    }


    private boolean isTextBody(String contentType) {
        boolean isTextBody = false;
        MediaType mediaType = MediaType.parse(contentType);
        if (mediaType != null) {
            if ("text".equalsIgnoreCase(mediaType.type())) {
                isTextBody = true;
            } else if ("application".equalsIgnoreCase(mediaType.type())) {
                if ("json".equalsIgnoreCase(mediaType.subtype())
                        || "x-www-form-urlencoded".equalsIgnoreCase(mediaType.subtype())) {
                    isTextBody = true;
                }
            }
        }
        return isTextBody;
    }

    public long contentLength(Response response){
        String s=response.headers().get("Content-Length");
        if (s == null) return -1;
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
