package com.nuaa.cgn.wenews.component;

import android.content.Context;

import com.nuaa.cgn.wenews.WNApplication;
import com.nuaa.cgn.wenews.module.ApplicationModule;
import com.nuaa.cgn.wenews.module.HttpModule;
import com.nuaa.cgn.wenews.network.api.WeNewsApi;

import dagger.Component;

/**
 * Created by cuiguonan on 2019/9/11.
 */

@Component(modules = {ApplicationModule.class,HttpModule.class})
public interface ApplicationComponent {

    WNApplication getApplication();

    Context getContext();

    WeNewsApi getWeNewsApi();





}
