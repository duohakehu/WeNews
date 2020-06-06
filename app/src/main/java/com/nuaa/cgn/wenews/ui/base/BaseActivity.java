package com.nuaa.cgn.wenews.ui.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nuaa.cgn.wenews.R;
import com.nuaa.cgn.wenews.WNApplication;
import com.nuaa.cgn.wenews.ui.inter.IBase;
import com.nuaa.cgn.wenews.utils.DialogHelper;
import com.nuaa.cgn.wenews.utils.StatusBarUtils;
import com.nuaa.cgn.wenews.widget.SimpleMultiStateView;
import com.trello.rxlifecycle2.LifecycleTransformer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper.Delegate;

/**
 * Created by cuiguonan on 2019/9/11.
 */

public abstract class BaseActivity<T extends BaseCotract.BasePresenter>extends SupportActivity implements IBase,Delegate,BaseCotract.BaseView{

    protected View mRootView;
    protected Dialog mLoadingDialog =null;
    Unbinder unbinder;

    /**
     *
     */
   // SimpleMultiStateView mSimpleMultiStateView;

    @Inject
    protected T mPresenter;
    protected BGASwipeBackHelper mBGASwipeBackHelper;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        initSwipeBaseFinish();
        super.onCreate(savedInstanceState);
        mRootView = createView(null,null,savedInstanceState);
        setContentView(mRootView);
        initInjector(WNApplication.getInstance().getApplicationComponent());
        attachView();
        bindView(mRootView,savedInstanceState);
        initStateView();
        bindData();
        mLoadingDialog = DialogHelper.getLoadingDialog(this);




    }

    private void initStateView(){

    }

    @Override
    public View getView() {
        return mRootView;
    }



    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    private void attachView(){

        if(mPresenter !=null){
            mPresenter.attachView(this);
        }
    }


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(getContentlayout(),container);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }


    /**
     * 改变状态栏颜色
     * @param color
     * @param stausBarAlpha
     */
    protected void setStatusBarColor(@ColorInt int color, @IntRange(from = 0, to = 255)int stausBarAlpha){

        StatusBarUtils.setColorForSwipeBack(this,color,stausBarAlpha);
    }

    protected void showLoadingDialog(String str){

        if(mLoadingDialog!=null){

            TextView tv = mLoadingDialog.findViewById(R.id.tv_load_dialog);
            tv.setText(str);
            mLoadingDialog.show();

        }

    }


    protected void hideLoadingDialog(){

        if(mLoadingDialog != null && mLoadingDialog.isShowing()){
            mLoadingDialog.dismiss();
        }
    }


//    protected SimpleMultiStateView getStateView() {
//       return mSimpleMultiStateView;
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if(mPresenter!=null){
            mPresenter.detachView();
        }
    }


    @Override
    public void showLoading() {
//        if(mSimpleMultiStateView!=null){
//            mSimpleMultiStateView.showLoadingView();
//        }
    }


    @Override
    public void showSuccess() {
//        if(mSimpleMultiStateView!=null){
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


    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return this.<T>bindToLifecycle();
    }

    @Override
    public void onSwipeBackLayoutSlide(float v) {

    }

    @Override
    public void onSwipeBackLayoutCancel() {

    }

    @Override
    public void onSwipeBackLayoutExecuted() {
        mBGASwipeBackHelper.swipeBackward();
    }


    /**
     * 初始化滑动返回
     */
    private void initSwipeBaseFinish(){

        mBGASwipeBackHelper = new BGASwipeBackHelper(this,this);
        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackManager.getInstance().init(this) 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。
        // 设置滑动返回是否可用。默认值为 true
        mBGASwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mBGASwipeBackHelper.setIsOnlyTrackingLeftEdge(false);
        // 设置是否是微信滑动返回样式。默认值为 true
        mBGASwipeBackHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mBGASwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mBGASwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mBGASwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mBGASwipeBackHelper.setSwipeBackThreshold(0.3f);

    }










}
