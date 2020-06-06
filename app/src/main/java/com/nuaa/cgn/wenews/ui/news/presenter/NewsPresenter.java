package com.nuaa.cgn.wenews.ui.news.presenter;

import com.nuaa.cgn.wenews.R;
import com.nuaa.cgn.wenews.WNApplication;
import com.nuaa.cgn.wenews.database.ChannelDao;
import com.nuaa.cgn.wenews.model.entity.Channel;
import com.nuaa.cgn.wenews.ui.base.BasePresenter;
import com.nuaa.cgn.wenews.ui.news.contract.NewsContract;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by cuiguonan on 2019/9/19.
 */

public class NewsPresenter extends BasePresenter<NewsContract.View> implements NewsContract.Presenter {


    @Inject
    public NewsPresenter(){}


    @Override
    public void getChannel() {
        List<Channel> channelList;
        List<Channel> myChannels = new ArrayList<>();
        List<Channel> otherChannels = new ArrayList<>();
        channelList = ChannelDao.getChannels();
        if (channelList.size() < 1) {
            List<String> channelName = Arrays.asList(WNApplication.getContext().getResources()
                    .getStringArray(R.array.weixun_channel));
            List<String> channelCode = Arrays.asList(WNApplication.getContext().getResources()
                    .getStringArray(R.array.weixun_channel_code));
            List<Channel> channels = new ArrayList<>();

            for (int i = 0; i < channelName.size(); i++) {
                Channel channel = new Channel();
                channel.setChannelCode(channelCode.get(i));
                channel.setChannelName(channelName.get(i));
                channel.setChannelType(i < 1 ? 1 : 0);
                channel.setChannelSelect(i < channelCode.size() - 3);
                if (i < channelCode.size() - 3) {
                    myChannels.add(channel);
                } else {
                    otherChannels.add(channel);
                }
                channels.add(channel);
            }

            DataSupport.saveAllAsync(channels).listen(new SaveCallback() {
                @Override
                public void onFinish(boolean success) {
                }
            });

            channelList = new ArrayList<>();
            channelList.addAll(channels);
        } else {
            channelList = ChannelDao.getChannels();
            Iterator<Channel> iterator = channelList.iterator();
            while (iterator.hasNext()) {
                Channel channel = iterator.next();
                if (!channel.isChannelSelect()) {
                    otherChannels.add(channel);
                    iterator.remove();
                }
            }
            myChannels.addAll(channelList);
        }
        mView.loadData(myChannels, otherChannels);

    }
}
