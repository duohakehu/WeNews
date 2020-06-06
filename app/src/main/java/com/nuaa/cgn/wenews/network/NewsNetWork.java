package com.nuaa.cgn.wenews.network;

import com.nuaa.cgn.wenews.model.entity.NewsDetail;
import com.nuaa.cgn.wenews.model.response.CommentResponse;
import com.nuaa.cgn.wenews.model.response.NewsResponse;
import com.nuaa.cgn.wenews.model.response.ResultResponse;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by cuiguonan on 2019/9/24.
 */

public interface NewsNetWork {

    String GET_ARTICLE_LIST = "api/news/feed/v62/?refer=1&count=20&loc_mode=4&device_id=34960436458&iid=13136511752";
    String GET_COMMENT_LIST = "article/v2/tab_comments/";


    @GET(GET_ARTICLE_LIST)
    Observable<NewsResponse> getNewList(@Query("category") String category,
                                        @Query("min_behot_time") long lastTime,
                                        @Query("last_refresh_sub_entrance_interval") long currentTime);


    /**
     * 获取新闻详情数据
     */
    @GET
    Observable<ResultResponse<NewsDetail>> getNewsDetail(@Url String url);

    /**
     * 获取评论列表数据
     *
     * @param groupId
     * @param itemId
     * @param offset
     * @param count
     * @return
     */
    @GET(GET_COMMENT_LIST)
    Observable<CommentResponse> getCommentList(@Query("group_id") String groupId,
                                               @Query("item_id") String itemId,
                                               @Query("offset") String offset,
                                               @Query("count") String count);
}
