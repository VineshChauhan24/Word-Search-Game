package com.aar.app.wordsearch.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.aar.app.wordsearch.R;

/**
 * Created by abdularis on 22/06/17.
 *
 * Base class untuk semua class yang memiliki karakteristik seperti grid
 */

public abstract class GridBehavior extends View {

    private int mGridWidth;
    private int mGridHeight;

    public GridBehavior(Context context) {
        super(context);
        init(context, null);
    }

    public GridBehavior(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public int getGridWidth() {
        return (int) (mGridWidth * getScaleX());
    }

    public int getGridHeight() {
        return (int) (mGridHeight * getScaleY());
    }

    public abstract int getColCount();

    public abstract int getRowCount();

    /**
     * Return lebar minimum yang dibutuhkan, yg didapatkan dari jumlah dan lebar grid
     *
     * @return lebar grid view yang dibutuhkan
     */
    public int getRequiredWidth() {
        return getPaddingLeft() + getPaddingRight() + (getColCount() * getGridWidth());
    }


    /**
     * Return tinggi minimum yang dibutuhkan, yg didapatkan dari jumlah dan tinggi grid
     *
     * @return tinggi grid view yng dibutuhkan
     */
    public int getRequiredHeight() {
        return getPaddingTop() + getPaddingBottom() + (getRowCount() * getGridHeight());
    }

    /**
     * Return column index grid pada posisi layar tertentu
     *
     * @param screenPos posisi pada layar relative terhadap view ini.
     * @return index column, dimana column >= 0 dan column < jumlah horizontal grid - 1
     */
    public int getColIndex(int screenPos) {
        return Math.max( Math.min((screenPos - getPaddingLeft()) / getGridWidth(), getColCount() - 1), 0 );
    }


    /**
     * Return row index grid pada posisi layar tertentu
     *
     * @param screenPos posisi pada layar relative terhadap view ini.
     * @return index row, dimana row >= 0 dan row < jumlah vertical grid - 1
     */
    public int getRowIndex(int screenPos) {
        return Math.max( Math.min((screenPos - getPaddingTop()) / getGridHeight(), getRowCount() - 1), 0 );
    }

    public int getCenterColFromIndex(int cIdx) {
        return (Math.min(Math.max(0, cIdx), getColCount() - 1) * getGridWidth()) +
                (getGridWidth() / 2) + getPaddingLeft();
    }

    public int getCenterRowFromIndex(int rIdx) {
        return (Math.min(Math.max(0, rIdx), getRowCount() - 1) * getGridHeight()) +
                (getGridHeight() / 2) + getPaddingTop();
    }

    public void setGridWidth(int gridWidth) {
        mGridWidth = gridWidth;
        invalidate();
    }

    public void setGridHeight(int gridHeight) {
        mGridHeight = gridHeight;
        invalidate();
    }

    public abstract void setColCount(int colCount);

    public abstract void setRowCount(int rowCount);

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int measuredWidth = getRequiredWidth();
        int measuredHeight = getRequiredHeight();

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

    private void init(Context context, AttributeSet attrs) {
        mGridWidth = 50;
        mGridHeight = 50;

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GridBehavior, 0, 0);

            mGridWidth = a.getDimensionPixelSize(R.styleable.GridBehavior_gridWidth, mGridWidth);
            mGridHeight = a.getDimensionPixelSize(R.styleable.GridBehavior_gridHeight, mGridHeight);

            a.recycle();
        }
    }
}
