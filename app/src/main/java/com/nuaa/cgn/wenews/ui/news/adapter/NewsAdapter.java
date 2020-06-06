package com.nuaa.cgn.wenews.ui.news.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nuaa.cgn.wenews.model.entity.Channel;
import com.nuaa.cgn.wenews.ui.base.BaseFragment;
import com.nuaa.cgn.wenews.ui.news.NewsListFragment;

import java.util.List;

/**
 * Created by cuiguonan on 2019/9/20.
 */

public class NewsAdapter extends FragmentStatePagerAdapter {
    private List<Channel> mChannels;

    public NewsAdapter(FragmentManager fm, List<Channel> channels) {
        super(fm);
        this.mChannels = channels;
    }

    public void updateChannel(List<Channel> channels){
        this.mChannels = channels;
        notifyDataSetChanged();
    }

    @Override
    public BaseFragment getItem(int position) {

        return NewsListFragment.newInstance(mChannels.get(position).getChannelCode());
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mChannels.get(position).getChannelName();
    }

    @Override
    public int getCount() {
        return mChannels != null ? mChannels.size() : 0;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
