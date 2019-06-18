package com.wengjianfeng.wretrofitrxjava.http;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 接口服务
 **/
public interface ObservableAPI {

    /**
     * 登录
     */
    @POST(URL.URL_LOGIN)
    Observable<BaseResponseEntity<LoginResponseEntity>> login(@Body BaseRequestEntity<LoginRequestEntity> requestEntity);

}