package com.jjc.qiqiharuniversity.common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * Author jiajingchao
 * Created on 2021/2/23
 * Description:相机，相册管理
 */
public class CropManager {

    private static final String TAG = CropManager.class.getSimpleName();
    private static final int REQUEST_MODE = 1;
    private static final int REQUEST_MODE_CAMERA = 2;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";
    public static final String RATIO_1_1 = "1,1";
    public static final String RATIO_3_4 = "3,4";

    private Activity activity;
    private String ratio;
    private int maxWidth;
    private int maxHeight;
    private String saveFilePath;
    private Uri imageUri;
    private CropListener cropListener;

    public CropManager(Activity activity) {
        this.activity = activity;
    }

    public void setCropListener(CropListener cropListener) {
        this.cropListener = cropListener;
    }

    public void setSaveFilePath(String saveFilePath) {
        this.saveFilePath = saveFilePath;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public void setMaxWidthAndHeight(int maxWidth, int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    public void pickFromGallery() {
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT)
                .setType("image/*")
                .addCategory(Intent.CATEGORY_OPENABLE);

        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        activity.startActivityForResult(Intent.createChooser(intent, ""), REQUEST_MODE);
    }

    @SuppressLint("QueryPermissionsNeeded")
    public void pickFromCamera() {
        if (activity == null) {
            return;
        }

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//打开相机的Intent
        if (takePhotoIntent.resolveActivity(activity.getPackageManager()) != null) {//这句作用是如果没有相机则该应用不会闪退，要是不加这句则当系统没有相机应用的时候该应用会闪退
            File imageFile = createImageFile();//创建用来保存照片的文件
            if (imageFile != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    /*7.0以上要通过FileProvider将File转化为Uri*/
                    String authority = activity.getApplication().getPackageName() + ".fileProvider";
                    imageUri = FileProvider.getUriForFile(activity, authority, imageFile);
                } else {
                    /*7.0以下则直接使用Uri的fromFile方法将File转化为Uri*/
                    imageUri = Uri.fromFile(imageFile);
                }
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将用于输出的文件Uri传递给相机
                activity.startActivityForResult(takePhotoIntent, REQUEST_MODE_CAMERA);//打开相机
            }
        }
    }

    private File createImageFile() {
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_MODE) {
                final Uri selectedUri = data.getData();

                if (selectedUri != null) {
                    startCrop(selectedUri);
                } else {
                    LogHelper.i(TAG, "onActivityResult selectedUri is null");
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            } else if (requestCode == REQUEST_MODE_CAMERA) {
                startCrop(imageUri);
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
//            handleCropError(data);
            LogHelper.i(TAG, "onActivityResult UCrop.RESULT_ERROR");
        }
    }

    private void startCrop(Uri uri) {
        if (activity == null) {
            return;
        }

        if (uri == null) {
            return;
        }

        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME;

//        switch (mRadioGroupCompressionSettings.getCheckedRadioButtonId()) {
//            case R.id.radio_png:
//                destinationFileName += ".png";
//                break;
//            case R.id.radio_jpeg:
//                destinationFileName += ".jpg";
//                break;
//        }

        destinationFileName += ".jpg";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(activity.getCacheDir(), destinationFileName)));
        UCrop.Options options = new UCrop.Options();
        // 设置圆形裁剪框
        options.setCircleDimmedLayer(true);
        uCrop.withOptions(options);

        if (!TextUtils.isEmpty(ratio)) {
            if (ratio.equals(RATIO_1_1)) {
                uCrop = uCrop.withAspectRatio(1, 1);
            } else if (ratio.equals(RATIO_3_4)) {
                uCrop = uCrop.withAspectRatio(3, 4);
            }
        }

        if (maxWidth != 0 && maxHeight != 0) {
            uCrop = uCrop.withMaxResultSize(maxWidth, maxHeight);
        }

        uCrop.start(activity);
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            saveCroppedImage(resultUri);
        } else {
            LogHelper.i(TAG, "handleCropResult resultUri is null");
        }
    }

    private void saveCroppedImage(Uri imageUri) {
        if (activity == null) {
            return;
        }

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            LogHelper.i(TAG, "saveCroppedImage checkSelfPermission error");
        } else {
            if (imageUri != null && imageUri.getScheme().equals("file")) {
                try {
                    copyFileToDownloads(imageUri);
                } catch (Exception e) {
                    LogHelper.i(TAG, "saveCroppedImage error " + e.getMessage());
                }
            } else {
                LogHelper.i(TAG, "saveCroppedImage imageUri null");
            }
        }
    }

    private void copyFileToDownloads(Uri croppedFileUri) throws Exception {
        if (TextUtils.isEmpty(saveFilePath)) {
            return;
        }

//        String downloadsDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
//        String filename = String.format("%d_%s", Calendar.getInstance().getTimeInMillis(), croppedFileUri.getLastPathSegment());

        File saveFile = new File(saveFilePath);

        FileInputStream inStream = new FileInputStream(new File(croppedFileUri.getPath()));
        FileOutputStream outStream = new FileOutputStream(saveFile);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();

        if (cropListener != null) {
            cropListener.onCropFile(saveFilePath);
        }
    }

    public interface CropListener {
        void onCropFile(String filePath);
    }
}
