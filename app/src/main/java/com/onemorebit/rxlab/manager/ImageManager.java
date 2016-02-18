package com.onemorebit.rxlab.manager;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.onemorebit.rxlab.R;
import com.onemorebit.rxlab.model.ImageDao;
import com.onemorebit.rxlab.util.Contextor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import rx.Observable;

/**
 * Created by Euro on 2/17/16 AD.
 */
public class ImageManager {
    private static final String TAG = "ImageManager";
    private static ImageManager ourInstance;
    private Context context;

    public static ImageManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new ImageManager();
        }
        return ourInstance;
    }

    private ImageManager() {
        context = Contextor.getInstance().getContext();
    }

    public Observable<BaseDownloadTask> getImageDownloaderObservable(ImageDao.DataEntity imageDao) {

        Observable<BaseDownloadTask> taskObservable = Observable.create(subscriber -> {

            FileDownloader.getImpl().create(imageDao.imageUrl)
                .setPath(getFilePathName(imageDao))
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        subscriber.onNext(task);
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        subscriber.onNext(task);
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        subscriber.onNext(task);
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                        Log.d(TAG, "blockComplete: ");
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                        Log.d(TAG, "retry: ");
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        Log.d(TAG, "completed: ");
                        subscriber.onNext(task);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.d(TAG, "paused: ");
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        subscriber.onNext(task);
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        Log.d(TAG, "warn: ");
                    }
                })
                .start();

        });

        return taskObservable;
    }

    private String getFilePathName(ImageDao.DataEntity imageDao) {
        return getExternalPhotoPath(imageDao.caption.trim() + imageDao.createdTime.getTime() + ".jpg", "Photo by " + imageDao.username);
    }

    public boolean isHasFileInStorage(ImageDao.DataEntity imageDao){
        File myFile = new File(getFilePathName(imageDao));
        return myFile.exists();
    }

    public String getExternalPhotoPath(String filename, String description) {
        // Get the directory for the app's private pictures directory.
        File file = new File("/sdcard/RxLab", filename);

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, filename);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis ());
        values.put(MediaStore.Images.ImageColumns.BUCKET_ID, file.toString().toLowerCase(Locale.US).hashCode());
        values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, file.getName().toLowerCase(Locale.US));
        values.put("_data", file.getAbsolutePath());

        ContentResolver cr = context.getContentResolver();
        cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        return file.getAbsolutePath();
    }
}
