package com.nuaa.cgn.wenews.ui.news.presenter;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.nuaa.cgn.wenews.R;
import com.nuaa.cgn.wenews.WNApplication;
import com.nuaa.cgn.wenews.model.entity.News;
import com.nuaa.cgn.wenews.model.entity.NewsData;
import com.nuaa.cgn.wenews.model.response.NewsResponse;
import com.nuaa.cgn.wenews.network.BaseObserver;
import com.nuaa.cgn.wenews.network.RxSchedulers;
import com.nuaa.cgn.wenews.network.api.WeNewsApi;
import com.nuaa.cgn.wenews.ui.base.BasePresenter;
import com.nuaa.cgn.wenews.ui.news.contract.NewsListContract;
import com.nuaa.cgn.wenews.utils.PreUtils;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


/**
 * Created by cuiguonan on 2019/9/23.
 */

public class NewsListPresenter extends BasePresenter<NewsListContract.View> implements NewsListContract.Presenter {

    WeNewsApi mWeNewsApi;
    public static final String ACTION_DEFAULT = "default";
    public static final String ACTION_DOWN = "down";
    public static final String ACTION_UP = "up";

    private long lastime;

    @Inject
    public NewsListPresenter(WeNewsApi mWeNewsApi){ this.mWeNewsApi = mWeNewsApi;}


    @Override
    public void getData(final String channelCode, final String action) {

        lastime = PreUtils.getLong(channelCode, 0);
        if(lastime == 0)
            lastime = System.currentTimeMillis() / 1000;

        mWeNewsApi.getNewsList(channelCode,lastime,System.currentTimeMillis() / 1000)
                .compose(RxSchedulers.<NewsResponse>applySchedulers())
                .compose(mView.<NewsResponse>bindToLife())
                .subscribe(new BaseObserver<NewsResponse>() {
                    @Override
                    public void onSuccess(NewsResponse newsResponse) {

                        lastime = System.currentTimeMillis()/1000;
                        PreUtils.putLong(channelCode, lastime);

                        List<NewsData> data = newsResponse.data;
                        List<News> newsList = new ArrayList<>();

                        if (data != null && data.size() != 0){
                            for (NewsData newsData : data) {
                                News news = new Gson().fromJson(newsData.content, News.class);
                                news.itemType = getNewsType(news);
                                newsList.add(news);
                            }
                        }
                        KLog.e(newsList);

                        if (!action.equals(NewsListPresenter.ACTION_UP)) {
                            mView.loadData(newsList);
                        } else {
                            String[] channelCodes = WNApplication.getContext().getResources().getStringArray(R.array.weixun_channel_code);
                            if (channelCode.equals(channelCodes[0])) {
                                //如果是推荐频道
                                newsList.remove(0);//移除第一个，因为第一个是置顶新闻，重复
                            }
                            mView.loadMoreData(newsList);
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {

                    }
                });

    }

        private int getNewsType(News news) {
            if (news.has_video) {
                //如果有视频
                if (news.video_style == 0) {
                    //右侧视频
                    if (news.middle_image == null || TextUtils.isEmpty(news.middle_image.url)) {
                        return News.TYPE_TEXT_NEWS;
                    }
                    return News.TYPE_RIGHT_PIC_NEWS;
                } else if (news.video_style == 2) {
                    //居中视频
                    return News.TYPE_CENTER_PIC_NEWS;
                }
            } else {
                //非视频新闻
                if (!news.has_image) {
                    //纯文字新闻
                    return News.TYPE_TEXT_NEWS;
                } else {
                    if (news.image_list == null || news.image_list.size() == 0) {
                        //图片列表为空，则是右侧图片
                        return News.TYPE_RIGHT_PIC_NEWS;
                    }

                    if (news.gallary_image_count == 3) {
                        //图片数为3，则为三图
                        return News.TYPE_THREE_PIC_NEWS;
                    }

                    //中间大图，右下角显示图数
                    return News.TYPE_CENTER_PIC_NEWS;
                }
            }
            return News.TYPE_TEXT_NEWS;
        }




}
