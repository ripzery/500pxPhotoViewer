package com.onemorebit.rxlab.util;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by Euro on 2/16/16 AD.
 */
public class GlideConfig implements GlideModule {
    @Override public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override public void registerComponents(Context context, Glide glide) {

    }
}
