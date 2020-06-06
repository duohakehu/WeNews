package com.nuaa.cgn.wenews.module;

import android.content.Context;

import com.nuaa.cgn.wenews.WNApplication;

import dagger.Module;
import dagger.Provides;

/**
 * Created by cuiguonan on 2019/9/11.
 */

@Module
public class ApplicationModule {

    private Context mContext;

    public ApplicationModule(Context context){
        this.mContext = context;
    }

    @Provides
    WNApplication provideApplication(){

        return (WNApplication) mContext.getApplicationContext();
    }

    @Provides
    Context provideContext(){
        return mContext;
    }
}
