package com.jjc.qiqiharuniversity.common;

import android.content.Context;
import android.text.TextUtils;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Author jiajingchao
 * Created on 2021/4/16
 * Description: 缓存管理类
 */
public class CacheManager {
    private static final String TAG = CacheManager.class.getSimpleName();
    public static final String KEY_CACHE_SIZE_MAX = "cache_size_max";
    public static final int DEF_CACHE_SIZE_MAX = -1;//默认为未设限制
    public static final int CACHE_SIEZ_300M = 300;//300M
    public static final int CACHE_SIEZ_500M = 500;//500M
    public static final int CACHE_SIEZ_800M = 800;//800M
    public static final int CACHE_SIEZ_1G = 1024;//1G
    public static final int CACHE_SIEZ_2G = 2048;//2G
    public static final int CACHE_SIEZ_4G = 4096;//4G
    private static volatile CacheManager instance;
    private long lastCheckTime;
    private static final long delayTime = 500;

    public static CacheManager getInstance() {
        if (instance == null) {
            synchronized (CacheManager.class) {
                if (instance == null) {
                    instance = new CacheManager();
                }
            }
        }
        return instance;
    }

    public static class CachedMediaResInfo {
        public List<File> modifyTimeAscFileList;//修改时间升序列表
        public List<File> modifyTimeDescFileList;//修改时间降序列表
        public long mediaResWholeFileSize;//kb
    }

    public void clearCache(final Context context, CacheClearListener listener) {
        final CachedMediaResInfo resInfo = getCacheResInfo(context);
        if (resInfo != null) {
            clearCacheByFileList(0, resInfo.modifyTimeAscFileList, listener);
        } else {
            if (listener != null) {
                listener.onFailed();
            }
        }
    }

    private void clearGlideCache(final Context context) {
        Glide.get(context).clearDiskCache();
    }

    private void clearDownloadedRes(Context context) {
        FileManager.deleteDir(new File(FileManager.getDownloadPath(context)));
    }

    public void checkHasOutOfCache(Context context) {
        if (isFastClearCache()) {
            return;
        }
        long cacheMaxSizeMB = getCacheMaxSize(context);
        LogHelper.i(TAG, "checkHasOutOfCache cacheMaxSizeMB : " + cacheMaxSizeMB);
        if (cacheMaxSizeMB == DEF_CACHE_SIZE_MAX) {
            return;
        }
        CachedMediaResInfo resInfo = getCacheResInfo(context);
        if (resInfo != null) {
            long cachedFileSizeKB = resInfo.mediaResWholeFileSize;
            LogHelper.i(TAG, "checkHasOutOfCache cachedFileSizeKB : " + cachedFileSizeKB + " cacheMaxSizeMB * 1024 : " + cacheMaxSizeMB * 1024);
            if (cachedFileSizeKB > cacheMaxSizeMB * 1024) {
                float offset = cachedFileSizeKB - cacheMaxSizeMB * 1024;
                clearCacheByFileList(offset, resInfo.modifyTimeAscFileList, null);
            }
        }
    }

    /**
     * @param outRangeSize 超出上限多少 单位KB
     * @param fileList
     */
    private void clearCacheByFileList(float outRangeSize, List<File> fileList, CacheClearListener listener) {
        if (fileList == null || fileList.size() == 0) {
            if (listener != null) {
                listener.onFailed();
            }
            return;
        }
        LogHelper.i(TAG, "clearCacheByFileList outRangeSize : " + outRangeSize);
        List<File> clearFileList = null;
        if (outRangeSize == 0) {
            clearFileList = fileList;
        } else if (outRangeSize > 0) {
            clearFileList = getFileListNeedClear(outRangeSize, fileList);
        }
        doClearFileBackground(clearFileList, listener);
    }

    private void doClearFileBackground(final List<File> clearFileList, CacheClearListener listener) {
        if (clearFileList != null && !clearFileList.isEmpty()) {
            if (listener != null) {
                listener.onStart();
            }
            ExecutorService threadExecutor = Executors.newCachedThreadPool();
            LogHelper.i(TAG, "clearCacheByFileList clearFileList : " + clearFileList.size());
            Future<Integer> future = threadExecutor.submit(new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    LogHelper.i(TAG, "start");
                    for (int i = 0; i < clearFileList.size(); i++) {
                        FileManager.deleteFile(clearFileList.get(i));
                    }
                    return 1;
                }
            });
            try {
                if (future.get(2, TimeUnit.SECONDS) == 1) {
                    LogHelper.i(TAG, "clearCache");
                    if (listener != null) {
                        listener.onSuccess();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogHelper.i(TAG, "clearCache Exception : " + e.toString());
                if (listener != null) {
                    listener.onFailed();
                }
            } finally {
                future.cancel(true);
                if (!threadExecutor.isShutdown()) {
                    threadExecutor.shutdownNow();
                }
            }
        } else {
            if (listener != null) {
                listener.onFailed();
            }
        }
    }

    private List<File> getFileListNeedClear(float ourRangeSize, List<File> fileList) {
        List<File> result = new ArrayList<>();
        long size = 0;
        for (File file : fileList) {
            size = file.length() + size;
            result.add(file);
            if (size / 1024f >= ourRangeSize) {
                return result;
            }
        }
        return result;
    }

    private CachedMediaResInfo getCacheResInfo(Context context) {
        String downloadPath = FileManager.getDownloadPath(context);
        String streamMediaCachePath = FileManager.getStreamMediaCachePath(context);
        List<File> fileList = new ArrayList<>();
        fileList.add(Glide.getPhotoCacheDir(context));
        if (!TextUtils.isEmpty(downloadPath)) {
            fileList.add(new File(downloadPath));
            fileList.add(new File(streamMediaCachePath));
        }
        return FileManager.getCachedMediaResInfo(fileList);
    }

    public long getCachedResSizeInKB(Context context) {
        CachedMediaResInfo resInfo = getCacheResInfo(context);
        return resInfo == null ? 0 : resInfo.mediaResWholeFileSize;
    }

    public String getCachedResSizeStr(Context context) {
        long kb = getCachedResSizeInKB(context);
        if (kb <= 0) {
            return "0";
        } else if (kb > 0 && kb < 1024) {
            return kb + "K";
        } else if (kb >= 1024 && kb < 1024 * 1024) {
            float mb = kb / 1024f;
            long roundMB = Math.round(mb);
            return roundMB + "M";
        } else {
            float gb = kb / 1024f / 1024f;
            long roundGB = Math.round(gb);
            return roundGB + "G";
        }
    }

    public int getCacheMaxSize(Context context) {
        return SPManager.getInstance().getInt(context, KEY_CACHE_SIZE_MAX, DEF_CACHE_SIZE_MAX);
    }

    public void setCacheMaxSize(Context context, int size) {
        SPManager.getInstance().putInt(context, KEY_CACHE_SIZE_MAX, size);
    }

    public String getCacheMaxSizeStr(Context context) {
        int size = getCacheMaxSize(context);
        return getCacheMaxSizeStr(size);
    }

    public String getCacheMaxSizeStr(int size) {
        String str;
        switch (size) {
            case CACHE_SIEZ_300M:
                str = "300M";
                break;
            case CACHE_SIEZ_500M:
                str = "500M";
                break;
            case CACHE_SIEZ_800M:
                str = "800M";
                break;
            case CACHE_SIEZ_1G:
                str = "1G";
                break;
            case CACHE_SIEZ_2G:
                str = "2G";
                break;
            case CACHE_SIEZ_4G:
                str = "4G";
                break;
            case DEF_CACHE_SIZE_MAX:
            default:
                str = "不限制";
                break;
        }
        return str;
    }

    private boolean isFastClearCache() {
        long time = System.currentTimeMillis();
        long timeD = time - lastCheckTime;
        if (0 < timeD && timeD < delayTime) {
            return true;
        }
        lastCheckTime = time;
        return false;
    }

    public interface CacheClearListener {
        void onStart();

        void onSuccess();

        void onFailed();
    }

}
