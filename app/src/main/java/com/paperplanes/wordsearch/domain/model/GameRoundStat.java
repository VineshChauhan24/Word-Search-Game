package com.paperplanes.wordsearch.domain.model;

/**
 * Created by abdularis on 24/07/17.
 */

public class GameRoundStat {

    private String mName;
    private int mDuration;
    private int mUsedWordCount;
    private int mGridRowCount;
    private int mGridColCount;

    public GameRoundStat() {
        this("", 0, 0, 0, 0);
    }

    public GameRoundStat(String name, int duration, int usedWordCount,
                         int gridRowCount, int gridColCount) {
        mName = name;
        mDuration = duration;
        mUsedWordCount = usedWordCount;
        mGridRowCount = gridRowCount;
        mGridColCount = gridColCount;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public int getUsedWordCount() {
        return mUsedWordCount;
    }

    public void setUsedWordCount(int usedWordCount) {
        mUsedWordCount = usedWordCount;
    }

    public int getGridRowCount() {
        return mGridRowCount;
    }

    public void setGridRowCount(int gridRowCount) {
        mGridRowCount = gridRowCount;
    }

    public int getGridColCount() {
        return mGridColCount;
    }

    public void setGridColCount(int gridColCount) {
        mGridColCount = gridColCount;
    }
}
