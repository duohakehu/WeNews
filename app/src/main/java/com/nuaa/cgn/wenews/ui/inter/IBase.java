package com.nuaa.cgn.wenews.ui.inter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nuaa.cgn.wenews.component.ApplicationComponent;

/**
 * Created by cuiguonan on 2019/9/11.
 */

public interface IBase {

    View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    View getView();

    int getContentlayout();

    void initInjector(ApplicationComponent appComponent);

    void bindView(View view, Bundle saveInstanceState);

    void bindData();


}
