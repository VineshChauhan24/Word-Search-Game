package com.aar.app.wordsearch.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.aar.app.wordsearch.R;
import com.aar.app.wordsearch.commons.GridIndex;
import com.aar.app.wordsearch.commons.math.Vec2;

import java.util.List;
import java.util.Stack;

/**
 * Created by abdularis on 20/06/17.
 *
 * Garis yang bisa didrag (coretan didalam word search game)
 */

public class StreakView extends View {

    public enum SnapType {
        NONE(0), START_END(1), ALWAYS_SNAP(2);
        int id;

        SnapType(int id) {
            this.id = id;
        }

        public static SnapType fromId(int id) {
            for (SnapType t : values()) {
                if (t.id == id) return t;
            }

            throw new IllegalArgumentException();
        }
    }

    private RectF mRect;
    private int mWidth;
    private Paint mPaint;
    private int mGridId;
    private GridBehavior mGrid;
    private SnapType mSnapToGrid;
    private TouchProcessor mTouchProcessor;
    private boolean mInteractive;
    private boolean mRememberStreakLine;
    private Stack<StreakLine> mLines;
    private OnInteractionListener mInteractionListener;
    private boolean mEnableOverrideStreakLineColor;
    private int mOverrideStreakLineColor;

    public static StreakLine createStreakLine(GridIndex start, GridIndex end, int color) {
        StreakLine streakLine = new StreakLine();
        streakLine.setColor(color);
        streakLine.startIdx = start;
        streakLine.endIdx = end;

        return streakLine;
    }

    public StreakView(Context context) {
        super(context);
        init(context, null);
    }

    public StreakView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public int getStreakWidth() {
        return mWidth;
    }

    public GridBehavior getGrid() {
        return mGrid;
    }

    public SnapType isSnapToGrid() {
        return mSnapToGrid;
    }

    public boolean isInteractive() {
        return mInteractive;
    }

    public boolean isRememberStreakLine() {
        return mRememberStreakLine;
    }

    public void setEnableOverrideStreakLineColor(boolean enableOverrideStreakLineColor) {
        mEnableOverrideStreakLineColor = enableOverrideStreakLineColor;
    }

    public void setOverrideStreakLineColor(int overrideStreakLineColor) {
        mOverrideStreakLineColor = overrideStreakLineColor;
    }

    public void setStreakWidth(int width) {
        mWidth = width;
        invalidate();
    }

    public void setGrid(GridBehavior grid) {
        mGrid = grid;
    }

    public void setOnInteractionListener(OnInteractionListener listener) {
        mInteractionListener = listener;
    }

    public void setSnapToGrid(SnapType snapToGrid) {
        if (mSnapToGrid != snapToGrid && mGridId == -1 && mGrid == null)
            throw new IllegalStateException("setGrid() first to set the grid object!");
        mSnapToGrid = snapToGrid;
    }

    public void setInteractive(boolean interactive) {
        mInteractive = interactive;
    }

    public void setRememberStreakLine(boolean rememberStreakLine) {
        mRememberStreakLine = rememberStreakLine;
    }

    private void pushStreakLine(StreakLine streakLine, boolean snapToGrid) {
        mLines.push(streakLine);
        if (mGrid != null) {
            streakLine.start.x = mGrid.getCenterColFromIndex(streakLine.startIdx.col);
            streakLine.start.y = mGrid.getCenterRowFromIndex(streakLine.startIdx.row);

            streakLine.end.x = mGrid.getCenterColFromIndex(streakLine.endIdx.col);
            streakLine.end.y = mGrid.getCenterRowFromIndex(streakLine.endIdx.row);
        }
    }

    public void invalidateStreakLine() {
        for (StreakLine streakLine : mLines) {
            streakLine.start.x = mGrid.getCenterColFromIndex(streakLine.startIdx.col);
            streakLine.start.y = mGrid.getCenterRowFromIndex(streakLine.startIdx.row);

            streakLine.end.x = mGrid.getCenterColFromIndex(streakLine.endIdx.col);
            streakLine.end.y = mGrid.getCenterRowFromIndex(streakLine.endIdx.row);
        }
    }

    public void addStreakLines(List<StreakLine> streakLines, boolean snapToGrid) {
        for (StreakLine line : streakLines)
            pushStreakLine(line, snapToGrid);
        invalidate();
    }

    public void addStreakLine(StreakLine streakLine, boolean snapToGrid) {
        pushStreakLine(streakLine, snapToGrid);
        invalidate();
    }

    public void popStreakLine() {
        if (!mLines.isEmpty()) {
            mLines.pop();
            invalidate();
        }
    }

    public void removeAllStreakLine() {
        mLines.clear();
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mInteractive)
            return mTouchProcessor.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mGridId != -1 && mSnapToGrid != SnapType.NONE) {
            mGrid = (GridBehavior) getRootView().findViewById(mGridId);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (mSnapToGrid != SnapType.NONE && mGrid != null) {
            measuredWidth = mGrid.getRequiredWidth();
            measuredHeight = mGrid.getRequiredHeight();
        }

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (StreakLine line : mLines) {
            Vec2 v = Vec2.sub(line.end, line.start);

            float length = v.length();
            double rot = Math.toDegrees(getRotation(v, Vec2.Right));
            if (v.y < 0)
                rot = -rot;

            canvas.save();
            if (!Double.isNaN(rot))
                canvas.rotate((float) rot, line.start.x, line.start.y);

            int halfWidth = mWidth / 2;

            if (mEnableOverrideStreakLineColor) {
                mPaint.setColor(mOverrideStreakLineColor);
            } else {
                mPaint.setColor(line.color);
            }

            mRect.set(line.start.x - halfWidth, line.start.y - halfWidth,
                    line.start.x + length + halfWidth, line.start.y + halfWidth);

            canvas.drawRoundRect(mRect, halfWidth, halfWidth, mPaint);

            canvas.restore();
        }
    }

    private float getRotation(Vec2 p1, Vec2 p2) {
        float dot = Vec2.normalize(p1).dot(Vec2.normalize(p2));
        return (float) Math.acos(dot);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GREEN);
        mWidth = 26;
        mGridId = -1;
        mGrid = null;
        mSnapToGrid = SnapType.NONE;
        mRememberStreakLine = false;
        mInteractive = false;

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StreakView, 0, 0);

            mPaint.setColor(a.getInteger(R.styleable.StreakView_streakColor, mPaint.getColor()));
            mWidth = a.getDimensionPixelSize(R.styleable.StreakView_streakWidth, mWidth);
            mGridId = a.getResourceId(R.styleable.StreakView_strekGrid, mGridId);
            mSnapToGrid = SnapType.fromId(a.getInt(R.styleable.StreakView_snapToGrid, 0));
            mInteractive = a.getBoolean(R.styleable.StreakView_interactive, mInteractive);
            mRememberStreakLine = a.getBoolean(R.styleable.StreakView_rememberStreakLine, mRememberStreakLine);

            a.recycle();
        }

        setSnapToGrid(mSnapToGrid);
        mTouchProcessor = new TouchProcessor(new OnTouchProcessedListener(), 3.0f);
        mLines = new Stack<>();
        mRect = new RectF();
    }


    private class OnTouchProcessedListener implements TouchProcessor.OnTouchProcessed {

        @Override
        public void onDown(MotionEvent event) {
            if (!mRememberStreakLine) {
                if (mLines.isEmpty())
                    mLines.push(new StreakLine());
            }
            else {
                mLines.push(new StreakLine());
            }

            StreakLine line = mLines.peek();

            int colIdx = mGrid.getColIndex((int) event.getX());
            int rowIdx = mGrid.getRowIndex((int) event.getY());
            line.startIdx.set(rowIdx, colIdx);

            if (mSnapToGrid != SnapType.NONE && mGrid != null) {
                line.start.set(mGrid.getCenterColFromIndex(colIdx), mGrid.getCenterRowFromIndex(rowIdx));
                line.end.set(line.start.x, line.start.y);
            }
            else {
                line.start.set(event.getX(), event.getY());
                line.end.set(event.getX(), event.getY());
            }

            if (mInteractionListener != null)
                mInteractionListener.onTouchBegin(line);
            invalidate();
        }

        @Override
        public void onUp(MotionEvent event) {
            if (mLines.isEmpty())
                return;

            StreakLine line = mLines.peek();

            int colIdx = mGrid.getColIndex((int) event.getX());
            int rowIdx = mGrid.getRowIndex((int) event.getY());
            line.endIdx.set(rowIdx, colIdx);

            if (mSnapToGrid != SnapType.NONE && mGrid != null)
                line.end.set(mGrid.getCenterColFromIndex(colIdx), mGrid.getCenterRowFromIndex(rowIdx));
            else
                line.end.set(event.getX(), event.getY());

            if (mInteractionListener != null)
                mInteractionListener.onTouchEnd(line);
            invalidate();
        }

        @Override
        public void onMove(MotionEvent event) {
            if (mLines.isEmpty())
                return;

            StreakLine line = mLines.peek();

            int colIdx = mGrid.getColIndex((int) event.getX());
            int rowIdx = mGrid.getRowIndex((int) event.getY());
            line.endIdx.set(rowIdx, colIdx);

            if (mSnapToGrid == SnapType.ALWAYS_SNAP && mGrid != null) {
                line.end.set(mGrid.getCenterColFromIndex(colIdx), mGrid.getCenterRowFromIndex(rowIdx));
            }
            else {
                int halfWidth = mWidth / 2;
                float x = Math.max(Math.min(event.getX(), getWidth() - halfWidth), halfWidth);
                float y = Math.max(Math.min(event.getY(), getHeight() - halfWidth), halfWidth);
                line.end.set(x, y);
            }

            if (mInteractionListener != null)
                mInteractionListener.onTouchDrag(line);
            invalidate();
        }
    }



    //
    public interface OnInteractionListener {
        void onTouchBegin(StreakLine streakLine);
        void onTouchDrag(StreakLine streakLine);
        void onTouchEnd(StreakLine streakLine);
    }


    public static class StreakLine {
        Vec2 start;
        Vec2 end;
        GridIndex startIdx;
        GridIndex endIdx;
        int color = Color.RED;

        public StreakLine() {
            start = new Vec2();
            end = new Vec2();
            startIdx = new GridIndex(-1, -1);
            endIdx = new GridIndex(-1, -1);
        }

        public Vec2 getStart() {
            return start;
        }

        public Vec2 getEnd() {
            return end;
        }

        public GridIndex getStartIndex() {
            return startIdx;
        }

        public GridIndex getEndIndex() {
            return endIdx;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }
}
