package com.onemorebit.rxlab.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.onemorebit.rxlab.R;
import com.onemorebit.rxlab.model.ImageDao;
import com.onemorebit.rxlab.viewgroup.PhotoViewGroup;
import java.util.List;

/**
 * Created by Euro on 2/15/16 AD.
 */
public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.MyHolder>{

    private final List<ImageDao.DataEntity> dataEntities;

    public PhotoListAdapter(List<ImageDao.DataEntity> dataEntities) {
        this.dataEntities = dataEntities;
    }

    @Override public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyHolder myHolder = new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_view, parent, false));
        return myHolder;
    }

    @Override public void onBindViewHolder(MyHolder holder, int position) {
        /* to do next */
        holder.photoViewGroup.setPhotoDetail(dataEntities.get(position));
    }

    @Override public int getItemCount() {
        return dataEntities.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private PhotoViewGroup photoViewGroup;

        public MyHolder(View itemView) {
            super(itemView);
            photoViewGroup = (PhotoViewGroup) itemView.findViewById(R.id.item);
        }

    }
}
