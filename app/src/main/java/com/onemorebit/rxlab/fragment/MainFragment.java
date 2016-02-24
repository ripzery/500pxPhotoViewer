package com.onemorebit.rxlab.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import com.onemorebit.rxlab.R;
import com.onemorebit.rxlab.adapter.PhotoListAdapter;
import com.onemorebit.rxlab.manager.HttpImageObservableManager;
import com.onemorebit.rxlab.manager.ImageManager;
import com.onemorebit.rxlab.util.Contextor;

/**
 * Created by Euro on 2/15/16 AD.
 */
public class MainFragment extends Fragment implements MediaScannerConnection.MediaScannerConnectionClient {

    private static final String FILE_TYPE = "image/*";
    private static final String SCAN_PATH = "/sdcard/RxLab";
    private View rootView;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeContainer;
    private RecyclerView recyclerView;
    private MediaScannerConnection conn;
    private PhotoListAdapter photoAdapter;
    private ImageManager imageManager;
    private LinearLayoutManager layoutManager;
    private Button btnNewPhotos;

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
        btnNewPhotos = (Button) rootView.findViewById(R.id.btnNewPhotos);
        imageManager = new ImageManager(getContext());
        layoutManager = new LinearLayoutManager(getContext());
        swipeContainer.setColorSchemeResources(R.color.colorAccent);
        swipeContainer.setOnRefreshListener(() -> loadImages(false));

        btnNewPhotos.setOnClickListener(v -> {
            hideBtnNewPhotos();
            recyclerView.smoothScrollToPosition(0);
        });

        loadImages(true);

        setHasOptionsMenu(true);
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_open_gallery:
                startScan();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startScan() {
        if (conn != null) {
            conn.disconnect();
        }
        conn = new MediaScannerConnection(getContext(), this);
        conn.connect();
    }

    private void loadImages(boolean isLoadFirstTime) {
        /* start request api */

        if (isLoadFirstTime) {

            progressDialog = ProgressDialog.show(getActivity(), "Please wait", "Loading images", true, false);

            HttpImageObservableManager.getInstance().fetchAllImagesObservable().onErrorReturn(throwable -> {
                progressDialog.hide();
                throwable.printStackTrace();
                return null;
            }).subscribe(dataEntities -> {
                if (dataEntities != null) {
                    progressDialog.hide();
                    imageManager.setListImages(dataEntities);
                    photoAdapter = new PhotoListAdapter(dataEntities);
                    recyclerView.setAdapter(photoAdapter);
                    recyclerView.setLayoutManager(layoutManager);
                    swipeContainer.setRefreshing(false);
                }
            });
        } else {

            HttpImageObservableManager.getInstance().fetchImageAfterIdObservable(imageManager.getMaximumPhotoId()).onErrorReturn(throwable -> {
                swipeContainer.setRefreshing(false);
                throwable.printStackTrace();
                return null;
            }).subscribe(dataEntities -> {
                if (dataEntities != null) {
                    imageManager.addListImagesAtTopPosition(dataEntities);
                    photoAdapter.setDataEntities(imageManager.getListImages());
                    View firstVisibleView = recyclerView.getChildAt(0);
                    int offsetTop = firstVisibleView.getTop();
                    int firstVisiblePosition = recyclerView.getChildLayoutPosition(firstVisibleView);
                    Log.d("TAG", "loadImages: " + (dataEntities.size() + firstVisiblePosition));
                    swipeContainer.setRefreshing(false);
                    if (dataEntities.size() > 0) {
                        /* show btn new photos */
                        showBtnNewPhotos();

                        photoAdapter.increaseLastPositionBy(dataEntities.size());
                        layoutManager.scrollToPositionWithOffset(dataEntities.size() + firstVisiblePosition, offsetTop);
                        //recyclerView.scrollToPosition(dataEntities.size()+1+ firstVisiblePosition);
                    }
                }
            });
        }
    }

    private void showBtnNewPhotos() {
        btnNewPhotos.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(Contextor.getInstance().getContext(), R.anim.anim_fade_in_scale_in);
        btnNewPhotos.setAnimation(anim);
    }

    private void hideBtnNewPhotos() {
        btnNewPhotos.setVisibility(View.GONE);
        Animation anim = AnimationUtils.loadAnimation(Contextor.getInstance().getContext(), R.anim.anim_fade_out_scale_out);
        btnNewPhotos.setAnimation(anim);
    }

    @Override public void onMediaScannerConnected() {
        conn.scanFile(SCAN_PATH, FILE_TYPE);
    }

    @Override public void onScanCompleted(String path, Uri uri) {
        try {
            Log.d("onScanCompleted", uri + "success" + conn);
            if (uri != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                intent.setDataAndType(uri, "image/*");
                startActivity(intent);
            }
        } finally {
            conn.disconnect();
            conn = null;
        }
    }
}

