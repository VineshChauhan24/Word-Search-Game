package com.aar.app.wordsearch.custom;

import android.support.annotation.NonNull;
import android.view.MotionEvent;

/**
 * Created by abdularis on 15/06/17.
 *
 * Digunakan untuk memproses data dari MouseEvent agar lebih mudah digunakan,
 * memiliki movement threshold untuk mengubah sensitivitas perpindahan sentuhan
 */

class TouchProcessor {

    private boolean mIsDown;

    private float mMoveThreshold;
    private float mLastX;
    private float mLastY;

    private OnTouchProcessed mListener;

    TouchProcessor(@NonNull OnTouchProcessed listener, float moveThreshold) {
        mListener = listener;
        mMoveThreshold = Math.max(moveThreshold, 0.1f);
    }

    /*
        call this function to process touch event
     */
    boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                mLastY = event.getY();
                mIsDown = true;
                mListener.onDown(event);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsDown = false;
                mListener.onUp(event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsDown &&
                        (Math.abs(mLastX - event.getX()) > mMoveThreshold || Math.abs(mLastY - event.getY()) > mMoveThreshold)) {
                    mLastX = event.getX();
                    mLastY = event.getY();
                    mListener.onMove(event);
                }
                break;
            default:
                return false;
        }

        return true;
    }

    interface OnTouchProcessed {
        void onDown(MotionEvent event);
        void onUp(MotionEvent event);
        void onMove(MotionEvent event);
    }
}
