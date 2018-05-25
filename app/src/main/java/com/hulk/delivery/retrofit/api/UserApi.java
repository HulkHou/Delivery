package com.hulk.delivery.retrofit.api;

import com.hulk.delivery.entity.ResponseDataObjectList;
import com.hulk.delivery.entity.ResponseResult;
import com.hulk.delivery.entity.TAddress;
import com.hulk.delivery.entity.TCollect;
import com.hulk.delivery.entity.TFood;
import com.hulk.delivery.entity.TMessage;
import com.hulk.delivery.entity.TOrder;
import com.hulk.delivery.entity.TShop;
import com.hulk.delivery.entity.User;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by hulk-out on 2017/11/27.
 */

public interface UserApi {

    //注册
    @POST("sys/user")
    Observable<ResponseResult> doAdd(@Body RequestBody body);

    //验证码登录
    @FormUrlEncoded
    @POST("sys/user/loginByCode")
    Observable<ResponseResult> doLoginByCode(@Field("phone") String phone);

    //密码登录
    @FormUrlEncoded
    @POST("sys/user/login")
    Observable<ResponseResult> doLogin(@Field("phone") String phone,
                                       @Field("password") String password);

    //修改密码
    @FormUrlEncoded
    @POST("sys/user/changePwd")
    Observable<ResponseResult> doChangePassword(@Header("Authorization") String authorization,
                                                @Field("oldPwd") String oldPwd,
                                                @Field("newPwd") String newPwd,
                                                @Field("rePwd") String rePwd);

    //重置密码
    @FormUrlEncoded
    @POST("sys/user/resetPwd")
    Observable<ResponseResult> doResetPassword(@Header("Authorization") String authorization,
                                               @Field("newPwd") String newPwd,
                                               @Field("rePwd") String rePwd);

    //查询User
    @GET("sys/user/{phone}")
    Observable<ResponseResult<User>> getUser(@Path("phone") String phone);

    //获取userId
    @GET("sys/user/userId")
    Observable<ResponseResult> getUserId(@Header("Authorization") String authorization);

    //获取地址列表
    @GET("t/address")
    Observable<ResponseResult<ResponseDataObjectList<TAddress>>> getAddressList(@Header("Authorization") String authorization);

    //新增地址
    @POST("t/address")
    Observable<ResponseResult> doAddAddress(@Header("Authorization") String authorization, @Body RequestBody body);

    //更新地址
    @PUT("t/address")
    Observable<ResponseResult> doUpdateAddress(@Header("Authorization") String authorization, @Body RequestBody body);

    //删除地址
    @DELETE("t/address/{id}")
    Observable<ResponseResult> deleteAddress(@Header("Authorization") String authorization,
                                             @Path("id") Integer id);

    //设置默认地址
    @PUT("t/address/{id}")
    Observable<ResponseResult> setDefaultAddress(@Header("Authorization") String authorization,
                                                 @Path("id") Integer id);

    //获取订单列表
    @GET("t/order")
    Observable<ResponseResult<ResponseDataObjectList<TOrder>>> getOrderList(@Header("Authorization") String authorization);

    //获取消息列表
    @GET("t/message")
    Observable<ResponseResult<ResponseDataObjectList<TMessage>>> getMessageList(@Header("Authorization") String authorization);

    //获取商家列表
    @GET("t/shop")
    Observable<ResponseResult<ResponseDataObjectList<TShop>>> getShopList();

    //获取collect商家列表
    @GET("t/collect")
    Observable<ResponseResult<ResponseDataObjectList<TCollect>>> getCollectShopList(@Header("Authorization") String authorization);

    //获取Food列表
    @GET("t/food")
    Observable<ResponseResult<ResponseDataObjectList<TFood>>> getFoodList();

}
