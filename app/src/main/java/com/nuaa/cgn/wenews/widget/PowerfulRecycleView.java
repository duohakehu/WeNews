package com.nuaa.cgn.wenews.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import com.nuaa.cgn.wenews.R;
import com.nuaa.cgn.wenews.utils.DisplayUtils;

/**
 * Created by cuiguonan on 2019/9/23.
 */

public class PowerfulRecycleView extends RecyclerView {

    private Context mContext;

    private int mMarginLeft;
    private int mMarginRight;


    //分割线颜色
    private int mDivideColor;
    //分割线大小
    private int mDividerSize;
    //分割线Drawable
    private Drawable mDivideDrawable;
    //是否使用瀑布型布局
    private boolean mStaggerLayout;
    //列数
    private int mColums;
    //布局方向
    private int mOrientation = LinearLayoutManager.VERTICAL;

    private LayoutManager mLayoutManager;

    private DividerDecoration mDividerDecoration;

    private Drawable mItemDrawale;





    public PowerfulRecycleView(Context context) {
        super(context, null);
    }

    public PowerfulRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public PowerfulRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PowerfulRecyclerView);//加载自定义属性

        //初始化自定义属性
        mDivideColor = ta.getColor(R.styleable.PowerfulRecyclerView_dividerColor, Color.parseColor("#ffd8d8d8"));
        mDividerSize = ta.getDimensionPixelSize(R.styleable.PowerfulRecyclerView_dividerSize, DisplayUtils.dip2px(context, 1));
        mDivideDrawable = ta.getDrawable(R.styleable.PowerfulRecyclerView_dividerDrawable);
        mStaggerLayout = ta.getBoolean(R.styleable.PowerfulRecyclerView_useStaggerLayout, false);
        mColums = ta.getInteger(R.styleable.PowerfulRecyclerView_numColumns, 1);
        mOrientation = ta.getInt(R.styleable.PowerfulRecyclerView_rvOrientation, mOrientation);

        mMarginLeft = ta.getDimensionPixelSize(R.styleable.PowerfulRecyclerView_dividerMarginLeft, 0 );
        mMarginRight = ta.getDimensionPixelSize(R.styleable.PowerfulRecyclerView_dividerMarginRight, 0);

        ta.recycle();
        setLayoutManger();
        setDivider();



    }

    void setLayoutManger(){
        if(mStaggerLayout){
            if(mOrientation == LinearLayoutManager.VERTICAL){

                mLayoutManager = new GridLayoutManager(mContext,  mColums);

            }else {

                mLayoutManager = new GridLayoutManager(mContext, mColums, mOrientation, false);
            }

        }else {

            //瀑布流布局
            mLayoutManager = new StaggeredGridLayoutManager(mColums,mOrientation);
        }

        setLayoutManager(mLayoutManager);
    }

    /**
     * 设置分割线
     */
    private void setDivider() {
        if (mItemDrawale == null){
            //绘制颜色分割线
            mDividerDecoration = new DividerDecoration(mContext, mOrientation,mItemDrawale, mDividerSize,mMarginLeft,mMarginRight);
        }else{
            //绘制图片分割线
            mDividerDecoration = new DividerDecoration(mContext,mOrientation,mItemDrawale,mDividerSize,mMarginLeft,mMarginRight);
        }
        this.addItemDecoration(mDividerDecoration);
    }


}
