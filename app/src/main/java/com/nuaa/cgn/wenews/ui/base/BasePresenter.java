package com.nuaa.cgn.wenews.ui.base;

/**
 * Created by cuiguonan on 2019/9/11.
 */

public class BasePresenter<T extends BaseCotract.BaseView> implements BaseCotract.BasePresenter<T>{


    protected T mView;

    @Override
    public void attachView(T view) {

        this.mView = view;

    }

    @Override
    public void detachView() {
        if(mView!=null){
            mView =null;
        }

    }
}
