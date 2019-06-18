package com.wengjianfeng.wretrofitrxjava.http;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author wy
 * @description 拦截器工具类
 */

public class InterceptorHelper {
    public static String TAG = "Interceptor";

    /**
     * 日志拦截器
     */
    public static HttpLoggingInterceptor getLogInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.w(TAG, "LogInterceptor---------: " + message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);//设置打印数据的级别
    }

    /**
     * 缓存拦截器
     *
     * @return
     */
    public static Interceptor getCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                //CONTEXT不能为空
                if (true) {
                    int maxStale = 4 * 7 * 24 * 60; // 离线时缓存保存4周,单位:秒
                    CacheControl tempCacheControl = new CacheControl.Builder()
                            .onlyIfCached()
                            .maxStale(maxStale, TimeUnit.SECONDS)
                            .build();
                    request = request.newBuilder()
                            .cacheControl(tempCacheControl)
                            .build();
                }
                return chain.proceed(request);
            }
        };
    }


    /**
     * 重试拦截器
     *
     * @return
     */
    public static Interceptor getRetryInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                int maxRetry = 10;//最大重试次数
                int retryNum = 5;//假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）

                Request request = chain.request();
                Response response = chain.proceed(request);
                while (!response.isSuccessful() && retryNum < maxRetry) {
                    retryNum++;
                    response = chain.proceed(request);
                }
                return response;
            }
        };
    }

    /**
     * 请求头拦截器
     *
     * @return
     */
    public static Interceptor getHeaderInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                //在这里你可以做一些想做的事,比如token失效时,重新获取token
                //或者添加header等等


                Request originalRequest = chain.request();

                if (null == originalRequest.body()) {
                    return chain.proceed(originalRequest);
                }

                Request compressedRequest = originalRequest.newBuilder()
                        .header("Content-Encoding", "gzip")
                        .header("User-Agent", "OkHttp Headers.java")
                        .addHeader("Accept", "application/json; q=0.5")
                        .addHeader("Accept", "application/vnd.github.v3+json")
                        .addHeader("Accept-Encoding", "identity")
//                    .addHeader(Constants.WEB_TOKEN, webi_token)
                        .build();
                Response proceed = chain.proceed(compressedRequest);
                return proceed;
            }
        };

    }
}