package com.nuaa.cgn.wenews.widget.table;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nuaa.cgn.wenews.R;
import com.nuaa.cgn.wenews.utils.ContextUtils;

/**
 * Created by cuiguonan on 2019/9/18.
 */

public class BottomTab extends LinearLayout {

    private Context mContext;
    private int icon;
    private ImageView mImageView;
    private TextView mTextView;
    private int position;



    public BottomTab(Context context, int icon, String title) {
        this(context, null, icon, title);
    }

    public BottomTab(Context context, @Nullable AttributeSet attrs, int icon, String title) {
        this(context, attrs, 0,icon, title);
    }

    public BottomTab(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int icon, String title) {
        super(context, attrs, defStyleAttr);
        init(context,icon,title);
    }


    private void init(Context context, int icon, String title){

        mContext = context;
        this.icon = icon;

        setOrientation(LinearLayout.VERTICAL);
        int size = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                24,getResources().getDisplayMetrics());//转换为24dp

        mImageView = new ImageView(mContext);
        LayoutParams params = new LayoutParams(size,size);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.topMargin = ContextUtils.dip2px(context, 3.5f);
        mImageView.setImageResource(icon);
        mImageView.setLayoutParams(params);


        mTextView = new TextView(mContext);
        LayoutParams t_params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mTextView.setText(title);
        mTextView.setTextSize(ContextUtils.dip2px(context, 3.5f));
        mTextView.setTextColor(ContextCompat.getColor(mContext, R.color.color_table));
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setLayoutParams(t_params);


        addView(mImageView);
        addView(mTextView);

    }


    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected) {
            mImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.app_color_blue));
            mTextView.setTextColor(ContextCompat.getColor(mContext, R.color.app_color_blue));
        } else {
            mImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.color_table));
            mTextView.setTextColor(ContextCompat.getColor(mContext, R.color.color_table));
        }
    }

    public void setTabPosition(int position) {
        this.position = position;
        if (position == 0) {
            setSelected(true);
        }
    }

    public int getTabPosition() {
        return position;
    }
}
