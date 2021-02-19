package com.jjc.qiqiharuniversity.manager;


import com.jjc.qiqiharuniversity.bean.ListNewsVO;
import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Author jiajingchao
 * Created on 2021/1/5
 * Description:
 */

public interface HttpApi {
    /**
     * 头条的新闻接口
     * @param type
     * @param key
     * @return
     */
    @POST("toutiao/index")
    Observable<ListNewsVO> newsList(@Query("type") String type, @Query("key") String key);
}
