package com.hulk.delivery.retrofit.api;

import com.hulk.delivery.entity.ResponseDataAddressList;
import com.hulk.delivery.entity.ResponseResult;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by hulk-out on 2017/11/27.
 */

public interface UserApi {

    //获取地址列表
    @GET("t/address")
    Observable<ResponseResult<ResponseDataAddressList>> getAddressList();

    //新增地址
    @POST("t/address")
    Observable<ResponseResult> doAddAddress(@Body RequestBody body);

}
