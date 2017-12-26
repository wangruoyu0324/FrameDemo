package com.wry.http.Api;

import com.wry.http.retrofit.HttpResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * @author ZhongDaFeng
 */
public interface UserApi {

    @GET("user/login")
    Observable<HttpResponse> login(@QueryMap Map<String, Object> request);

}
