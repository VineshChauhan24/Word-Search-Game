package com.aar.app.wordsearch.model;

import com.aar.app.wordsearch.commons.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdularis on 08/07/17.
 */

public class GameData {

    private int mId;
    private String mName;
    private int mDuration;
    private Grid mGrid;
    private List<UsedWord> mUsedWords;

    public GameData() {
        this(0, "", 0, null, new ArrayList<>());
    }

    public GameData(int id, String name, int duration, Grid grid, List<UsedWord> usedWords) {
        mId = id;
        mName = name;
        mDuration = duration;
        mGrid = grid;
        mUsedWords = usedWords;
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

    public Grid getGrid() {
        return mGrid;
    }

    public void setGrid(Grid grid) {
        mGrid = grid;
    }

    public List<UsedWord> getUsedWords() {
        return mUsedWords;
    }

    public UsedWord markWordAsAnswered(String word, UsedWord.AnswerLine answerLine, boolean enableReverse) {
        String answerStrRev = Util.getReverseString(word);
        for (UsedWord usedWord : mUsedWords) {

            if (usedWord.isAnswered()) continue;

            String currUsedWord = usedWord.getString();
            if (currUsedWord.equalsIgnoreCase(word) ||
                    (currUsedWord.equalsIgnoreCase( answerStrRev ) && enableReverse)) {

                usedWord.setAnswered(true);
                usedWord.setAnswerLine(answerLine);
                return usedWord;
            }
        }
        return null;
    }

    public int getAnsweredWordsCount() {
        int count = 0;
        for (UsedWord uw : mUsedWords) {
            if (uw.isAnswered()) count++;
        }
        return count;
    }

    public boolean isFinished() {
        return getAnsweredWordsCount() == mUsedWords.size();
    }

    public void addUsedWord(UsedWord usedWord) {
        mUsedWords.add(usedWord);
    }

    public void addUsedWords(List<UsedWord> usedWords) {
        mUsedWords.addAll(usedWords);
    }
}
