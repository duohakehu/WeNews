package com.nuaa.cgn.wenews.ui.news.presenter;


import com.nuaa.cgn.wenews.model.entity.NewsDetail;
import com.nuaa.cgn.wenews.model.response.CommentResponse;
import com.nuaa.cgn.wenews.model.response.ResultResponse;
import com.nuaa.cgn.wenews.network.BaseObserver;
import com.nuaa.cgn.wenews.network.RxSchedulers;
import com.nuaa.cgn.wenews.network.api.WeNewsApi;
import com.nuaa.cgn.wenews.ui.base.BasePresenter;
import com.nuaa.cgn.wenews.ui.news.contract.NewsDetailContract;

import javax.inject.Inject;



/**
 * Created by cuiguonan on 2019/9/27.
 */

public class NewsDetailPresenter extends BasePresenter<NewsDetailContract.View> implements NewsDetailContract.Presenter{

    WeNewsApi mWeNewsApi;

    @Inject
    public NewsDetailPresenter(WeNewsApi mWeNewsApi){

        this.mWeNewsApi = mWeNewsApi;

    }

    @Override
    public void getNewsData(String url) {

        mWeNewsApi.getNewsDetail(url)
                .compose(RxSchedulers.<ResultResponse<NewsDetail>>applySchedulers())
                .compose(mView.<ResultResponse<NewsDetail>>bindToLife())
                .subscribe(new BaseObserver<ResultResponse<NewsDetail>>() {
                    @Override
                    public void onSuccess(ResultResponse<NewsDetail> newsDetailResultResponse) {
                        mView.loadNewsData(newsDetailResultResponse);
                    }

                    @Override
                    public void onFail(Throwable e) {
                        mView.loadNewsData(null);

                    }
                });

    }

    @Override
    public void getConmentData(String groupId, String itemId,int pageNow) {

        int offset = (pageNow - 1) * 20;
        mWeNewsApi.getNewsComment(groupId, itemId, String.valueOf(offset), String.valueOf(pageNow))
        .compose(RxSchedulers.<CommentResponse>applySchedulers())
        .compose(mView.<CommentResponse>bindToLife())
        .subscribe(new BaseObserver<CommentResponse>() {
            @Override
            public void onSuccess(CommentResponse commentResponse) {
                mView.loadConmentData(commentResponse);
            }

            @Override
            public void onFail(Throwable e) {
                mView.loadConmentData(null);

            }
        });

    }
}
