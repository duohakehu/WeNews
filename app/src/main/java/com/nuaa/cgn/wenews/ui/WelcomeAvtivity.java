package com.nuaa.cgn.wenews.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nuaa.cgn.wenews.R;
import com.nuaa.cgn.wenews.component.ApplicationComponent;
import com.nuaa.cgn.wenews.ui.base.BaseActivity;
import com.nuaa.cgn.wenews.utils.ImageLoaderUtil;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by cuiguonan on 2019/9/11.
 */

public class WelcomeAvtivity extends BaseActivity{

    @BindView(R.id.iv_img)
    ImageView ivad;

    @BindView(R.id.iv_ad)
    RelativeLayout llBottom;

    @BindView(R.id.iv_skip)
    TextView tvskip;

    @BindView(R.id.iv_pass)
    FrameLayout fl_ad;

    CompositeDisposable mCompositeDisposable = new CompositeDisposable();//管理订阅




    @Override
    public int getContentlayout() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initInjector(ApplicationComponent appComponent) {

    }

    @Override
    public void bindView(View view, Bundle saveInstanceState) {

        ImageLoaderUtil.LoadImage(this, "https://timgsa.baidu.com/timg?image&quality" +
                "=80&size=b9999_10000&sec=1568706538922&di=dd6d722109ffa1af91fe2f003f4d2bf8&imgtype=0&" +
                "src=http%3A%2F%2Fwww.ps-xxw.cn%2Fuploadfile%2F201406%2F4%2F8321462190.jpg", ivad);

        //添加订阅
        mCompositeDisposable.add(countObservable(5).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {

                tvskip.setText("跳过 4");

            }
        }).subscribeWith(new DisposableObserver<Integer>(){

            @Override
            public void onNext(Integer integer) {

                tvskip.setText("跳过 "+(integer-1));

            }
            @Override
            public void onError(Throwable e) {}

            @Override
            public void onComplete() {

                toMain();
            }
        }));


    }

    private void toMain(){

        if(mCompositeDisposable !=null){
            mCompositeDisposable.dispose();//取消订阅
        }
        Intent intent =new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {

        if(mCompositeDisposable !=null){
            mCompositeDisposable.dispose();
        }
        super.onDestroy();
    }

    /**
     * 创建计时器
     * @param time
     * @return
     */
    public Observable<Integer> countObservable(int time){

        if(time < 0 )
            time = 0;
        final int countTime =time;
        return Observable.interval(0,1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(Long aLong) throws Exception {
                        return countTime - aLong.intValue();
                    }
                })
                .take(time);
    }

    @Override
    public void bindData() {

    }

    @Override
    public void onRetry() {

    }

    @OnClick(R.id.iv_pass)
    public void onViewClicked(){
        toMain();
    }
}
