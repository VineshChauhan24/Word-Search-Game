package com.aar.app.wordsearch.model;

public class GameDataInfo {

    private int mId;
    private String mName;
    private int mDuration;
    private int mGridRowCount;
    private int mGridColCount;
    private int mUsedWordsCount;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
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

    public int getUsedWordsCount() {
        return mUsedWordsCount;
    }

    public void setUsedWordsCount(int usedWordsCount) {
        mUsedWordsCount = usedWordsCount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof GameDataInfo)) return false;
        GameDataInfo o2 = (GameDataInfo) obj;
        return mId == o2.mId &&
                mName.equals(o2.mName) &&
                mDuration == o2.mDuration &&
                mGridColCount == o2.mGridColCount &&
                mGridRowCount == o2.mGridRowCount &&
                mUsedWordsCount == o2.mUsedWordsCount;
    }
}
