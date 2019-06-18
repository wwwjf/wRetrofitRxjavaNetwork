package com.wengjianfeng.wretrofitrxjava.http;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHandler {
    private static Retrofit mRetrofit;
    private static OkHttpClient mOkHttpClient;
    private static RetrofitHandler mRetrofitHandler;
    private static ObservableAPI mObservableAPI;

    private RetrofitHandler() {
        initRetrofit();
    }

    public static synchronized RetrofitHandler getInstance() {
        if (mRetrofitHandler == null) {
            synchronized (RetrofitHandler.class) {
                if (mRetrofitHandler == null) {
                    mRetrofitHandler = new RetrofitHandler();
                }
            }
        }
        return mRetrofitHandler;
    }

    /**
     * 获取 Retrofit
     */
    private void initRetrofit() {
        initOkHttpClient();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(URL.BASE_URL)
                //JSON转换器,使用Gson来转换
                .addConverterFactory(GsonConverterFactory.create())
                //RxJava适配器
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mOkHttpClient)
                .build();
        mObservableAPI = mRetrofit.create(ObservableAPI.class);
    }

    /**
     * 单例模式获取 OkHttpClient
     */
    private static void initOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (RetrofitHandler.class) {
                if (mOkHttpClient == null) {
                    // 指定缓存路径,缓存大小100Mb
                    Cache cache = new Cache(new File(HttpConfig.DIR_CACHE_FILE, "HttpCache"),
                            1024 * 1024 * 100);
                    mOkHttpClient = new OkHttpClient.Builder()
                            //设置连接超时时间
                            .connectTimeout(HttpConfig.HTTP_TIME_OUT_TIME, TimeUnit.SECONDS)
                            //设置读取超时时间
                            .readTimeout(HttpConfig.HTTP_TIME_OUT_TIME, TimeUnit.SECONDS)
                            //设置写入超时时间
                            .writeTimeout(HttpConfig.HTTP_TIME_OUT_TIME, TimeUnit.SECONDS)
                            //默认重试一次
                            .retryOnConnectionFailure(true)
                            //添加请求头拦截器
                            .addInterceptor(InterceptorHelper.getHeaderInterceptor())
                            //添加日志拦截器
                            .addInterceptor(InterceptorHelper.getLogInterceptor())
                            //添加缓存拦截器
                            .addInterceptor(InterceptorHelper.getCacheInterceptor())
                            //添加重试拦截器
                            .addInterceptor(InterceptorHelper.getRetryInterceptor())
                            // 信任Https,忽略Https证书验证
                            // https认证,如果要使用https且为自定义证书 可以去掉这两行注释，并自行配制证书。
//                            .sslSocketFactory(SSLSocketTrust.getSSLSocketFactory())
//                            .hostnameVerifier(SSLSocketTrust.getHostnameVerifier())
                            //缓存
                            .cache(cache)
                            .build();
                }
            }
        }
    }

    /**
     * 对外提供调用 API的接口
     *
     * @return
     */
    public ObservableAPI getAPIService() {
        return mObservableAPI;
    }
}