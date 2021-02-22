package com.jjc.qiqiharuniversity.http;

import com.jjc.qiqiharuniversity.common.LogHelper;

import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Author jiajingchao
 * Created on 2021/2/20
 * Description:
 */
public class BaseRequest {

    private static final String TAG = BaseRequest.class.getSimpleName();

    public <T> Callback<T> callback(final RequestListener<T> listener) {
        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                if (listener != null) {
                    if (response.body() == null) {
                        BaseHttpException baseHttpException = new BaseHttpException(response.toString());
                        onFailure(call, baseHttpException);
                    } else {
                        listener.onResponse(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    call.clone().enqueue(this);
                    return;
                }

                if (listener != null) {
                    listener.onFailure(t);
                    bizIntercept(t);
                    LogHelper.i(TAG, "onFailure msg:" + t.getMessage());
                }
            }
        };
    }

    public void bizIntercept(Throwable t) {

    }

}
