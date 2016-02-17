package com.onemorebit.rxlab.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.onemorebit.rxlab.R;
import com.onemorebit.rxlab.adapter.PhotoListAdapter;
import com.onemorebit.rxlab.manager.HttpManager;
import com.onemorebit.rxlab.model.ImageDao;
import org.antlr.v4.codegen.model.Loop;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Euro on 2/15/16 AD.
 */
public class MainFragment extends Fragment {

    private View rootView;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView recyclerView;

    public static MainFragment getInstance() {

        final MainFragment mainFragment = new MainFragment();

        /* create bundle */
        Bundle args = new Bundle();

        return mainFragment;
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        initInstance();

        return rootView;
    }

    private void initInstance() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(() -> loadImages(false));

        loadImages(true);

    }

    private void loadImages(boolean isLoadFirstTime){
        /* start request api */

        Observable<ImageDao> observable = HttpManager.getInstance().getNuuNeoiService().getImage();

        if(isLoadFirstTime)
            progressDialog = ProgressDialog.show(getActivity(), "Please wait", "Loading images", true, false);

        observable
            .doOnCompleted(() -> {
                progressDialog.dismiss();
                Log.d("Hello", "initInstance: dismiss");
            })
            .map(imageDao -> imageDao.data)
            .flatMap(Observable::from)
            .toList()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
            .onErrorReturn(throwable -> {
                swipeContainer.setRefreshing(false);
                throwable.printStackTrace();
                return null;
            })
            .subscribe(dataEntities -> {
                if(dataEntities != null) {
                    final PhotoListAdapter adapter = new PhotoListAdapter(dataEntities);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    swipeContainer.setRefreshing(false);
                }
            });
    }
}
