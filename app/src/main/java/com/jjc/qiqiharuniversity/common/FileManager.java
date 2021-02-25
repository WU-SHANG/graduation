package com.jjc.qiqiharuniversity.common;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import org.apache.http.util.EncodingUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Author jiajingchao
 * Created on 2021/2/23
 * Description:文件管理
 */
public class FileManager {

    public final static String FOLDER_SAVE = "save_temp";
    public final static String FOLDER_SCREEN_RECORD = "screen_record";
    public final static String FOLDER_SHARE_WXMINI_THUMB = "share_wxmini_img";

    public static String getDownloadPath(Context context) {

        boolean externalStorageAvailable = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (externalStorageAvailable) {
            File externalDir = context.getExternalFilesDir("");
            if (externalDir != null) {
                return externalDir.getPath();
            }
        }
        return context.getFilesDir().getPath();
    }

    public static String getSavePath(Context context) {
        return getDownloadPath(context) + File.separator + FOLDER_SAVE;
    }

    public static String getFilesDir(@NonNull Context context, @NonNull String folderName) {
        return context.getFilesDir().getPath() + File.separator + folderName;
    }

    public static String getCacheDir(@NonNull Context context, @NonNull String folderName) {
        return context.getCacheDir().getPath() + File.separator + folderName;
    }

    public static String getExternalFilesDir(@NonNull Context context, @NonNull String folderName) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            // 外部存储可用
            File file = context.getExternalFilesDir(folderName);
            if (file != null) {
                return file.getPath();
            }

        }

        return context.getFilesDir().getPath() + File.separator + folderName;
    }

    public static boolean illegal(File file) {
        return file == null || !file.exists();
    }

    public static String readSDFile(File file) {
        String res = "";
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            int length = fis.available();
            byte[] buffer = new byte[length];
            fis.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    public static void writeSDFile(File file, String content) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            byte[] bytes = content.getBytes();
            fos.write(bytes);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void deleteDir(File dir) {
        try {
            if (dir == null || !dir.exists() || !dir.isDirectory()) {
                return;
            }

            for (File file : dir.listFiles()) {
                if (file.isFile()) {
                    file.delete(); // 删除所有文件
                } else if (file.isDirectory()) {
                    deleteDir(file); // 递规的方式删除文件夹
                }
            }

            dir.delete();// 删除目录本身
        } catch (Exception e) {
            // Empty
        }
    }

    public static void deleteFile(File path) {
        try {
            if (path == null || !path.exists() || path.isDirectory()) {
                return;
            }
            path.delete();
        } catch (Exception e) {
            // Empty
        }
    }

    public static long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static long getCacheFolderSize(@NonNull Context context) {
        long size = getFolderSize(context.getCacheDir());
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            // 外部存储可用
            size = size + getFolderSize(context.getExternalCacheDir());
        }

        return size;
    }

    public static void clearCacheFolder(@NonNull Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            // 外部存储可用
            deleteDir(context.getExternalCacheDir());
        }
    }


//    public static void saveFile(Context context, InputStream inputStream, String fileName) {
//
//        OutputStream os = null;
//        try {
//            String path = getDownloadPath(context);
//            // 2、保存到临时文件
//            // 1K的数据缓冲
//            byte[] bs = new byte[1024];
//            // 读取到的数据长度
//            int len;
//            // 输出的文件流保存到本地文件
//            String fileDirName = "";//文件路径
//            String fileRealName = fileName;//真实文件名
//            if (fileName.contains(File.separator)) {
//                int lastIndex = fileName.lastIndexOf(File.separator);
//                if (lastIndex >= 0) {
//                    fileDirName = fileName.substring(0, lastIndex);
//                    fileRealName = fileName.substring(lastIndex + 1, fileName.length());
//                    if (!TextUtils.isEmpty(fileDirName)) {
//                        path = path + File.separator + fileDirName;
//                    }
//                }
//            }
//            File tempFile = new File(path);
//            if (!tempFile.exists()) {
//                tempFile.mkdirs();
//            }
//            os = new FileOutputStream(tempFile.getPath() + File.separator + fileRealName);
//            // 开始读取
//            while ((len = inputStream.read(bs)) != -1) {
//                os.write(bs, 0, len);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            // 完毕，关闭所有链接
//            try {
//                os.close();
//                inputStream.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    /**
     * //新增校验fileName是否合法，如果包含路径名的话，就先创建文件夹，再写文件
     *
     * @param context
     * @param inputStream
     * @param fileName
     */
    public static String saveFile(Context context, InputStream inputStream, String fileName) {
        String ret = "0";
        OutputStream os = null;
        String path = FileManager.getSavePath(context);
        try {
            String originPath = path;
            // 2、保存到临时文件
            // 1K的数据缓冲
            byte[] bs = new byte[2048];
            // 读取到的数据长度
            int len;
            // 输出的文件流保存到本地文件
            String fileDirName = "";//文件路径
            String fileRealName = fileName;//真实文件名
            if (fileName.contains(File.separator)) {
                int lastIndex = fileName.lastIndexOf(File.separator);
                if (lastIndex >= 0) {
                    fileDirName = fileName.substring(0, lastIndex);
                    fileRealName = fileName.substring(lastIndex + 1, fileName.length());
                    if (!TextUtils.isEmpty(fileDirName)) {
                        originPath = originPath + File.separator + fileDirName;
                    }
                }
            }
            File tempFile = new File(originPath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            os = new FileOutputStream(tempFile.getPath() + File.separator + fileRealName);
            // 开始读取
            while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);
            }

        } catch (Exception e) {
            e.printStackTrace();
            ret = e.toString();
            Log.i("FileManager", "catch (Exception e) fileName : " + fileName + " ret : " + ret);
        } finally {
            // 完毕，关闭所有链接
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
//                return -3;
            }
            Log.i("FileManager", "finally  fileName : " + fileName + " ret : " + ret);
            if ("0".equals(ret)) {
                String srcFilePath = path + File.separator + fileName;
                String dstFilePath = getDownloadPath(context);

                ret = String.valueOf(renameFile(srcFilePath, dstFilePath, fileName));
//                ret = copyFile(srcFilePath, dstFilePath, fileName);
            }
        }
        return ret;
    }

    public static int renameFile(String oldPath, String newPath, String fileName) {
        String originPath = newPath;
        String fileDirName = "";//文件路径
        String fileRealName = fileName;//真实文件名
        if (fileName.contains(File.separator)) {
            int lastIndex = fileName.lastIndexOf(File.separator);
            if (lastIndex >= 0) {
                fileDirName = fileName.substring(0, lastIndex);
                fileRealName = fileName.substring(lastIndex + 1, fileName.length());
                if (!TextUtils.isEmpty(fileDirName)) {
                    originPath = originPath + File.separator + fileDirName;
                }
            }
        }
        File tempFile = new File(originPath);
        if (tempFile.exists() || tempFile.mkdirs()) {
            File srcFile = new File(oldPath);
            if (srcFile.exists()) {
                boolean success = srcFile.renameTo(new File(tempFile.getPath(), fileRealName));
                return success ? 1 : -2;
            } else {
                return -2;
            }
        } else {
            return -2;
        }

    }

    public static int copyFile(String oldPath, String newPath, String fileName) {
        int ret = 0;
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            String originPath = newPath;
            String fileDirName = "";//文件路径
            String fileRealName = fileName;//真实文件名
            if (fileName.contains(File.separator)) {
                int lastIndex = fileName.lastIndexOf(File.separator);
                if (lastIndex >= 0) {
                    fileDirName = fileName.substring(0, lastIndex);
                    fileRealName = fileName.substring(lastIndex + 1, fileName.length());
                    if (!TextUtils.isEmpty(fileDirName)) {
                        originPath = originPath + File.separator + fileDirName;
                    }
                }
            }
            File tempFile = new File(originPath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            fileInputStream = new FileInputStream(oldPath);
            fileOutputStream = new FileOutputStream(tempFile.getPath() + File.separator + fileRealName);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fileInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = -2;
            return ret;
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
//                return -4;
            }
            deleteFile(new File(oldPath));
            ret = 1;
        }
        return ret;
    }

    public static void copyFolder(String oldPath, String newPath) {
        try {
            File newFile = new File(newPath);
            if (!newFile.exists()) {
                if (!newFile.mkdirs()) {
                    return;
                }
            }
            File oldFile = new File(oldPath);
            String[] files = oldFile.list();
            File temp;
            for (String file : files) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file);
                } else {
                    temp = new File(oldPath + File.separator + file);
                }

                if (temp.isDirectory()) {   //如果是子文件夹
                    copyFolder(oldPath + "/" + file, newPath + "/" + file);
                } else if (temp.exists() && temp.isFile() && temp.canRead()) {
                    FileInputStream fileInputStream = new FileInputStream(temp);
                    FileOutputStream fileOutputStream = new FileOutputStream(newPath + "/" + temp.getName());
                    byte[] buffer = new byte[1024];
                    int byteRead;
                    while ((byteRead = fileInputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, byteRead);
                    }
                    fileInputStream.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //判断文件是否存在
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public static String getJsonFromAssets(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static String createScreenRecordRootPath(Context context) {
        String path = getExternalFilesDir(context, FOLDER_SCREEN_RECORD);
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path + File.separator;
    }

    public static String createShareWXMiniImagePath(Context context) {
        String path = getExternalFilesDir(context, FOLDER_SHARE_WXMINI_THUMB);
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path + File.separator;
    }

    public static String createFloder(String path, String floderName) {
        File file = new File(path, floderName);
        if (!file.exists()) {
            if (file.mkdirs()) {
                return file.getAbsolutePath();
            } else {
                return null;
            }
        } else {
            return file.getAbsolutePath();
        }
    }

    public static boolean createFile(String path, String name) {
        File file = new File(path, name);
        return createFile(file);
    }

    public static boolean createFile(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return false;
        }
        File file = new File(fileName);
        return createFile(file);
    }

    public static boolean createFile(File file) {
        try {
            if (!file.exists()) {
                return file.createNewFile();
            } else {
                return true;
            }
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean checkFile(String filePath) {
        //boolean result = FileUtil.fileIsExist(filePath);
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        boolean result = false;
        File mFile = new File(filePath);
        if (mFile.exists()) {
            result = true;
        }
        return result;
    }

    public static boolean insertVideoToMediaStore(Context context, File file) {
        OutputStream outputStream = null;
        FileInputStream fileInputStream = null;
        ToastManager.show(context, "开始保存视频到相册，请稍后");
        boolean rtn = false;
        try {
            ContentValues contentValues = new ContentValues();
            //设置文件名
            contentValues.put(MediaStore.Video.Media.DISPLAY_NAME, file.getName());
            contentValues.put(MediaStore.Video.VideoColumns.WIDTH, 1920);
            contentValues.put(MediaStore.Video.VideoColumns.HEIGHT, 1080);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.put(MediaStore.MediaColumns.ORIENTATION, 0);
            } else {
                contentValues.put("orientation", 0);
            }
            Uri uri = context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
            if (uri != null) {
                outputStream = context.getContentResolver().openOutputStream(uri);
                fileInputStream = new FileInputStream(file.getAbsolutePath());
                byte[] buffer = new byte[1024];
                int byteRead;
                LogHelper.i("WRTAG", "写入开始");
                while (-1 != (byteRead = fileInputStream.read(buffer))) {
                    outputStream.write(buffer, 0, byteRead);
                }
            } else {
                ToastManager.show(context, "视频保存失败！");
                LogHelper.i("WRTAG", "uri == null");
                rtn = false;
                return rtn;
            }
        } catch (Exception e) {
            ToastManager.show(context, "视频保存失败！");
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
                LogHelper.i("WRTAG", "写入完成");
                ToastManager.show(context, "视频保存成功，请到相册中查看");
                rtn = true;

            } catch (IOException e) {
                ToastManager.show(context, "视频保存失败！");
                e.printStackTrace();
                rtn = false;
            }
        }
        return rtn;

    }


    public static boolean unzip(String targetPath, String zipFilePath) {
        try {
            int BUFFER = 2048;
            String fileName = zipFilePath;
            String filePath = targetPath;
            ZipFile zipFile = new ZipFile(fileName);
            Enumeration emu = zipFile.entries();
            while (emu.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) emu.nextElement();
                if (entry.isDirectory()) {
                    new File(filePath + entry.getName()).mkdirs();
                    continue;
                }
                BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
                File file = new File(filePath + entry.getName());
                File parent = file.getParentFile();
                if (parent != null && (!parent.exists())) {
                    parent.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);

                int count;
                byte data[] = new byte[BUFFER];
                while ((count = bis.read(data, 0, BUFFER)) != -1) {
                    bos.write(data, 0, count);
                }
                bos.flush();
                bos.close();
                bis.close();
            }
            zipFile.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void getLottieResFileList(String targetPath) {
        try {
            File rushDir = new File(targetPath);
            if (rushDir.isDirectory()) {
                File[] fileList = rushDir.listFiles();
                for (File file : fileList) {
                    if (file.isFile()) {
                        if (file.getName().endsWith(".json")) {
                            String fileName = file.getName();
                            String path = file.getAbsolutePath();
                            LogHelper.i("targetPath", fileName + "____" + path);
                            FileManager.renameFile(path, targetPath + File.separator + "json" + File.separator, fileName);

                        } else if (file.getName().endsWith(".png") || file.getName().endsWith(".jpeg")) {
                            String fileName = file.getName();
                            String path = file.getAbsolutePath();
//                            info.videoResultFileName = folderName + ".png";
                            LogHelper.i("targetPath", fileName + "____" + path);
                            FileManager.renameFile(path, targetPath + File.separator + "images" + File.separator, fileName);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}