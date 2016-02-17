package com.onemorebit.rxlab.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onemorebit.rxlab.model.IPDao;
import com.onemorebit.rxlab.model.ImageDao;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Euro on 2/15/16 AD.
 */
public class HttpManager {
    private static Retrofit retrofit;
    private static HttpManager httpManager;
    private ApiService apiService;

    public static HttpManager getInstance() {
        if(httpManager == null){
            httpManager = new HttpManager();
        }
        return httpManager;
    }

    public ApiService getNuuNeoiService(){
        if(retrofit == null) {

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

            retrofit = new Retrofit.Builder().baseUrl("http://nuuneoi.com/courses/500px/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
            apiService = retrofit.create(ApiService.class);

        }
        return apiService;
    }

    public interface ApiService{
        @GET("ip2") Observable<IPDao> getIP();
        @GET("list") Observable<ImageDao> getImage();
    }

}
