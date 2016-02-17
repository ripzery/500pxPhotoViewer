package com.onemorebit.rxlab.viewgroup;

import android.content.Context;
import android.media.Image;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.fenjuly.library.ArrowDownloadButton;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.onemorebit.rxlab.R;
import com.onemorebit.rxlab.manager.ImageManager;
import com.onemorebit.rxlab.model.ImageDao;
import com.panwrona.downloadprogressbar.library.DownloadProgressBar;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * TODO: document your custom view class.
 */
public class PhotoViewGroup extends FrameLayout {

    private static final String TAG = "PhotoViewGroup";
    private View view;
    private ImageView imageView;
    private TextView tvTitle;
    private TextView tvDesc;
    private ProgressBar progress;
    private View layoutView;
    private ImageDao.DataEntity imageDao;
    private DownloadProgressBar progressDownloader;
    private ArrowDownloadButton btnArrowDownload;

    public PhotoViewGroup(Context context) {
        super(context);
        initInflate();
        initInstance(null, 0);
    }

    public PhotoViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstance(attrs, 0);
    }

    public PhotoViewGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initInflate();
        initInstance(attrs, defStyle);
    }

    private void initInflate() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.photo_view_group, this);
    }

    private void initInstance(AttributeSet attrs, int defStyle) {
        layoutView = view.findViewById(R.id.layoutView);
        imageView = (ImageView) view.findViewById(R.id.ivPhoto);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvDesc = (TextView) view.findViewById(R.id.tvDesc);
        //progressDownloader = (DownloadProgressBar) view.findViewById(R.id.downloadProgressBar);
        btnArrowDownload = (ArrowDownloadButton) view.findViewById(R.id.btnArrowDownload);
        progress = (ProgressBar) view.findViewById(R.id.progressBar);

        layoutView.setOnClickListener(v -> {
            Log.d(TAG, "initInstance: layoutView clicked");
        });

        btnArrowDownload.setOnClickListener(v -> {
            Observable<BaseDownloadTask> taskObservable = ImageManager.getInstance().getImageDownloaderObservable(imageDao);
            btnArrowDownload.reset();
            btnArrowDownload.startAnimating();
            taskObservable.compose(applySchedulers())
                .distinct(BaseDownloadTask::getSmallFileSoFarBytes)
                .filter(baseDownloadTask1 -> baseDownloadTask1.getSmallFileTotalBytes() != 0)
                .subscribe(baseDownloadTask -> {
                    if(baseDownloadTask.getSmallFileSoFarBytes() == 0) {
                        Toast.makeText(getContext(), "Start download", Toast.LENGTH_SHORT).show();
                    }
                    else if (baseDownloadTask.getStatus() == -3) {
                        // finish
                        Toast.makeText(getContext(), "Download " + " finish!", Toast.LENGTH_SHORT).show();
                        btnArrowDownload.setProgress(100);
                        btnArrowDownload.setEnabled(false);
                    } else {
                        // update progress
                        int progress = (baseDownloadTask.getSmallFileSoFarBytes() * 100 / baseDownloadTask.getSmallFileTotalBytes());
                        btnArrowDownload.setProgress(progress);
                        Log.d(TAG, "Subscribe" + " : " + baseDownloadTask.getSmallFileSoFarBytes() + "/" + baseDownloadTask.getSmallFileTotalBytes());
                    }
                });
        });

        //progressDownloader.setOnClickListener(v -> {
        //
        //});

        layoutView.setOnLongClickListener(v -> {
            Log.d(TAG, "onLongClick: ");

            /* download image */

            Observable<BaseDownloadTask> taskObservable = ImageManager.getInstance().getImageDownloaderObservable(imageDao);

            taskObservable.compose(applySchedulers())
                .distinct(BaseDownloadTask::getSmallFileSoFarBytes)
                .subscribe(baseDownloadTask -> {

                if(baseDownloadTask.getSmallFileSoFarBytes() == 0) {
                    Toast.makeText(getContext(), "Start download", Toast.LENGTH_SHORT).show();
                }
                else if (baseDownloadTask.getStatus() == -3) {
                    // finish
                    Toast.makeText(getContext(), "Download " + " finish!", Toast.LENGTH_SHORT).show();
                } else {
                    // update progress
                    Log.d(TAG, "Subscribe" + " : " + baseDownloadTask.getSmallFileSoFarBytes() + "/" + baseDownloadTask.getSmallFileTotalBytes());
                }
            });

            return true;
        });
    }

    public void setPhotoDetail(ImageDao.DataEntity imageDao) {
        this.imageDao = imageDao;
        progress.setVisibility(View.VISIBLE);
        Glide.with(view.getContext())
            .load(imageDao.imageUrl)
            .placeholder(R.drawable.orange_banana_wallpaper)
            .listener(new RequestListener<String, GlideDrawable>() {
                @Override public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache,
                    boolean isFirstResource) {
                    progress.setVisibility(View.GONE);
                    return false;
                }
            })
            .into(imageView);
        tvTitle.setText(imageDao.caption);
        tvDesc.setText(imageDao.username);

        if(ImageManager.getInstance().isHasFileInStorage(imageDao)){
            btnArrowDownload.setProgress(100);
            btnArrowDownload.setEnabled(false);
        }else{
            btnArrowDownload.reset();
            btnArrowDownload.setEnabled(true);
        }

    }

    private void initWithAttrs(AttributeSet attrs) {

    }

    public <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.io());
    }

    @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = width * 2 / 3;
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);


        /* Child view */
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);

        /* Parent View */
        setMeasuredDimension(width, height);
    }
}
