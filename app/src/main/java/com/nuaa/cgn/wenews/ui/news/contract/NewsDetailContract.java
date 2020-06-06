package com.nuaa.cgn.wenews.ui.news.contract;

import com.nuaa.cgn.wenews.model.entity.NewsDetail;
import com.nuaa.cgn.wenews.model.response.CommentResponse;
import com.nuaa.cgn.wenews.model.response.ResultResponse;
import com.nuaa.cgn.wenews.ui.base.BaseCotract;

/**
 * Created by cuiguonan on 2019/9/27.
 */

public interface NewsDetailContract {

    interface View extends BaseCotract.BaseView{
        /**
         * 加载新闻数据
         *
         * @param news
         */
        void loadNewsData(ResultResponse<NewsDetail> news);

        /**
         * 加载评论数据
         *
         * @param comment
         */
        void loadConmentData(CommentResponse comment);
    }

    interface Presenter extends BaseCotract.BasePresenter<View>{

        /**
         * 获取新闻详细信息
         *
         * @param url
         */
        void getNewsData(String url);

        /**
         * 获取新闻评论信息
         *
         * @param groupId
         * @param itemId
         * @param pageNow
         */
        void getConmentData(String groupId, String itemId, int pageNow);
    }


}
