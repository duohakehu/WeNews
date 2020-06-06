package com.nuaa.cgn.wenews.ui.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nuaa.cgn.wenews.R;
import com.nuaa.cgn.wenews.WNApplication;
import com.nuaa.cgn.wenews.ui.inter.IBase;
import com.nuaa.cgn.wenews.utils.DialogHelper;
import com.nuaa.cgn.wenews.utils.ToastUtils;
import com.nuaa.cgn.wenews.widget.MultiStateView;
import com.nuaa.cgn.wenews.widget.SimpleMultiStateView;
import com.trello.rxlifecycle2.LifecycleTransformer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by cuiguonan on 2019/9/17.
 */

public abstract class BaseFragment <T extends BaseCotract.BasePresenter> extends SupportFragment implements IBase,BaseCotract.BaseView{

    protected Context mContext;
    protected View mRootView;
    protected Dialog mLoadingDialog;
    Unbinder unbinder;

    @Inject
    public T mPresenter;

    //@BindView(R.id.SimpleMultiStateView)
    SimpleMultiStateView mSimpleMultiStateView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(mRootView!=null){
            ViewGroup parent = (ViewGroup) mRootView.getParent();

            if(parent!=null){
                parent.removeView(mRootView);
            }
        }else {
            mRootView = createView(inflater,container,savedInstanceState);
        }

        mContext = mRootView.getContext();
        mLoadingDialog = DialogHelper.getLoadingDialog(getActivity());

        return mRootView;
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getContentlayout(),container, false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initInjector(WNApplication.getInstance().getApplicationComponent());
        attachView();
        bindView(view, savedInstanceState);
        initStateView();
        //bindData();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        bindData();
    }

    @Nullable
    @Override
    public View getView() {
        return mRootView;
    }

    private void attachView(){
        if(mPresenter !=null){
            mPresenter.attachView(this);
        }
    }


    @Override
    public void onRetry() {

    }

    protected void showLoadingDialog() {
        if (mLoadingDialog != null)
            mLoadingDialog.show();
    }

    protected void showLoadingDialog(String str) {
        if (mLoadingDialog != null) {
            TextView tv = (TextView) mLoadingDialog.findViewById(R.id.tv_load_dialog);
            tv.setText(str);
            mLoadingDialog.show();
        }
    }

    protected void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing())
            mLoadingDialog.dismiss();
    }

    private void initStateView() {
        if (mSimpleMultiStateView == null) return;
        mSimpleMultiStateView.setEmptyResource(R.layout.view_empty)
                .setRetryResource(R.layout.view_retry)
                .setLoadingResource(R.layout.view_loading)
                .setNoNetResource(R.layout.view_nonet)
                .build()
                .setonReLoadlistener(new MultiStateView.onReLoadlistener() {
                    @Override
                    public void onReload() {
                        onRetry();
                    }
                });
    }

    @Override
    public void showLoading() {
        if (mSimpleMultiStateView != null) {
            mSimpleMultiStateView.showLoadingView();
        }
    }

    @Override
    public void showSuccess() {
        hideLoadingDialog();
//        if (mSimpleMultiStateView != null) {
//            mSimpleMultiStateView.showContent();
//        }
    }

    @Override
    public void showFaild() {
//        if (mSimpleMultiStateView != null) {
//            mSimpleMultiStateView.showErrorView();
//        }
    }

    @Override
    public void showNotNet() {

//        if (mSimpleMultiStateView != null) {
//            mSimpleMultiStateView.showNoNetView();
//        }
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    protected void T(String string) {
        ToastUtils.showShort(WNApplication.getContext(), string);
    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.<T>bindToLifecycle();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
