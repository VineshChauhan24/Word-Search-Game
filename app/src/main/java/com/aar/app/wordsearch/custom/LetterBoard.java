package com.aar.app.wordsearch.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.aar.app.wordsearch.R;
import com.aar.app.wordsearch.commons.Direction;
import com.aar.app.wordsearch.commons.GridIndex;
import com.aar.app.wordsearch.commons.Util;
import com.aar.app.wordsearch.custom.layout.CenterLayout;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by abdularis on 26/06/17.
 *
 * Compound view untuk wordsearch game
 * yang memiliki tiga layer yaitu
 * - GridLine sebagai background
 * - StreakView sebagai middleground jadi akan dirender diatas background
 *      dan dibawah foreground
 * - LetterGrid sebagai foreground yang menampilkan letters (huruf-huruf)
 *      yang akan dirender paling atas
 */

public class LetterBoard extends CenterLayout implements Observer {

    private GridLine mGridLineBg;
    private StreakView mStreakView;
    private LetterGrid mLetterGrid;
    private LetterGridDataAdapter mData;
    private boolean mInitialized = false;

    private OnLetterSelectionListener mSelectionListener;

    public LetterBoard(Context context) {
        super(context);
        init(context, null);
    }

    public LetterBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GridLine getGridLineBackground() {
        return mGridLineBg;
    }

    public StreakView getStreakView() {
        return mStreakView;
    }

    public LetterGrid getLetterGrid() {
        return mLetterGrid;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == mData) {
            /*
                ketika data grid berubah maka update row dan column count
                dari grid line agar memiliki dimensi yang sama
               */
            mGridLineBg.setColCount(mData.getColCount());
            mGridLineBg.setRowCount(mData.getRowCount());

            /*
                ketika dimensi row dan column dari data grid berubah
                maka harus di layout/dikalkulasikan kembali ukuran dari streak view
               */
            mStreakView.invalidate();
            mStreakView.requestLayout();
        }
    }

    public void scale(float scaleX, float scaleY) {
        if (mInitialized) {
            mGridLineBg.setGridWidth((int) (mGridLineBg.getGridWidth() * scaleX));
            mGridLineBg.setGridHeight((int) (mGridLineBg.getGridHeight() * scaleY));
            mGridLineBg.setLineWidth((int) (mGridLineBg.getLineWidth() * scaleX));

            mLetterGrid.setGridWidth((int) (mLetterGrid.getGridWidth() * scaleX));
            mLetterGrid.setGridHeight((int) (mLetterGrid.getGridHeight() * scaleY));
            mLetterGrid.setLetterSize(mLetterGrid.getLetterSize() * scaleY);

            mStreakView.setStreakWidth((int) (mStreakView.getStreakWidth() * scaleY));

            // remove all views and re attach them, so this layout get re measure
            removeAllViews();
            attachAllViews();
            mStreakView.invalidateStreakLine();
        }
    }

    public int getGridColCount() {
        return mData.getColCount();
    }

    public int getGridRowCount() {
        return mData.getRowCount();
    }

    public LetterGridDataAdapter getDataAdapter() {
        return mData;
    }

    public OnLetterSelectionListener getSelectionListener() {
        return mSelectionListener;
    }

    public void addStreakLines(List<StreakView.StreakLine> streakLines) {
        mStreakView.addStreakLines(streakLines, false);
    }

    public void addStreakLine(StreakView.StreakLine streakLine) {
        if (streakLine != null)
            mStreakView.addStreakLine(streakLine, true);
    }

    public void popStreakLine() {
        mStreakView.popStreakLine();
    }

    public void removeAllStreakLine() {
        mStreakView.removeAllStreakLine();
    }

    public void setGridWidth(int width) {
        mGridLineBg.setGridWidth(width);
        mLetterGrid.setGridWidth(width);
    }

    public void setGridHeight(int height) {
        mGridLineBg.setGridHeight(height);
        mLetterGrid.setGridHeight(height);
    }

    public void setGridLineVisibility(boolean visible) {
        if (!visible)
            mGridLineBg.setVisibility(INVISIBLE);
        else
            mGridLineBg.setVisibility(VISIBLE);
    }

    public void setGridLineColor(int color) {
        mGridLineBg.setLineColor(color);
    }

    public void setGridLineWidth(int width) {
        mGridLineBg.setLineWidth(width);
    }

    public void setLetterSize(float size) {
        mLetterGrid.setLetterSize(size);
    }

    public void setLetterColor(int color) {
        mLetterGrid.setLetterColor(color);
    }

    public void setStreakWidth(int width) {
        mStreakView.setStreakWidth(width);
    }

    public void setDataAdapter(LetterGridDataAdapter dataAdapter) {
        if (dataAdapter == null) {
            throw new IllegalArgumentException("Data Adapter can't be null");
        }
        else if (dataAdapter != mData) {
            if (mData != null) mData.deleteObserver(this);

            mData = dataAdapter;
            mData.addObserver(this);

            mLetterGrid.setDataAdapter(mData);

            mGridLineBg.setColCount(mData.getColCount());
            mGridLineBg.setRowCount(mData.getRowCount());
        }
    }

    public void setOnLetterSelectionListener(OnLetterSelectionListener listener) {
        mSelectionListener = listener;
    }

    private void init(Context context, AttributeSet attrs) {
        mGridLineBg = new GridLine(context);
        mStreakView = new StreakView(context);
        mLetterGrid = new LetterGrid(context);

        int gridWidth = 50;
        int gridHeight = 50;
        int gridColCount = 8;
        int gridRowCount = 8;

        int lineColor = Color.GRAY;
        int lineWidth = 2;

        float letterSize = 32.0f;
        int letterColor = Color.GRAY;
        int streakWidth = 35;
        int snapToGrid = 0;
        boolean gridLineVisibility = true;

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LetterBoard, 0, 0);

            gridWidth = a.getDimensionPixelSize(R.styleable.LetterBoard_gridWidth, gridWidth);
            gridHeight = a.getDimensionPixelSize(R.styleable.LetterBoard_gridHeight, gridHeight);
            gridColCount = a.getInteger(R.styleable.LetterBoard_gridColumnCount, gridColCount);
            gridRowCount = a.getInteger(R.styleable.LetterBoard_gridRowCount, gridRowCount);

            lineColor = a.getColor(R.styleable.LetterBoard_lineColor, lineColor);
            lineWidth = a.getDimensionPixelSize(R.styleable.LetterBoard_lineWidth, lineWidth);

            letterSize = a.getDimension(R.styleable.LetterBoard_letterSize, letterSize);
            letterColor = a.getColor(R.styleable.LetterBoard_letterColor, letterColor);

            streakWidth = a.getDimensionPixelSize(R.styleable.LetterBoard_streakWidth, streakWidth);
            snapToGrid = a.getInteger(R.styleable.LetterBoard_snapToGrid, snapToGrid);

            gridLineVisibility = a.getBoolean(R.styleable.LetterBoard_gridLineVisibility, gridLineVisibility);

            setGridWidth(gridWidth);
            setGridHeight(gridHeight);
            setGridLineColor(lineColor);
            setGridLineWidth(lineWidth);
            setLetterSize(letterSize);
            setLetterColor(letterColor);
            setStreakWidth(streakWidth);
            setGridLineVisibility(gridLineVisibility);

            a.recycle();
        }

        setDataAdapter(new SampleLetterGridDataAdapter(gridRowCount, gridColCount));

        mGridLineBg.setColCount(getGridColCount());
        mGridLineBg.setRowCount(getGridRowCount());

        mStreakView.setGrid(mGridLineBg);
        mStreakView.setInteractive(true);
        mStreakView.setRememberStreakLine(true);
        mStreakView.setSnapToGrid(StreakView.SnapType.fromId(snapToGrid));
        mStreakView.setOnInteractionListener(new StreakViewInteraction());

        attachAllViews();

        mInitialized = true;

        setScaleX(getScaleX());
        setScaleY(getScaleY());
    }

    private void attachAllViews() {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(mGridLineBg, layoutParams);
        addView(mStreakView, layoutParams);
        addView(mLetterGrid, layoutParams);
    }


    private class StreakViewInteraction implements StreakView.OnInteractionListener {

        private String getStringInRange(GridIndex start, GridIndex end) {
            Direction dir = Direction.fromLine(start, end);
            if (dir == Direction.NONE) return "";

            int count = Util.getIndexLength(start, end);
            char buff[] = new char[count];
            for (int i = 0; i < count; i++) {
                buff[i] = mData.getLetter(start.row + (dir.yOff * i), start.col + (dir.xOff * i));
            }

            return String.valueOf(buff);
        }

        @Override
        public void onTouchBegin(StreakView.StreakLine streakLine) {
            if (mSelectionListener != null) {
                GridIndex idx = streakLine.getStartIndex();
                String str = String.valueOf(mData.getLetter(idx.row, idx.col));
                mSelectionListener.onSelectionBegin(streakLine, str);
            }
        }

        @Override
        public void onTouchDrag(StreakView.StreakLine streakLine) {
            if (mSelectionListener != null)
                mSelectionListener.onSelectionDrag(
                        streakLine,
                        getStringInRange(streakLine.getStartIndex(), streakLine.getEndIndex()));
        }

        @Override
        public void onTouchEnd(StreakView.StreakLine streakLine) {
            if (mSelectionListener != null) {
                String str = getStringInRange(streakLine.getStartIndex(), streakLine.getEndIndex());

                mSelectionListener.onSelectionEnd(streakLine, str);
            }
        }
    }


    public interface OnLetterSelectionListener {
        void onSelectionBegin(StreakView.StreakLine streakLine, String str);
        void onSelectionDrag(StreakView.StreakLine streakLine, String str);
        void onSelectionEnd(StreakView.StreakLine streakLine, String str);
    }
}
