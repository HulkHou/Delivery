package com.hulk.delivery.retrofit.api;

import com.hulk.delivery.entity.ResponseDataAddressList;
import com.hulk.delivery.entity.ResponseResult;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by hulk-out on 2017/11/27.
 */

public interface UserApi {

    //登录
    @FormUrlEncoded
    @POST("sys/user/login")
    Observable<ResponseResult> doLogin(@Field("phone") String phone,
                                       @Field("password") String password);

    //获取地址列表
    @GET("t/address")
    Observable<ResponseResult<ResponseDataAddressList>> getAddressList(@Header("Authorization") String authorization);

    //新增地址
    @POST("t/address")
    Observable<ResponseResult> doAddAddress(@Header("Authorization") String authorization, @Body RequestBody body);

}
