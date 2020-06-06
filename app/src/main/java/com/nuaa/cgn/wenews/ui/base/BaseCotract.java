package com.nuaa.cgn.wenews.ui.base;

import com.trello.rxlifecycle2.LifecycleTransformer;

import java.io.IOException;

/**
 * Created by cuiguonan on 2019/9/11.
 */

public interface BaseCotract {


    interface BasePresenter<T extends BaseCotract.BaseView>{


        void attachView(T view);

        void  detachView();
    }


    interface  BaseView{

        //显示进度
        void showLoading() throws IOException;

        //显示请求成功
        void showSuccess();

        //失败重试
        void showFaild();

        //显示网路不可用
        void showNotNet();

        //重试
        void onRetry();

        /**
         *  绑定生命周期
         * @param <T>
         * @return
         */
       <T> LifecycleTransformer<T> bindToLife();


    }
}
