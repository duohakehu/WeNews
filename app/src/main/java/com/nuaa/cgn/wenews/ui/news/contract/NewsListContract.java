package com.nuaa.cgn.wenews.ui.news.contract;

import com.nuaa.cgn.wenews.model.entity.News;
import com.nuaa.cgn.wenews.ui.base.BaseCotract;

import java.util.List;

/**
 * Created by cuiguonan on 2019/9/23.
 */

public interface NewsListContract {

    interface View extends BaseCotract.BaseView {

        /**
         * 加载新闻数据
         *
         * @param newsList
         */
        void loadData(List<News> newsList);

        /**
         * 加载更多新闻数据
         *
         * @param newsList
         */
        void loadMoreData(List<News> newsList);

    }

    interface Presenter extends BaseCotract.BasePresenter<View> {

        /**
         * 获取新闻详细信息
         *
         * @param channelCode
         * @param action
         */
        void getData(String channelCode, String action);
    }

}