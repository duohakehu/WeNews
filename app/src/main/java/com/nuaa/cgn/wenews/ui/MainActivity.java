package com.nuaa.cgn.wenews.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.nuaa.cgn.wenews.R;
import com.nuaa.cgn.wenews.WNApplication;
import com.nuaa.cgn.wenews.component.ApplicationComponent;
import com.nuaa.cgn.wenews.component.DaggerApplicationComponent;
import com.nuaa.cgn.wenews.component.DaggerHttpComponent;
import com.nuaa.cgn.wenews.ui.base.BaseActivity;
import com.nuaa.cgn.wenews.ui.base.SupportActivity;
import com.nuaa.cgn.wenews.ui.base.SupportFragment;
import com.nuaa.cgn.wenews.ui.news.NewsFragment;
import com.nuaa.cgn.wenews.utils.DialogHelper;
import com.nuaa.cgn.wenews.utils.StatusBarUtils;
import com.nuaa.cgn.wenews.widget.table.BottomBar;
import com.nuaa.cgn.wenews.widget.table.BottomTab;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by cuiguonan on 2019/9/18.
 */

public class MainActivity extends BaseActivity {
    @BindView(R.id.contentContainer)
    FrameLayout mContentContainer;
    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;



    private SupportFragment[] mFragments = new SupportFragment[4];


    public int getContentlayout() {
        return R.layout.activity_main;
    }

    public void initInjector(ApplicationComponent appComponent){}


    public void bindView(View view, Bundle saveInstanceState) {
        StatusBarUtils.setTranslucentForImageViewInFragment(MainActivity.this, 0, null);

        if(saveInstanceState == null){

            mFragments[0] = NewsFragment.newInstance();


            getSupportDelegate().loadMultipleRootFragment(R.id.contentContainer, 0,
                  mFragments[0]);

        }else {

            mFragments[0] = findFragment(NewsFragment.class);

        }

        mBottomBar.addItem(new BottomTab(this, R.drawable.ic_news,"新闻"))
                .addItem(new BottomTab(this, R.drawable.ic_video, "视频"))
                .addItem(new BottomTab(this, R.drawable.ic_jiandan, "妹子"))
                .addItem(new BottomTab(this, R.drawable.ic_my, "我的"));

        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
              //  getSupportDelegate().showHideFragment(mFragments[position], mFragments[prePosition]);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });

    }


    public void bindData() {

    }


    public void onRetry() {

    }
}
