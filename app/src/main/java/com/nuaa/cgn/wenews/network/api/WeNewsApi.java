package com.nuaa.cgn.wenews.network.api;

import com.nuaa.cgn.wenews.model.entity.NewsDetail;
import com.nuaa.cgn.wenews.model.response.CommentResponse;
import com.nuaa.cgn.wenews.model.response.NewsResponse;
import com.nuaa.cgn.wenews.model.response.ResultResponse;
import com.nuaa.cgn.wenews.network.NewsNetWork;

import io.reactivex.Observable;

/**
 * Created by cuiguonan on 2019/9/24.
 */

public class WeNewsApi {

    public  static WeNewsApi mInstance;

    public NewsNetWork mNewsNetWork;

    public  WeNewsApi (NewsNetWork mweNetWork){
        mNewsNetWork = mweNetWork;
    }


    public static  WeNewsApi getInstance(NewsNetWork mNewsNetWork){
        if (mInstance == null)
            mInstance = new WeNewsApi(mNewsNetWork);
        return mInstance;
    }


    /**
     * 获取新闻列表
     * @param category
     * @param lastTime
     * @param currentTime
     * @return
     */
    public Observable<NewsResponse> getNewsList(String category, long lastTime, long currentTime) {
        return mNewsNetWork.getNewList(category, lastTime, currentTime);
    }


    /**
     * 新闻详情
     * @param url
     * @return
     */
    public Observable<ResultResponse<NewsDetail>> getNewsDetail(String url){
        return mNewsNetWork.getNewsDetail(url);
    }

    /**
     * 获取新闻评论
     * @param groupId
     * @param itemId
     * @param pageNow
     * @return
     */
    public Observable<CommentResponse> getNewsComment(String groupId, String itemId, String offset,String pageNow){
        return mNewsNetWork.getCommentList(groupId, itemId, offset, pageNow);
    }




}
