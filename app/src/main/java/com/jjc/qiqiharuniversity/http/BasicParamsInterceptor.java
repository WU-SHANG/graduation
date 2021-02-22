package com.jjc.qiqiharuniversity.http;

import android.text.TextUtils;

import com.jjc.qiqiharuniversity.common.LogHelper;
import com.jjc.qiqiharuniversity.common.util.MD5Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Author jiajingchao
 * Created on 2021/2/20
 * Description: 参数插值器
 */
public class BasicParamsInterceptor implements Interceptor {
    private static final String TAG = BasicParamsInterceptor.class.getSimpleName();

    public static final String ACCESS_KEY = "625e08eb7d304f55";
    public static final String SECRET_KEY = "83550994C4D14C05851A1D1ACE5EE472";
    private static final String HTTP_CONSTANTS_SIGN = "sign";
    private static final String HTTP_CONSTANTS_ACCESS_KEY = "accessKey";
    private static final String HTTP_CONSTANTS_NONCE = "nonce";

    Map<String, String> queryParamsMap = new HashMap<>();
    Map<String, String> paramsMap = new HashMap<>();
    Map<String, String> headerParamsMap = new HashMap<>();
    List<String> headerLinesList = new ArrayList<>();
    boolean canSignPrivateParams = false;
    Map<String, String> mCommonParamsMap;

    private BasicParamsInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();

        // process header params inject
        Headers.Builder headerBuilder = request.headers().newBuilder();
        if (headerParamsMap.size() > 0) {
            Iterator iterator = headerParamsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                headerBuilder.add((String) entry.getKey(), (String) entry.getValue());
            }
        }

        if (headerLinesList.size() > 0) {
            for (String line : headerLinesList) {
                headerBuilder.add(line);
            }
            requestBuilder.headers(headerBuilder.build());
        }
        // process header params end


        // process queryParams inject whatever it's GET or POST
        if (queryParamsMap.size() > 0) {
            request = injectParamsIntoUrl(request.url().newBuilder(), requestBuilder, queryParamsMap);
        }

        // process post body inject
        if (paramsMap != null && paramsMap.size() > 0 && request.method().equals("POST")) {
            if (request.body() instanceof FormBody) {
                FormBody.Builder newFormBodyBuilder = new FormBody.Builder();
                if (paramsMap.size() > 0) {
                    Iterator iterator = paramsMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        newFormBodyBuilder.add((String) entry.getKey(), (String) entry.getValue());
                    }
                }

                FormBody oldFormBody = (FormBody) request.body();
                int paramSize = oldFormBody.size();
                if (paramSize > 0) {
                    for (int i = 0; i < paramSize; i++) {
                        newFormBodyBuilder.add(oldFormBody.name(i), oldFormBody.value(i));
                    }
                }

                requestBuilder.post(newFormBodyBuilder.build());
                request = requestBuilder.build();
            } else if (request.body() instanceof MultipartBody) {
                MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

                Iterator iterator = paramsMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry entry = (Map.Entry) iterator.next();
                    multipartBuilder.addFormDataPart((String) entry.getKey(), (String) entry.getValue());
                }

                List<MultipartBody.Part> oldParts = ((MultipartBody) request.body()).parts();
                if (oldParts != null && oldParts.size() > 0) {
                    for (MultipartBody.Part part : oldParts) {
                        multipartBuilder.addPart(part);
                    }
                }

                requestBuilder.post(multipartBuilder.build());
                request = requestBuilder.build();
            }

        }
        if (request != null && "POST".equals(request.method()) && canSignPrivateParams && request.body() instanceof FormBody) {
            FormBody.Builder newFormBodyBuilder = new FormBody.Builder();
            newFormBodyBuilder.add(HTTP_CONSTANTS_ACCESS_KEY, ACCESS_KEY);
            String uuid = UUID.randomUUID().toString().replace("-", "");
            newFormBodyBuilder.add(HTTP_CONSTANTS_NONCE, uuid);
            FormBody oldFormBody = (FormBody) request.body();
            if (oldFormBody != null && oldFormBody.size() > 0) {
                LogHelper.i(TAG, "addSignToRequest url : " + request.url());
                TreeMap<String, String> privateParams = new TreeMap<>();
                privateParams.put(HTTP_CONSTANTS_ACCESS_KEY, ACCESS_KEY);
                privateParams.put(HTTP_CONSTANTS_NONCE, uuid);
                for (int i = 0; i < oldFormBody.size(); i++) {
                    newFormBodyBuilder.add(oldFormBody.name(i), oldFormBody.value(i));
                    filterPrivateParams(privateParams, oldFormBody.name(i), oldFormBody.value(i));
                    LogHelper.i(TAG, "oldFormBody key : " + oldFormBody.name(i) + " oldFormBody.value(i) ：" + oldFormBody.value(i));
                }
                String signValue = signPrivateParams(privateParams);
                if (!TextUtils.isEmpty(signValue)) {
                    newFormBodyBuilder.add(HTTP_CONSTANTS_SIGN, signValue);
                }
            }
            requestBuilder.post(newFormBodyBuilder.build());
            request = requestBuilder.build();
        }

        return chain.proceed(request);
    }

    private boolean canInjectIntoBody(Request request) {
        if (request == null) {
            return false;
        }
        if (!TextUtils.equals(request.method(), "POST")) {
            return false;
        }
        RequestBody body = request.body();
        if (body == null) {
            return false;
        }
        MediaType mediaType = body.contentType();
        if (mediaType == null) {
            return false;
        }
        if (!TextUtils.equals(mediaType.subtype(), "x-www-form-urlencoded")) {
            return false;
        }
        return true;
    }

    // func to inject params into url
    private Request injectParamsIntoUrl(HttpUrl.Builder httpUrlBuilder, Request.Builder requestBuilder, Map<String, String> paramsMap) {
        if (paramsMap.size() > 0) {
            Iterator iterator = paramsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                httpUrlBuilder.addQueryParameter((String) entry.getKey(), (String) entry.getValue());
            }
            requestBuilder.url(httpUrlBuilder.build());
            return requestBuilder.build();
        }

        return null;
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    public static class Builder {

        BasicParamsInterceptor interceptor;

        public Builder() {
            interceptor = new BasicParamsInterceptor();
        }

        public Builder addParam(String key, String value) {
            interceptor.paramsMap.put(key, value);
            return this;
        }

        public Builder addParamsMap(Map<String, String> paramsMap) {
            interceptor.paramsMap.putAll(paramsMap);
            return this;
        }

        public Builder addHeaderParam(String key, String value) {
            interceptor.headerParamsMap.put(key, value);
            return this;
        }

        public Builder addHeaderParamsMap(Map<String, String> headerParamsMap) {
            interceptor.headerParamsMap.putAll(headerParamsMap);
            return this;
        }

        public Builder addHeaderLine(String headerLine) {
            int index = headerLine.indexOf(":");
            if (index == -1) {
                throw new IllegalArgumentException("Unexpected header: " + headerLine);
            }
            interceptor.headerLinesList.add(headerLine);
            return this;
        }

        public Builder addHeaderLinesList(List<String> headerLinesList) {
            for (String headerLine : headerLinesList) {
                int index = headerLine.indexOf(":");
                if (index == -1) {
                    throw new IllegalArgumentException("Unexpected header: " + headerLine);
                }
                interceptor.headerLinesList.add(headerLine);
            }
            return this;
        }

        public Builder addQueryParam(String key, String value) {
            interceptor.queryParamsMap.put(key, value);
            return this;
        }

        public Builder addQueryParamsMap(Map<String, String> queryParamsMap) {
            interceptor.queryParamsMap.putAll(queryParamsMap);
            return this;
        }

        public Builder signPrivateParams(Map<String, String> commonParams) {
            if (interceptor.mCommonParamsMap != null) {
                interceptor.mCommonParamsMap.clear();
                interceptor.mCommonParamsMap = null;
            }
            if (commonParams != null && !commonParams.isEmpty()) {
                interceptor.mCommonParamsMap = commonParams;
                interceptor.canSignPrivateParams = true;
            }
            return this;
        }

        public BasicParamsInterceptor build() {
            return interceptor;
        }

    }

    public BasicParamsInterceptor updateQueryParam(String key, String value) {
        queryParamsMap.put(key, value);
        return this;
    }

    private void filterPrivateParams(TreeMap<String, String> params, String key, String value) {
        if (!canSignPrivateParams || mCommonParamsMap == null || mCommonParamsMap.isEmpty() || params == null) {
            return;
        }
        if (!mCommonParamsMap.containsKey(key)) {//过滤公共参数
            params.put(key, value);
        }
    }

    private String signPrivateParams(TreeMap<String, String> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        Iterator iterator = params.entrySet().iterator();
        int index = 0;
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            sb.append(String.format((index == 0 ? "" : "&") + "%s=%s", key, value));
            index++;
        }
        sb.append(String.format("&secretKey=%s", SECRET_KEY));

        String firstSign = sb.toString();
        LogHelper.i(TAG, "firstSign : " + firstSign);
        return MD5Utils.getMd5U32Value(firstSign);
    }

}
