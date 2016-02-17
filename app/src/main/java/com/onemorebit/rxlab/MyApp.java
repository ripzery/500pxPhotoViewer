package com.onemorebit.rxlab;

import android.app.Application;
import com.liulishuo.filedownloader.FileDownloader;
import com.onemorebit.rxlab.util.Contextor;

/**
 * Created by Euro on 2/16/16 AD.
 */
public class MyApp extends Application {

    @Override public void onCreate() {
        super.onCreate();
        Contextor.getInstance().init(this);
        FileDownloader.init(this);
    }
}
