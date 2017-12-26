package com.wry.http;

import com.wry.mvp.model.MessageNumModel;
import com.wry.mvp.model.Network_GetMessageNum;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/10/20 0020.
 */

public interface RetrofitService {
    @POST("msg")
    Call<MessageNumModel> getUnreadMessageNum(@Header("pSign") String lang, @Body Network_GetMessageNum network_getMessageNum);
}
