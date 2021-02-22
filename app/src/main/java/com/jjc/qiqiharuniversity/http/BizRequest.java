package com.jjc.qiqiharuniversity.http;



import com.jjc.qiqiharuniversity.BizApplication;
import com.jjc.qiqiharuniversity.R;
import com.jjc.qiqiharuniversity.biz.login.LoginModel;
import com.jjc.qiqiharuniversity.common.AppManager;
import com.jjc.qiqiharuniversity.common.EventBusEvents;
import com.jjc.qiqiharuniversity.common.EventBusManager;
import com.jjc.qiqiharuniversity.common.ToastManager;

import java.util.Map;


/**
 * Author jiajingchao
 * Created on 2021/2/20
 * Description:
 */
public class BizRequest extends BaseRequest {

    private static final String TAG = BizRequest.class.getSimpleName();
    private static volatile BizRequest mInstance;
    private BizHttpService mBizHttpService;
    private BasicParamsInterceptor mParamsInterceptor;

    public static BizRequest getInstance() {
        if (mInstance == null) {
            synchronized (BizRequest.class) {
                if (mInstance == null) {
                    mInstance = new BizRequest();
                }
            }
        }
        return mInstance;
    }

    public BizRequest() {
        mParamsInterceptor = new BasicParamsInterceptor.Builder()
                .signPrivateParams(BizCommonFieldMap.buildCommonFieldMap())
//                .addQueryParam(BizHttpConstants.TOKEN, LoginController.getToken())
                .build();

        String baseUrl = BizHttpConstants.BASE_URL;

        mBizHttpService = RequestManager.getInstance()
                .addInterceptors(mParamsInterceptor)
                .createService(baseUrl, BizHttpService.class);
    }

    public void refresh() {
//        mParamsInterceptor.updateQueryParam(BizHttpConstants.TOKEN, LoginController.getToken());
    }

    @Override
    public void bizIntercept(BaseModel body) {
        super.bizIntercept(body);
        if (body.isUserIllegal()) {
            EventBusManager.post(new EventBusEvents.LogoutEvent());
        }

        if (body.isIllegal()) {
            ToastManager.show(BizApplication.getInstance(), body.getHint());
        }
    }

    @Override
    public void bizIntercept(Throwable t) {
        super.bizIntercept(t);
        if (AppManager.isDebug()) {
            ToastManager.showLengthLong(BizApplication.getInstance(), t.getMessage());
        } else {
            ToastManager.show(BizApplication.getInstance(), BizApplication.getInstance().getResources().getString(R.string.network_fail_toast_text));
        }
    }

    public void login(Map<String, String> params, RequestListener<BaseModel<LoginModel>> listener) {
        mBizHttpService.login(params).enqueue(callback(listener));
    }

    public void sendCode(Map<String, String> params, RequestListener<BaseModel> listener) {
        mBizHttpService.sendCode(params).enqueue(callback(listener));
    }

    public void verifyCode(Map<String, String> params, RequestListener<BaseModel> listener) {
        mBizHttpService.verifyCode(params).enqueue(callback(listener));
    }

}