package com.aar.app.wordsearch.model;

/**
 * Created by abdularis on 08/07/17.
 */

public class Word {

    private int mId;
    private String mString;

    public Word() {
        this(-1, "");
    }

    public Word(int id, String string) {
        mId = id;
        mString = string;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getString() {
        return mString;
    }

    public void setString(String string) {
        mString = string;
    }
}
