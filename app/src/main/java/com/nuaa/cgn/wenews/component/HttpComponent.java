package com.nuaa.cgn.wenews.component;

import com.nuaa.cgn.wenews.ui.news.NewsFragment;
import com.nuaa.cgn.wenews.ui.news.NewsListFragment;

import dagger.Component;

/**
 * Created by cuiguonan on 2019/9/22.
 */
@Component(dependencies = ApplicationComponent.class)
public interface HttpComponent {

    void inject(NewsFragment newsFragment);

    void inject(NewsListFragment newsListFragment);
}
