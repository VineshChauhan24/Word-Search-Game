package com.aar.app.wordsearch.data.entity;

import com.aar.app.wordsearch.model.GameData;
import com.aar.app.wordsearch.model.UsedWord;

import java.util.List;

/**
 * Created by abdularis on 08/07/17.
 */

public class GameDataEntity {

    private GameData.Info mInfo;
    private int mGridRowCount;
    private int mGridColCount;
    private String mGridData;
    private List<UsedWord> mUsedWords;

    public GameDataEntity() {
        mGridRowCount = 0;
        mGridColCount = 0;
        mGridData = null;
        mUsedWords = null;
    }

    public GameData.Info getInfo() {
        return mInfo;
    }

    public void setInfo(GameData.Info info) {
        mInfo = info;
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

    public String getGridData() {
        return mGridData;
    }

    public void setGridData(String gridData) {
        mGridData = gridData;
    }

    public List<UsedWord> getUsedWords() {
        return mUsedWords;
    }

    public void setUsedWords(List<UsedWord> usedWords) {
        mUsedWords = usedWords;
    }
}
