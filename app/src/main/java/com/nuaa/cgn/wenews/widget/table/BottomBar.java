package com.nuaa.cgn.wenews.widget.table;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

/**
 * Created by cuiguonan on 2019/9/17.
 */

public class BottomBar extends LinearLayout {


    private LinearLayout mTabLayout;
    private LayoutParams mTabparams;

    private boolean mVisible = true;

    private static final int TRANSLATE_DURATION_MILLIS = 200;

    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private int mCurrentPosition = 0;//当前坐标

    private OnTabSelectedListener mTabselectedListener;


    public BottomBar(Context context) {
        this(context, null);
    }

    public BottomBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }



    /**
     * 动态初始化BottomBar属性
     * @param context
     */
    private void init(Context context, AttributeSet attr){
        setOrientation(VERTICAL);
        mTabLayout = new LinearLayout(context);
        mTabLayout.setBackgroundColor(Color.WHITE);
        mTabLayout.setOrientation(HORIZONTAL);
        addView(mTabLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mTabparams = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        mTabparams.weight = 1;
    }



    public BottomBar addItem(final BottomTab tab){

       tab.setOnClickListener(new OnClickListener() {
           @Override
           public void onClick(View v) {
               if(mTabselectedListener == null){
                   return;
               }
               int pos = tab.getTabPosition();

               if(mCurrentPosition == pos){
                   mTabselectedListener.onTabReselected(pos);
               }else {
                   mTabselectedListener.onTabSelected(pos,mCurrentPosition);
                   tab.setSelected(true);
                   mTabselectedListener.onTabUnselected(mCurrentPosition);
                   mTabLayout.getChildAt(mCurrentPosition).setSelected(false);
                   mCurrentPosition = pos;

               }
           }
       });
       tab.setTabPosition(mTabLayout.getChildCount());//设置position坐标
       tab.setLayoutParams(mTabparams);
       mTabLayout.addView(tab);
       return this;

    }

    public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener) {
        mTabselectedListener = onTabSelectedListener;
    }

    /**
     * activity销毁时(手机翻转)保存tab状态
     * @return
     */
    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        return new SavedSate(superState,mCurrentPosition);
    }


    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedSate savedSate = (SavedSate) state;
        super.onRestoreInstanceState(state);
        if(mCurrentPosition != savedSate.position){
            mTabLayout.getChildAt(mCurrentPosition).setSelected(false);
            mTabLayout.getChildAt(savedSate.position).setSelected(true);
        }

        mCurrentPosition = savedSate.position;
    }

    static class SavedSate extends BaseSavedState{

        private int position;

        public SavedSate(Parcel source) {
            super(source);
            position = source.readInt();
        }

        public SavedSate(Parcelable source, int position){
            super(source);
            this.position = position;
        }


        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(position);
        }


        public static final Creator<SavedSate> CREATOR = new Creator<SavedSate>() {
            public SavedSate createFromParcel(Parcel in) {
                return new SavedSate(in);
            }

            public SavedSate[] newArray(int size) {
                return new SavedSate[size];
            }
        };
    }

    public void hide(boolean anim) {
        toggle(false, anim, false);
    }

    public void show(boolean anim) {
        toggle(true, anim, false);
    }


    private void toggle(final boolean visible, final boolean animate, boolean force) {
        if (mVisible != visible || force) {
            mVisible = visible;
            int height = getHeight();
            if (height == 0 && !force) {
                ViewTreeObserver vto = getViewTreeObserver();
                if (vto.isAlive()) {
                    // view树完成测量并且分配空间而绘制过程还没有开始的时候播放动画。
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            ViewTreeObserver currentVto = getViewTreeObserver();
                            if (currentVto.isAlive()) {
                                currentVto.removeOnPreDrawListener(this);
                            }
                            toggle(visible, animate, true);
                            return true;
                        }
                    });
                    return;
                }
            }
            int translationY = visible ? 0 : height;
            if (animate) {
                animate().setInterpolator(mInterpolator)
                        .setDuration(TRANSLATE_DURATION_MILLIS)
                        .translationY(translationY);
            } else {
                ViewCompat.setTranslationY(this, translationY);
            }
        }
    }


    public interface OnTabSelectedListener{

        void onTabSelected(int position, int prePosition);

        void onTabUnselected(int position);

        void onTabReselected(int position);
    }





}
