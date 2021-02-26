package com.jjc.qiqiharuniversity.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;

/**
 * Author jiajingchao
 * Created on 2021/2/20
 * Description: 图片加载管理类，封装Glide
 */
public class ImageManager {

    public static void load(@NonNull Fragment fragment,
                            @NonNull String url,
                            @NonNull ImageView imageView) {
        if (fragment == null) {
            return;
        }

        if (fragment.isDetached()) {
            return;
        }

        if (fragment.getActivity() == null) {
            return;
        }

        if (TextUtils.isEmpty(url)) {
            return;
        }

        Glide.with(fragment.getActivity()).load(url).into(imageView);
    }

    public interface DownloadOnlyListener {
        void onResourceReady(@NonNull File resource);
    }

    public static void downloadOnly(Activity activity,
                                    @NonNull String url,
                                    final DownloadOnlyListener listener) {
        if (activity.isDestroyed()) {
            return;
        }
        if (TextUtils.isEmpty(url)) {
            return;
        }


        Glide.with(activity).downloadOnly().load(url).into(new CustomTarget<File>() {
            @Override
            public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                if (listener != null) {
                    listener.onResourceReady(resource);
                }
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }

    public static void load(@NonNull Fragment fragment,
                            @NonNull String url,
                            @NonNull ImageView imageView,
                            int errorRes) {
        if (fragment == null) {
            return;
        }

        if (fragment.isDetached()) {
            return;
        }

        if (fragment.getActivity() == null) {
            return;
        }

        Glide.with(fragment.getActivity()).load(url).error(errorRes).placeholder(errorRes).fallback(errorRes).into(imageView);
    }

    public static void load(@NonNull Activity activity,
                            @NonNull String url,
                            @NonNull ImageView imageView,
                            int errorRes) {
        if (activity.isDestroyed()) {
            return;
        }

        RequestOptions options = new RequestOptions().error(errorRes);
        if (!TextUtils.isEmpty(url)) {
            Glide.with(activity).load(url).apply(options).into(imageView);
        }
    }

    public static void loadPlaceHolder(@NonNull Activity activity,
                                       @NonNull String url,
                                       @NonNull ImageView imageView,
                                       int errorRes) {
        if (activity.isDestroyed()) {
            return;
        }
        Glide.with(activity).load(url).error(errorRes).placeholder(errorRes).fallback(errorRes).into(imageView);
    }

    public static void loadPlaceHolder(@NonNull Activity activity,
                                       @NonNull String url,
                                       @NonNull ImageView imageView,
                                       int errorRes,
                                       int placeHolder) {
        if (activity.isDestroyed()) {
            return;
        }
        Glide.with(activity).load(url).error(errorRes).placeholder(placeHolder).fallback(errorRes).into(imageView);
    }

    public static void load(@NonNull Activity activity, @NonNull String url, @NonNull ImageView imageView) {
        if (activity.isDestroyed()) {
            return;
        }
        if (!TextUtils.isEmpty(url)) {
            Glide.with(activity).load(url).into(imageView);
        }
    }

    public static void load(@NonNull Activity activity, @NonNull String url, @NonNull ImageView imageView, final onLoadListener onLoadListener) {
        if (activity.isDestroyed()) {
            return;
        }
        if (!TextUtils.isEmpty(url)) {
            Glide.with(activity).load(url).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    onLoadListener.onReady();
                    return false;
                }
            }).into(imageView);
        }
    }

    public static void loadCircle(@NonNull Activity activity, @NonNull String url, @NonNull ImageView imageView) {
        if (activity.isDestroyed()) {
            return;
        }
        if (!TextUtils.isEmpty(url)) {
            Glide.with(activity).load(url).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(imageView);
        }
    }

    public static void loadCircle(@NonNull Activity activity, @NonNull String url, @NonNull ImageView imageView, int resId) {
        if (activity.isDestroyed()) {
            return;
        }
        Glide.with(activity).load(url).apply(RequestOptions.bitmapTransform(new CircleCrop())).placeholder(resId).thumbnail(Glide.with(activity).load(resId).apply(RequestOptions.bitmapTransform(new CircleCrop()))).into(imageView);
    }

    public static void loadCircle(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        if (context == null) {
            return;
        }

        if (TextUtils.isEmpty(url)) {
            return;
        }

        if (context instanceof Activity && ((Activity) context).isDestroyed()) {
            return;
        }
        Glide.with(context).load(url).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(imageView);
    }

    public static void loadCircle(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView, int resId) {
        if (context == null) {
            return;
        }

        if (TextUtils.isEmpty(url)) {
            return;
        }

        if (context instanceof Activity && ((Activity) context).isDestroyed()) {
            return;
        }
        Glide.with(context).load(url).apply(RequestOptions.bitmapTransform(new CircleCrop())).placeholder(resId).thumbnail(Glide.with(context).load(resId).apply(RequestOptions.bitmapTransform(new CircleCrop()))).into(imageView);
    }

    public static void loadCircleByFile(@NonNull Activity activity, @NonNull File file, @NonNull ImageView imageView) {
        if (activity.isDestroyed()) {
            return;
        }

        RequestOptions options = new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .circleCrop();

        Glide.with(activity).load(file).apply(options).into(imageView);
    }

    public static void loadFitCenter(@NonNull Fragment fragment,
                                     @NonNull String url,
                                     @NonNull ImageView imageView) {
        if (fragment.isDetached()) {
            return;
        }

        if (TextUtils.isEmpty(url)) {
            return;
        }

        if (fragment.getActivity() == null) {
            return;
        }

        Glide.with(fragment.getActivity()).load(url).fitCenter().into(imageView);
    }

    public static void loadFile(@NonNull Activity activity, @NonNull File file, @NonNull ImageView imageView) {
        if (activity.isDestroyed()) {
            return;
        }

        RequestOptions options = new RequestOptions()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE);

        Glide.with(activity).load(file).apply(options).into(imageView);
    }

    public interface onLoadListener {
        void onReady();
    }

    public static void load(Context context,
                            String url,
                            ImageView imageView) {
        if (context == null) {
            return;
        }

        if (TextUtils.isEmpty(url)) {
            return;
        }

        if (context instanceof Activity && ((Activity) context).isDestroyed()) {
            return;
        }

        Glide.with(context).load(url).into(imageView);
    }

    public static void loadPlaceHolder(Context context,
                                       String url,
                                       ImageView imageView,
                                       int placeHolder) {
        if (context == null) {
            return;
        }
        if (context instanceof Activity && ((Activity) context).isDestroyed()) {
            return;
        }
        Glide.with(context).load(url).error(placeHolder).placeholder(placeHolder).fallback(placeHolder).into(imageView);
    }
}
