package com.onemorebit.rxlab;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.onemorebit.rxlab.databinding.ActivityMainBinding;
import com.onemorebit.rxlab.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding mainActivityBinding;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainActivity.this.initInstance();
        //Observable<ImageDao> imageObv = HttpManager.getInstance().getNuuNeoiService().getAllImage();

        //RxView.clicks(mainActivityBinding.btnCallApi).subscribe(aVoid -> {
        //    imageObv
        //        .subscribeOn(Schedulers.io())
        //        .observeOn(AndroidSchedulers.mainThread())
        //        .unsubscribeOn(Schedulers.io())
        //        .map(imageDao -> imageDao.data)
        //        .flatMap(Observable::from)
        //        .filter(dataEntity -> dataEntity.camera != null)
        //        .toList()
        //        .subscribe(dataEntities -> {
        //            Log.d(TAG, "onCreate: " + dataEntities.size());
        //        });
        //});

        initInstance();

        //observable
        //    .doOnSubscribe(() -> {
        //        Log.d(TAG, "onCreate:  start request ");
        //    })
        //    .doOnCompleted(() -> {
        //        Log.d(TAG, "onCreate:  end request ");
        //    })
        //    .map(ipDao1 -> {
        //        ipDao1.origin = ipDao1.origin + " Hello ";
        //        return ipDao1;
        //    })
        //    .flatMap(ipDao2 -> observable2)
        //    .subscribeOn(Schedulers.io())
        //    .observeOn(AndroidSchedulers.mainThread())
        //    .subscribe(ipDao -> { mainActivityBinding.tvHello.setText(ipDao.origin); });

        //Observable.zip(observable, observable2, (ipDao1, ipDao2) -> ipDao1.origin + ", " +  ipDao2.origin)
        //    .subscribeOn(Schedulers.io())
        //    .observeOn(AndroidSchedulers.mainThread())
        //    .onErrorResumeNext(throwable -> Observable.just("Hello Error on " + throwable.toString()))
        //    .subscribe(onComplete);
    }

    private void initInstance() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, MainFragment.getInstance()).commit();
    }
}
