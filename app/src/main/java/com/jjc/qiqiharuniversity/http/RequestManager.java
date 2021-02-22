package com.jjc.qiqiharuniversity.http;

import androidx.annotation.NonNull;

import com.jjc.qiqiharuniversity.common.ObjectHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author jiajingchao
 * Created on 2021/2/20
 * Description:
 */
public class RequestManager {

    private static volatile RequestManager mInstance;
    private OkHttpClient.Builder mBuilder;
    private List<Interceptor> mInterceptors;

    public static RequestManager getInstance() {
        if (mInstance == null) {
            synchronized (RequestManager.class) {
                if (mInstance == null) {
                    mInstance = new RequestManager();
                }
            }
        }

        return mInstance;
    }

    public RequestManager() {
        mInterceptors = new ArrayList<>();
        mBuilder = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS);
    }

    public <T> T createService(@NonNull String baseUrl, @NonNull Class<T> service) {
        if (!ObjectHelper.isIllegal(mInterceptors)) {
            for (Interceptor interceptor : mInterceptors) {
                mBuilder.addInterceptor(interceptor);
            }
        }

        Retrofit retrofit = new Retrofit.Builder()
                .client(mBuilder.build())
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(service);
    }

    public RequestManager addInterceptors(@NonNull Interceptor interceptor) {
        mInterceptors.add(interceptor);
        return this;
    }

}
