package com.aar.app.wordsearch.model;

/**
 * Created by abdularis on 08/07/17.
 */

public class Grid {
    public static final char GRID_NEWLINE_SEPARATOR = ',';
    public static final char NULL_CHAR = '\0';

    private char mArray[][];

    public Grid(char grid[][]) {
        mArray = grid;
    }

    public Grid(int rowCount, int colCount) {
        if (rowCount <= 0 || colCount <= 0)
            throw new IllegalArgumentException("Row and column should be greater than 0");
        mArray = new char[rowCount][colCount];
        reset();
    }

    public int getRowCount() {
        return mArray.length;
    }

    public int getColCount() {
        return mArray[0].length;
    }

    public char[][] getArray() {
        return mArray;
    }

    public char at(int row, int col) {
        return mArray[row][col];
    }

    public void setAt(int row, int col, char c) {
        mArray[row][col] = c;
    }

    public void reset() {
        for (int i = 0; i < mArray.length; i++) {
            for (int j = 0; j < mArray[i].length; j++) {
                mArray[i][j] = NULL_CHAR;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColCount(); j++) {
                stringBuilder.append(at(i, j));
            }

            if (i != getRowCount() - 1)
                stringBuilder.append(GRID_NEWLINE_SEPARATOR);
        }

        return stringBuilder.toString();
    }
}
