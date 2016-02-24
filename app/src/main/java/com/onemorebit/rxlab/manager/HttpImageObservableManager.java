package com.onemorebit.rxlab.manager;

import android.app.ProgressDialog;
import android.util.Log;
import com.onemorebit.rxlab.model.dao.ImageDao;
import com.onemorebit.rxlab.util.ComposeObservable;
import java.util.List;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Euro on 2/24/16 AD.
 */
public class HttpImageObservableManager {

    private static HttpImageObservableManager httpImageObservableManager;

    public static HttpImageObservableManager getInstance() {
        if(httpImageObservableManager == null){
            httpImageObservableManager = new HttpImageObservableManager();
        }
        return httpImageObservableManager;
    }

    public HttpImageObservableManager() {

    }

    public Observable<List<ImageDao.DataEntity>> fetchAllImagesObservable(){
        Observable<ImageDao> observable = HttpManager.getInstance().getNuuNeoiService().getAllImage()
            .compose(ComposeObservable.applySchedulers());

        Observable<List<ImageDao.DataEntity>> listObservable = observable.map(imageDao -> imageDao.data)
            .flatMap(Observable::from)
            .toList();


        return listObservable;
    }

    public Observable<List<ImageDao.DataEntity>> fetchImageAfterIdObservable(int id){
        Observable<ImageDao> newerImageObservable = HttpManager.getInstance()
            .getNuuNeoiService()
            .getImageAfterId(id)
            .compose(ComposeObservable.applySchedulers());

        Observable<List<ImageDao.DataEntity>> listObservable = newerImageObservable.map(imageDao -> imageDao.data)
            .flatMap(Observable::from)
            .toList();


        return listObservable;
    }
}
