package com.nuaa.cgn.wenews.ui.news;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.flyco.tablayout.SlidingTabLayout;
import com.nuaa.cgn.wenews.R;
import com.nuaa.cgn.wenews.component.ApplicationComponent;
import com.nuaa.cgn.wenews.component.DaggerHttpComponent;
import com.nuaa.cgn.wenews.model.entity.Channel;
import com.nuaa.cgn.wenews.ui.base.BaseFragment;
import com.nuaa.cgn.wenews.ui.news.adapter.NewsAdapter;
import com.nuaa.cgn.wenews.ui.news.contract.NewsContract;
import com.nuaa.cgn.wenews.ui.news.presenter.NewsPresenter;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public class NewsFragment extends BaseFragment<NewsPresenter> implements NewsContract.View {

    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.iv_edit)
    ImageView mIvEdit;
    @BindView(R.id.SlidingTabLayout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.fake_status_bar)
    View fakeStatusBar;
    Unbinder unbinder;

    private NewsAdapter mNewsPagerAdapter;

    private List<Channel> mSelectedDatas;
    private List<Channel> mUnSelectedDatas;

    private int selectedIndex;
    private String selectedChannel;

    public static NewsFragment newInstance() {
        Bundle args = new Bundle();
        NewsFragment fragment = new NewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentlayout() {
        return R.layout.fragment_news;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {
        DaggerHttpComponent.builder()
                .applicationComponent(appComponent)
                .build()
                .inject(this);
    }

//    @Subscriber
//    private void updateChannel(NewChannelEvent event) {
//        if (event == null) return;
//        if (event.selectedDatas != null && event.unSelectedDatas != null) {
//            mSelectedDatas = event.selectedDatas;
//            mUnSelectedDatas = event.unSelectedDatas;
//            mNewsPagerAdapter.updateChannel(mSelectedDatas);
//            mTabLayout.notifyDataSetChanged();
//            ChannelDao.saveChannels(event.allChannels);
//
//            List<String> integers = new ArrayList<>();
//            for (Channel channel : mSelectedDatas) {
//                integers.add(channel.getChannelName());
//            }
//            if (TextUtils.isEmpty(event.firstChannelName)) {
//                if (!integers.contains(selectedChannel)) {
//                    selectedChannel = mSelectedDatas.get(selectedIndex).getChannelName();
//                    mViewpager.setCurrentItem(selectedIndex, false);
//                } else {
//                    setViewpagerPosition(integers, selectedChannel);
//                }
//            } else {
//                setViewpagerPosition(integers, event.firstChannelName);
//            }
//        }
//    }
//
//
//    @Subscriber
//    private void selectChannel(SelectChannelEvent selectChannelEvent) {
//        if (selectChannelEvent == null) return;
//        List<String> integers = new ArrayList<>();
//        for (Channel channel : mSelectedDatas) {
//            integers.add(channel.getChannelName());
//        }
//        setViewpagerPosition(integers, selectChannelEvent.channelName);
//    }

//
//    /**
//     * 设置 当前选中页
//     *
//     * @param integers
//     * @param channelName
//     */
//    private void setViewpagerPosition(List<String> integers, String channelName) {
//        if (TextUtils.isEmpty(channelName) || integers == null) return;
//        for (int j = 0; j < integers.size(); j++) {
//            if (integers.get(j).equals(channelName)) {
//                selectedChannel = integers.get(j);
//                selectedIndex = j;
//                break;
//            }
//        }
//        mViewpager.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mViewpager.setCurrentItem(selectedIndex, false);
//            }
//        }, 100);
//    }




    @Override
    public void bindView(View view, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedIndex = position;
                selectedChannel = mSelectedDatas.get(position).getChannelName();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @Override
    public void bindData() {
        setStatusBarHeight(getStatusBarHeight());
        mSelectedDatas = new ArrayList<>();
        mUnSelectedDatas = new ArrayList<>();
        mPresenter.getChannel();
    }

    /**
     * 设置状态栏高度
     *
     * @param statusBarHeight
     */
    public void setStatusBarHeight(int statusBarHeight) {
        ViewGroup.LayoutParams params = fakeStatusBar.getLayoutParams();
        params.height = statusBarHeight;
        fakeStatusBar.setLayoutParams(params);
    }

    @Override
    public void onRetry() {

    }

    @Override
    public void loadData(List<Channel> channels, List<Channel> unSelectedDatas) {
        if (channels != null) {
            mSelectedDatas.clear();
            mSelectedDatas.addAll(channels);
            mUnSelectedDatas.clear();
            mUnSelectedDatas.addAll(unSelectedDatas);
            mNewsPagerAdapter = new NewsAdapter(getChildFragmentManager(), channels);
            mViewpager.setAdapter(mNewsPagerAdapter);
            mTabLayout.setViewPager(mViewpager);
        } else {
            T("数据异常");
        }
    }


    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }
}
