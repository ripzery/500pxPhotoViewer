package com.onemorebit.rxlab.util;

import android.content.Context;

/**
 * Created by Euro on 2/16/16 AD.
 */
public class Contextor {

    private static Contextor contextor;

    public static Contextor getInstance() {
        if(contextor == null){
            contextor = new Contextor();
        }
        return contextor;
    }

    private static Context mContext;

    public void init(Context context){
        mContext = context;
    }

    public Context getContext(){
        return mContext;
    }
}
