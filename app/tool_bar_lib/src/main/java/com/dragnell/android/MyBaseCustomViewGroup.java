package com.dragnell.android;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;

public class MyBaseCustomViewGroup extends ViewGroup {
    public MyBaseCustomViewGroup(Context context) {
        super(context);
        init(context, null);
    }

    public MyBaseCustomViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MyBaseCustomViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public MyBaseCustomViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);

        if(viewGroup != null) {
            viewGroup.measure(widthMeasureSpec, heightMeasureSpec);
            int child_right = viewGroup.getMeasuredWidth();
            int child_bottom = viewGroup.getMeasuredHeight();
            int maxW = Math.max(viewGroup.getMinimumWidth(), child_right);
            int maxH = Math.max(viewGroup.getMinimumHeight(), child_bottom);
            setMeasuredDimension( resolveSizeAndState(maxW - getPaddingEnd() - getPaddingStart(), widthMeasureSpec, viewGroup.getMeasuredState())
                    , resolveSizeAndState(maxH - getPaddingTop() - getPaddingBottom(), heightMeasureSpec, viewGroup.getMeasuredState()));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(changed) {
            ViewGroup viewGroup = (ViewGroup) getChildAt(0);
            if (viewGroup != null) {
                int w = viewGroup.getMeasuredWidth();
                int h = viewGroup.getMeasuredHeight();
                viewGroup.layout(getPaddingStart(), getPaddingTop(), w - getPaddingEnd(), h - getPaddingBottom());
            }
        }
    }

    protected void init(Context context, AttributeSet attrs)
    {
    }
}
