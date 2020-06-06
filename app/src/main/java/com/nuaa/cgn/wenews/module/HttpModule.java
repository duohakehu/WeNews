package com.nuaa.cgn.wenews.module;

import com.nuaa.cgn.wenews.WNApplication;
import com.nuaa.cgn.wenews.network.ApiConstants;
import com.nuaa.cgn.wenews.network.NewsNetWork;
import com.nuaa.cgn.wenews.network.RetrofitConfig;
import com.nuaa.cgn.wenews.network.api.WeNewsApi;

import java.io.File;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cuiguonan on 2019/9/22.
 */

@Module
public class HttpModule {

    @Provides
    OkHttpClient.Builder provideOkHttpClient() {
        // 指定缓存路径,缓存大小100Mb
        Cache cache = new Cache(new File(WNApplication.getContext().getCacheDir(), "HttpCache"),
                1024 * 1024 * 100);
        return new OkHttpClient().newBuilder().cache(cache)
                .retryOnConnectionFailure(true)
                .addInterceptor(RetrofitConfig.sLoggingInterceptor)
                .addInterceptor(RetrofitConfig.sRewriteCacheControlInterceptor)
                .addNetworkInterceptor(RetrofitConfig.sRewriteCacheControlInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS);
    }


    @Provides
    WeNewsApi provideWeNewsApi(OkHttpClient.Builder builder) {

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build());

        return WeNewsApi.getInstance(retrofitBuilder
                .baseUrl(ApiConstants.mWeNewsApi)
                .build().create(NewsNetWork.class));
    }

}
