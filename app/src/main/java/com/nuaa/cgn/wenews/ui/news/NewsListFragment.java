package com.nuaa.cgn.wenews.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.nuaa.cgn.wenews.R;
import com.nuaa.cgn.wenews.component.ApplicationComponent;
import com.nuaa.cgn.wenews.component.DaggerHttpComponent;
import com.nuaa.cgn.wenews.model.entity.News;
import com.nuaa.cgn.wenews.ui.base.BaseFragment;
import com.nuaa.cgn.wenews.ui.news.adapter.NewsListAdapter;
import com.nuaa.cgn.wenews.ui.news.contract.NewsListContract;
import com.nuaa.cgn.wenews.ui.news.presenter.NewsListPresenter;
import com.nuaa.cgn.wenews.widget.CustomLoadMoreView;
import com.nuaa.cgn.wenews.widget.PowerfulRecycleView;
import com.nuaa.cgn.wenews.widget.PtrWeiXunHeader;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by cuiguonan on 2019/9/24.
 */

public class NewsListFragment extends BaseFragment<NewsListPresenter> implements NewsListContract.View{
    /**
     * 测试本地分支
     */
    @BindView(R.id.mPtrFrameLayout)
    PtrFrameLayout mPtrFrameLayout;
    @BindView(R.id.mRecyclerView)
    PowerfulRecycleView mPowerfulRecycleView;
    @BindView(R.id.tv_toast)
    TextView mTvToast;
    @BindView(R.id.rl_top_toast)
    RelativeLayout mRlTopToast;

    private String channelCode;
    private  List<News> beanList;
    private NewsListAdapter detailAdapter;
    private  int upPullNum = 1;
    private  int downPullNum = 1;
    private  boolean isRemoveHeaderView = false;
    private PtrWeiXunHeader mHeader;
    private  PtrFrameLayout mFrame;


    public static NewsListFragment newInstance(String channelCode) {
        Bundle args = new Bundle();
        args.putString("channelCode", channelCode);
        NewsListFragment fragment = new NewsListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void loadData(List<News> newlist) {
        if (newlist == null || newlist.size() == 0) {
            if (mHeader != null && mFrame != null) {
                mHeader.refreshComplete(false, mFrame);
            }
            showFaild();
            mPtrFrameLayout.refreshComplete();
        } else {
            downPullNum++;
            if (isRemoveHeaderView) {
                detailAdapter.removeAllHeaderView();
            }
            beanList.addAll(newlist);
            detailAdapter.setNewData(newlist);
            showToast(newlist.size(), true);
            mPtrFrameLayout.refreshComplete();
            if (mHeader != null && mFrame != null) {
                mHeader.refreshComplete(true, mFrame);
            }
            showSuccess();
            KLog.e("loadData: " + newlist.toString());
        }

    }

    @Override
    public void loadMoreData(List<News> newsList) {

        if (newsList == null || newsList.size() == 0) {
            if (mHeader != null && mFrame != null) {
                mHeader.refreshComplete(false, mFrame);
            }
            showFaild();
            mPtrFrameLayout.refreshComplete();
        } else {
            downPullNum++;
            if (isRemoveHeaderView) {
                detailAdapter.removeAllHeaderView();
            }
            beanList.addAll(newsList);
            detailAdapter.setNewData(newsList);
            showToast(newsList.size(), true);
            mPtrFrameLayout.refreshComplete();
            if (mHeader != null && mFrame != null) {
                mHeader.refreshComplete(true, mFrame);
            }
            showSuccess();
            KLog.e("loadData: " + newsList.toString());
        }

    }

    @Override
    public int getContentlayout() {
        return R.layout.fragment_list;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {

        DaggerHttpComponent.builder()
                .applicationComponent(appComponent)
                .build()
                .inject(this);

    }

    @Override
    public void bindView(View view, Bundle saveInstanceState) {

        if(getArguments() == null) return;
        channelCode = getArguments().getString("channelCode");
        mPtrFrameLayout.disableWhenHorizontalMove(true);
        mHeader = new PtrWeiXunHeader(mContext);
        mPtrFrameLayout.setHeaderView(mHeader);
        mPtrFrameLayout.addPtrUIHandler(mHeader);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return  PtrDefaultHandler.checkContentCanBePulledDown(frame, mPowerfulRecycleView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

                mFrame = frame;
                isRemoveHeaderView = true;
                mPresenter.getData(channelCode, NewsListPresenter.ACTION_DOWN);

            }
        });

        beanList = new ArrayList<>();
        mPowerfulRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPowerfulRecycleView.setAdapter(detailAdapter = new NewsListAdapter(beanList, channelCode, getActivity()));
        detailAdapter.setEnableLoadMore(true);
        detailAdapter.setLoadMoreView(new CustomLoadMoreView());
        detailAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        detailAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                KLog.e("onLoadMoreRequested: " + upPullNum);
                mPresenter.getData(channelCode, NewsListPresenter.ACTION_UP);
            }
        }, mPowerfulRecycleView);

    }


    @Override
    public void bindData() {
        mPresenter.getData(channelCode, NewsListPresenter.ACTION_DEFAULT);
    }

    @Override
    public void onRetry() {
        bindData();
    }


    private void showToast(int num, boolean isRefresh) {
        if (isRefresh) {
            mTvToast.setText(String.format(getResources().getString(R.string.news_toast), num + ""));
        } else {
            mTvToast.setText("将为你减少此类内容");
        }
        mRlTopToast.setVisibility(View.VISIBLE);
        ViewAnimator.animate(mRlTopToast)
                .newsPaper()
                .duration(1000)
                .start()
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        ViewAnimator.animate(mRlTopToast)
                                .bounceOut()
                                .duration(1000)
                                .start();
                    }
                });
    }
}
