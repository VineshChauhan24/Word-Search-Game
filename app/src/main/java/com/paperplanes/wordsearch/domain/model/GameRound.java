package com.paperplanes.wordsearch.domain.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdularis on 08/07/17.
 */

public class GameRound {

    private Info mInfo;
    private Grid mGrid;
    private List<UsedWord> mUsedWords;

    public GameRound() {
        this(new Info(), null, new ArrayList<UsedWord>());
    }

    public GameRound(Info info) {
        this(info, null, new ArrayList<UsedWord>());
    }

    public GameRound(Info info, Grid grid, List<UsedWord> usedWords) {
        mInfo = info;
        mGrid = grid;
        mUsedWords = usedWords;
    }

    public Info getInfo() {
        return mInfo;
    }

    public void setInfo(Info info) {
        mInfo = info;
    }

    public Grid getGrid() {
        return mGrid;
    }

    public void setGrid(Grid grid) {
        mGrid = grid;
    }

    public List<UsedWord> getUsedWords() {
        return mUsedWords;
    }

    public int getAnsweredWordsCount() {
        int count = 0;
        for (UsedWord uw : mUsedWords) {
            if (uw.isAnswered()) {
                count++;
            }
        }

        return count;
    }

    public void addUsedWord(UsedWord usedWord) {
        mUsedWords.add(usedWord);
    }

    public void addUsedWords(List<UsedWord> usedWords) {
        mUsedWords.addAll(usedWords);
    }

    public static class Info {
        private int mId;
        private String mName;
        private int mDuration;

        public Info() {
            this(-1, "", 0);
        }

        public Info(int id, String name, int duration) {
            mId = id;
            mName = name;
            mDuration = duration;
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

        public int getDuration() {
            return mDuration;
        }

        public void setDuration(int duration) {
            mDuration = duration;
        }
    }
}
