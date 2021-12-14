package co.africanwolf.hiddenwords.data.entity;

import co.africanwolf.hiddenwords.model.UsedWord;

import java.util.List;

/**
 * Created by abdularis on 08/07/17.
 */

public class GameDataEntity {

    private int mId;
    private String mName;
    private long mDuration;
    private int mGridRowCount;
    private int mGridColCount;
    private String mGridData;
    private List<UsedWord> mUsedWords;

    public GameDataEntity() {
        mId = 0;
        mName = "";
        mDuration = 0;
        mGridRowCount = 0;
        mGridColCount = 0;
        mGridData = null;
        mUsedWords = null;
    }

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

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long duration) {
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
