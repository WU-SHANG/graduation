package com.jjc.qiqiharuniversity.http;

import com.jjc.qiqiharuniversity.biz.home.news.ListNewsVO;
import com.jjc.qiqiharuniversity.biz.login.LoginModel;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Author jiajingchao
 * Created on 2021/2/20
 * Description: http请求接口
 */
public interface BizHttpService {

    @FormUrlEncoded
    @POST("user/login")
    Call<BaseModel<LoginModel>> login(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("user/phone/sendCode")
    Call<BaseModel> sendCode(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("user/phone/verifyCode")
    Call<BaseModel> verifyCode(@FieldMap Map<String, String> params);

    /**
     * 头条的新闻接口
     * @param type
     * @param key
     * @return
     */
    @POST("toutiao/index")
    Observable<ListNewsVO> newsList(@Query("type") String type, @Query("key") String key);

    @POST("toutiao/index")
    Call<ListNewsVO> getNewsList(@Query("type") String type, @Query("key") String key);

}
