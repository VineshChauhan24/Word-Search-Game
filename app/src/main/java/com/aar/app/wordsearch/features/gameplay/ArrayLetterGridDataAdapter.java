package com.aar.app.wordsearch.features.gameplay;

import com.aar.app.wordsearch.custom.LetterGridDataAdapter;

/**
 * Created by abdularis on 09/07/17.
 */

public class ArrayLetterGridDataAdapter extends LetterGridDataAdapter {

    private char mGrid[][];

    ArrayLetterGridDataAdapter(char grid[][]) {
        mGrid = grid;
    }

    public char[][] getGrid() {
        return mGrid;
    }

    public void setGrid(char[][] grid) {
        if (grid != null && grid != mGrid) {
            mGrid = grid;
            setChanged();
            notifyObservers();
        }
    }

    @Override
    public int getColCount() {
        return mGrid[0].length;
    }

    @Override
    public int getRowCount() {
        return mGrid.length;
    }

    @Override
    public char getLetter(int row, int col) {
        return mGrid[row][col];
    }
}
