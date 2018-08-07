package com.aar.app.wordsearch.custom.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.aar.app.wordsearch.R;


/**
 * Created by abdularis on 24/06/17.
 *
 * FlowLayout meletakan content secara horizontal dan wrap secara vertical
 * ketika tidak ada lagi space horizontal
 */

public class FlowLayout extends ViewGroup {

    private int mXSpacing;
    private int mYSpacing;

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);

        // final width and height
        int measuredWidth = 0;
        int measuredHeight = 0;

        // current maximum width and height in iteration
        int currMaxWidth = 0;
        int currMaxHeight = 0;

        int availWidth = widthSpec - getPaddingLeft() - getPaddingRight() - mXSpacing - mXSpacing;
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            int cWidth = child.getMeasuredWidth() + mXSpacing;
            int cHeight = child.getMeasuredHeight() + mYSpacing;

            currMaxWidth += cWidth;
            if (currMaxWidth > availWidth) {
                measuredWidth = Math.max(
                        measuredWidth,
                        Math.max(currMaxWidth - cWidth, cWidth)
                );

                measuredHeight += currMaxHeight;

                currMaxWidth = cWidth;
                currMaxHeight = 0;
            }

            if (i == count - 1) {
                measuredHeight += Math.max(currMaxHeight, cHeight);
            }

            currMaxHeight = Math.max(currMaxHeight, cHeight);
        }

//        measuredWidth += mXSpacing;
        measuredHeight += mYSpacing;

        measuredWidth += getPaddingLeft() + getPaddingRight();
        measuredHeight += getPaddingTop() + getPaddingBottom();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY)
            measuredWidth = widthSpec;
        else if (widthMeasureSpec == MeasureSpec.AT_MOST)
            measuredWidth = Math.min(measuredWidth, widthSpec);

        if (heightMode == MeasureSpec.EXACTLY)
            measuredHeight = heightSpec;
        else if (heightMode == MeasureSpec.AT_MOST)
            measuredHeight = Math.min(measuredHeight, heightSpec);

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = getPaddingLeft() + mXSpacing;
        int childTop = getPaddingTop() + mYSpacing;
        int childRight = getWidth() - getPaddingRight() - mXSpacing;
        int childBottom = getHeight() - getPaddingBottom() - mYSpacing;

        int childWidth = childRight - childLeft;
        int childHeight = childBottom - childTop;

        int currLeft = childLeft;
        int currTop = childTop;
        int currMaxHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            if (currLeft + child.getMeasuredWidth() > childWidth) {
                currLeft = childLeft;
                currTop += currMaxHeight + mYSpacing;
                currMaxHeight = 0;

                if (currTop > childHeight) break;
            }

            child.layout(
                    currLeft,
                    currTop,
                    Math.min(currLeft + child.getMeasuredWidth(), childRight),
                    Math.min(currTop + child.getMeasuredHeight(), childBottom)
            );

            currLeft += child.getMeasuredWidth() + mXSpacing;

            currMaxHeight = Math.max(currMaxHeight, child.getMeasuredHeight());
        }
    }

    private void init(Context context, AttributeSet attrs) {
        mXSpacing = 5;
        mYSpacing = 5;

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout, 0, 0);

            mXSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_horizontalSpacing, mXSpacing);
            mYSpacing = a.getDimensionPixelSize(R.styleable.FlowLayout_verticalSpacing, mYSpacing);

            a.recycle();
        }
    }
}
