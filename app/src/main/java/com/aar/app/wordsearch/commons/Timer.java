package com.aar.app.wordsearch.commons;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

/**
 * Created by abdularis on 14/04/17.
 */

public class Timer {

    private List<OnTimeoutListener> mTimeoutListeners;
    private List<OnStoppedListener> mStoppedListeners;
    private List<OnStartedListener> mStartedListeners;

    private boolean mStarted;
    private long mStartTime, mEllapsedTime;
    private long mInterval;

    private java.util.Timer mTimer;
    private Handler mHandler;
    private Runnable mRunnable;

    public Timer(long interval) {
        mTimeoutListeners = new ArrayList<>();
        mStoppedListeners = new ArrayList<>();
        mStartedListeners = new ArrayList<>();

        mStarted = false;
        mStartTime = 0L;
        mEllapsedTime = 0L;
        mInterval = interval > 0 ? interval : 1000;
        mHandler = new Handler();
        mTimer = null;

        mRunnable = new Runnable() {
            @Override
            public void run() {
                mEllapsedTime = SystemClock.uptimeMillis() - mStartTime;
                callTimeoutListeners(mEllapsedTime);
            }
        };
    }

    public void start() {
        if (mStarted) return;

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                mHandler.post(mRunnable);
            }
        };

        mStarted = true;
        mStartTime = SystemClock.uptimeMillis();
        mTimer = new java.util.Timer();
        mTimer.schedule(task, mInterval, mInterval);
        callStartedListener();
    }

    public void stop() {
        if (!mStarted) return;
        mTimer.cancel();
        mTimer = null;
        mStarted = false;
        mEllapsedTime = SystemClock.uptimeMillis() - mStartTime;
        callStoppedListeners(mEllapsedTime);
        Log.v("MyTimer", "stop called");
    }

    public boolean isStarted() {
        return mStarted;
    }

    public void addOnTimeoutListener(OnTimeoutListener listener) {
        mTimeoutListeners.add(listener);
    }

    public void addOnStoppedListener(OnStoppedListener listener) {
        mStoppedListeners.add(listener);
    }

    public void addOnStartedListener(OnStartedListener listener) {
        mStartedListeners.add(listener);
    }

    private void callTimeoutListeners(long ellapsedTime) {
        for (OnTimeoutListener listener : mTimeoutListeners) listener.onTimeout(ellapsedTime);
    }

    private void callStoppedListeners(long ellapsedTime) {
        for (OnStoppedListener listener : mStoppedListeners) listener.onStopped(ellapsedTime);
    }

    private void callStartedListener() {
        for (OnStartedListener listener : mStartedListeners) listener.onStarted();
    }

    public interface OnTimeoutListener {
        void onTimeout(long ellapsedTime);
    }

    public interface OnStoppedListener {
        void onStopped(long ellapsedTime);
    }

    public interface OnStartedListener {
        void onStarted();
    }

}
