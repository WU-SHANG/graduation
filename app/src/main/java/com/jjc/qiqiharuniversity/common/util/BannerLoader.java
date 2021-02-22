package com.jjc.qiqiharuniversity.common.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jjc.qiqiharuniversity.common.ObjectHelper;
import com.youth.banner.loader.ImageLoader;

/**
 * Author jiajingchao
 * Created on 2021/1/11
 * Description:轮播图的图片加载器
 */
public class BannerLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        if (context == null || ObjectHelper.isIllegal(path) || ObjectHelper.isIllegal(imageView)) {
            return;
        }
        Glide.with(context)
                .load(path)
                .into(imageView);
    }
}
