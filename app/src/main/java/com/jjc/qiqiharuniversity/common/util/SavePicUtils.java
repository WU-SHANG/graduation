package com.jjc.qiqiharuniversity.common.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jjc.qiqiharuniversity.common.DeviceManager;
import com.jjc.qiqiharuniversity.common.LogHelper;
import com.jjc.qiqiharuniversity.common.ToastManager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Author: sushuai
 * Date: 2020-06-05 13:26
 * Description: 图片存储本地
 * Wiki:
 * History:
 * <author> <time> <version> <desc>
 */
public class SavePicUtils {
    /**
     * 获取权限 Permission
     */
    @SuppressLint("CheckResult")
    public static void save(final Activity activity, String imagePath) {
        Glide.get(activity).clearMemory();
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        Glide.with(activity).load(imagePath).apply(requestOptions).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                saveImageToGallery(activity, drawable2Bitmap(resource));
            }
        });
    }

    /**
     * Get permission before calling this method
     *
     * @param activity
     * @param imagePath
     */
    public static void saveH5Image(final Activity activity, String imagePath, final OnImageSaveListener listener) {
        Glide.with(activity).asBitmap().load(imagePath).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                if (bitmap != null) {
                    int rtn = saveImageToGallery(activity, bitmap);
                    if (rtn == 2) {
                        if (listener != null) {
                            listener.onSuccess();
                        }
                    } else {
                        if (listener != null) {
                            listener.onFailed();
                        }
                    }
                } else {
                    if (listener != null) {
                        listener.onFailed();
                    }
                }
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                if (listener != null) {
                    listener.onFailed();
                }
            }
        });
    }

    public interface OnImageSaveListener {
        void onSuccess();

        void onFailed();
    }

    private static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    public static Bitmap base64ToBitmap(String base64Str) {
        if (TextUtils.isEmpty(base64Str)) {
            return null;
        }
        String realBase64Str = base64Str;
        if (realBase64Str.startsWith("data:image/jpeg;base64,")
                || realBase64Str.startsWith("data:image/png;base64,")
                || realBase64Str.startsWith("data:image/jpg;base64,")) {//js传过来的base64字符串如果包含格式信息，需要去掉格式信息
            realBase64Str = realBase64Str.substring(realBase64Str.indexOf(",") + 1);
        }
        byte[] decode = Base64.decode(realBase64Str, Base64.NO_WRAP);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
        return bitmap;
    }


    public static int saveImageToGallery(Context context, Bitmap bmp) {
        //文件名为时间
        long timeStamp = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sd = sdf.format(new Date(timeStamp));
        String fileName = sd + ".jpg";

        if ("OnePlus".equals(DeviceManager.getDeviceBrand())) {
            return saveDCIMImage(fileName, bmp, context);
        }

        //生成路径
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dirName = "qiqihar";
        String rootDir = context.getExternalFilesDir(null).getAbsolutePath();

        File appDir = new File(root, dirName);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }

        //获取文件
        File file = new File(root, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();

            LogHelper.i("SavePicUtils", "file.getAbsolutePath() == " + file.getAbsolutePath());
            //通知系统相册刷新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(new File(file.getPath()))));
            ToastManager.show(context, "保存成功");
            return 2;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ToastManager.show(context, "保存失败");
        return -1;
    }

    public static Bitmap layout2Bitmap(Context context, View view, int widthDp, int heightDp) {
        int widthPx = DisplayUtils.dp2px(context, widthDp);
        int heightPx = DisplayUtils.dp2px(context, heightDp);
        // 测量
        int widthSpec = View.MeasureSpec.makeMeasureSpec(widthPx, View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(heightPx, View.MeasureSpec.EXACTLY);
        view.measure(widthSpec, heightSpec);
        // 布局
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        view.layout(0, 0, measuredWidth, measuredHeight);
        // 绘制
        int width = view.getWidth();
        int height = view.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private static int saveDCIMImage(String fileName, Bitmap bitmap, Context context) {
        try {
            //设置保存参数到ContentValues中
            ContentValues contentValues = new ContentValues();
            //设置文件名
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            //兼容Android Q和以下版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                //android Q中不再使用DATA字段，而用RELATIVE_PATH代替
                //RELATIVE_PATH是相对路径不是绝对路径
                //DCIM是系统文件夹，关于系统文件夹可以到系统自带的文件管理器中查看，不可以写没存在的名字
                contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/");
                //contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, "Music/signImage");
            } else {
                contentValues.put(MediaStore.Images.Media.DATA, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath());
            }
            //设置文件类型
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/JPEG");
            //执行insert操作，向系统文件夹中添加文件
            //EXTERNAL_CONTENT_URI代表外部存储器，该值不变
            Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            if (uri != null) {
                //若生成了uri，则表示该文件添加成功
                //使用流将内容写入该uri中即可
                OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                }
            }
            return 2;
        } catch (Exception e) {
            return -1;
        }
    }


    public interface OnImageCompressListener {
        void onSuccess(Bitmap bitmap, int picType);

        void onFailed();
    }


    public static Bitmap getBitmap(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;
    }


    public static File saveBitmapFile(Bitmap bitmap) {
        //文件名为时间
        long timeStamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sd = sdf.format(new Date(timeStamp));
        String fileName = sd + ".jpeg";

        //生成路径
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dirName = "coach_mini_thumb";

        File appDir = new File(root, dirName);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }

        File file = new File(root, fileName);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 判断图片类型
     */
    public static int getPicTypeByUrl(String url) {
        int picType = -1;
        if (url == null) {
            return picType;
        }
        if (url.equals("")) {
            return picType;
        }
        String[] picArray = url.split("/");
        String picStr = "";
        if (picArray.length > 0) {
            picStr = picArray[picArray.length - 1];
        } else {
            picStr = picArray[0];
        }
        if (picStr.toLowerCase().contains(".png")) {
            picType = 0;
        } else if (picStr.toLowerCase().contains(".jpg") || picStr.toLowerCase().contains(".jpeg")) {
            picType = 1;
        }
        return picType;
    }


    /**
     * 按5:4裁切图片
     */
    private static Bitmap cropImage5To4(Bitmap srcBitmap) {
        if (srcBitmap == null) {
            return null;
        }

        int w = srcBitmap.getWidth(); // 得到图片的宽，高
        int h = srcBitmap.getHeight();

        float srcW = w > h * 1.25f ? h * 1.25f : w;
        float srcH = w > h * 1.25f ? h : w / 1.25f;

        float retX = w > h * 1.25f ? (w - h * 1.25f) / 2 : 0;//基于原图，取正方形左上角x坐标
        float retY = w > h * 1.25f ? 0 : (h - w / 1.25f) / 2f;

        //以下这句是关键
        return Bitmap.createBitmap(srcBitmap, Math.round(retX), Math.round(retY), Math.round(srcW), Math.round(srcH), null, false);

    }
}
