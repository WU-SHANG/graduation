package com.jjc.qiqiharuniversity.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * Author jiajingchao
 * Created on 2021/1/11
 * Description:图片加载
 */
public class BannerLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide
        Glide.with(context)
                .load(path)
                .into(imageView);
    }
}
