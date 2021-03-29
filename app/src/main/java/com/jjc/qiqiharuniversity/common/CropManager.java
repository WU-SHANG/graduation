package com.jjc.qiqiharuniversity.common;

import android.Manifest;
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
 * Created on 2021/3/28
 * Description:图片裁剪
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

    public void pickFromCamera() {
        if (activity == null) {
            return;
        }

//        Intent intent = new Intent();
//        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//        activity.startActivityForResult(intent, REQUEST_MODE_CAMERA);

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

//        uCrop = basisConfig(uCrop);
//        uCrop = advancedConfig(uCrop);

//        if (requestMode == REQUEST_SELECT_PICTURE_FOR_FRAGMENT) {       //if build variant = fragment
//            setupFragment(uCrop);
//        } else {                                                        // else start uCrop Activity
//            uCrop.start(SampleActivity.this);
//        }

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

//        showNotification(saveFile);
//        Toast.makeText(this, R.string.notification_image_saved, Toast.LENGTH_SHORT).show();
//        finish();

        if (cropListener != null) {
            cropListener.onCropFile(saveFilePath);
        }
    }

//    private void showNotification(@NonNull File file) {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Uri fileUri = FileProvider.getUriForFile(
//                this,
//                getString(R.string.file_provider_authorities),
//                file);
//
//        intent.setDataAndType(fileUri, "image/*");
//
//        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(
//                intent,
//                PackageManager.MATCH_DEFAULT_ONLY);
//        for (ResolveInfo info : resInfoList) {
//            grantUriPermission(
//                    info.activityInfo.packageName,
//                    fileUri, FLAG_GRANT_WRITE_URI_PERMISSION | FLAG_GRANT_READ_URI_PERMISSION);
//        }
//
//        NotificationCompat.Builder notificationBuilder;
//        NotificationManager notificationManager = (NotificationManager) this
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            if (notificationManager != null) {
//                notificationManager.createNotificationChannel(createChannel());
//            }
//            notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
//        } else {
//            notificationBuilder = new NotificationCompat.Builder(this);
//        }
//
//        notificationBuilder
//                .setContentTitle(getString(R.string.app_name))
//                .setContentText(getString(R.string.notification_image_saved_click_to_preview))
//                .setTicker(getString(R.string.notification_image_saved))
//                .setSmallIcon(R.drawable.ic_done)
//                .setOngoing(false)
//                .setContentIntent(PendingIntent.getActivity(this, 0, intent, 0))
//                .setAutoCancel(true);
//        if (notificationManager != null) {
//            notificationManager.notify(DOWNLOAD_NOTIFICATION_ID_DONE, notificationBuilder.build());
//        }
//    }

    public interface CropListener {
        void onCropFile(String filePath);
    }
}
