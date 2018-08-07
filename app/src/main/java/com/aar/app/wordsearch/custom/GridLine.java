package com.aar.app.wordsearch.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.aar.app.wordsearch.R;

/**
 * Created by abdularis on 22/06/17.
 *
 * Digunakan untuk merender grid line
 *  ____________
 * |   |   |   |
 * |---|---|---|
 * |___|___|___|
 * |   |   |   |
 */

public class GridLine extends GridBehavior {

    private int mLineWidth;
    private int mColCount;
    private int mRowCount;
    private Paint mPaint;

    public GridLine(Context context) {
        super(context);
        init(context, null);
    }

    public GridLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @Override
    public int getColCount() {
        return mColCount;
    }

    @Override
    public int getRowCount() {
        return mRowCount;
    }

    public int getLineWidth() {
        return mLineWidth;
    }

    public int getLineColor() {
        return mPaint.getColor();
    }

    public void setLineWidth(int lineWidth) {
        mLineWidth = lineWidth;
        invalidate();
    }

    public void setLineColor(int color) {
        mPaint.setColor(color);
        invalidate();
    }

    @Override
    public int getRequiredWidth() {
        return super.getRequiredWidth() + mLineWidth;
    }

    @Override
    public int getRequiredHeight() {
        return super.getRequiredHeight() + mLineWidth;
    }

    @Override
    public int getCenterColFromIndex(int cIdx) {
        return super.getCenterColFromIndex(cIdx) + (mLineWidth / 2);
    }

    @Override
    public int getCenterRowFromIndex(int rIdx) {
        return super.getCenterRowFromIndex(rIdx) + (mLineWidth / 2);
    }

    @Override
    public void setColCount(int colCount) {
        mColCount = colCount;
        invalidate();
        requestLayout();
    }

    @Override
    public void setRowCount(int rowCount) {
        mRowCount = rowCount;
        invalidate();
        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int viewWidth = getRequiredWidth();
        int viewHeight = getRequiredHeight();

        int pLeft = getPaddingLeft();
        int pTop = getPaddingTop();
        int pRight = getPaddingRight();
        int pBottom = getPaddingBottom();

        float y = pTop;
        // horizontal lines
        for (int i = 0; i <= getRowCount(); i++) {
            canvas.drawRect(pLeft, y, viewWidth + mLineWidth - pRight, y + mLineWidth, mPaint);

            y += getGridHeight();
        }

        float x = pLeft;
        // vertical lines
        for (int i = 0; i <= getColCount(); i++) {
            canvas.drawRect(x, pTop, x + mLineWidth, viewHeight + mLineWidth - pBottom, mPaint);

            x += getGridWidth();
        }
    }

    private void init(Context context, AttributeSet attrs) {
        mLineWidth = 2;
        mColCount = 8;
        mRowCount = 8;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GridLine, 0, 0);

            mLineWidth = a.getDimensionPixelSize(R.styleable.GridLine_lineWidth, mLineWidth);
            mPaint.setColor(a.getColor(R.styleable.GridLine_lineColor, Color.GRAY));
            mColCount = a.getInteger(R.styleable.GridLine_gridColumnCount, mColCount);
            mRowCount = a.getInteger(R.styleable.GridLine_gridRowCount, mRowCount);

            a.recycle();
        }
    }
}
