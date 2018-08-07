package com.aar.app.wordsearch.custom.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by abdularis on 22/06/17.
 *
 * Center layout merupakan layout untuk membuat semua child view berada ditengah-tengah
 * relative terhadap posisi CenterLayout object
 */

public class CenterLayout extends ViewGroup {

    public CenterLayout(Context context) {
        super(context);
    }

    public CenterLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = 0;
        int measuredHeight = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            measuredWidth = Math.max(measuredWidth, child.getMeasuredWidth());
            measuredHeight = Math.max(measuredHeight, child.getMeasuredHeight());
        }

        measuredWidth += getPaddingLeft() + getPaddingRight();
        measuredHeight += getPaddingTop() + getPaddingBottom();

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY)
            measuredWidth = width;
        else if (widthMeasureSpec == MeasureSpec.AT_MOST)
            measuredWidth = Math.min(measuredWidth, width);

        if (heightMode == MeasureSpec.EXACTLY)
            measuredHeight = height;
        else if (heightMode == MeasureSpec.AT_MOST)
            measuredHeight = Math.min(measuredHeight, height);

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();

        int childLeft = getPaddingLeft();
        int childTop = getPaddingTop();
        int childRight = getWidth() - getPaddingRight();
        int childBottom = getHeight() - getPaddingBottom();

        int xOff;
        int yOff;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            xOff = Math.max(0, width - child.getMeasuredWidth()) / 2;
            yOff = Math.max(0, height - child.getMeasuredHeight()) / 2;

            child.layout(childLeft + xOff, childTop + yOff, childRight - xOff, childBottom - yOff);
        }
    }
}
