package com.aar.app.wordsearch.custom;

import java.util.Observable;

/**
 * Created by abdularis on 26/06/17.
 */

public abstract class LetterGridDataAdapter extends Observable {

    public abstract int getColCount();
    public abstract int getRowCount();
    public abstract char getLetter(int row, int col);

}
