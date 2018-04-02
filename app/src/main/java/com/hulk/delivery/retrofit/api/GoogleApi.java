package com.hulk.delivery.retrofit.api;

import com.hulk.delivery.entity.GoogleAddressResponseResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hulk-out on 2017/11/27.
 */

public interface GoogleApi {

    //获取地址列表
    @GET("maps/api/geocode/json")
    Observable<GoogleAddressResponseResult> getAddressByGoogle(@Query("latlng") String latlng,
                                                               @Query("key") String key);

}
