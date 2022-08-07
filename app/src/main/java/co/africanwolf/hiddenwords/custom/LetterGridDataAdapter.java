package co.africanwolf.hiddenwords.custom;

import java.util.Observable;

/**
 * Created by abdularis on 26/06/17.
 */

public abstract class LetterGridDataAdapter extends Observable {

    public abstract  void clear();
    public abstract int getColCount();
    public abstract int getRowCount();
    public abstract char getLetter(int row, int col);

}
