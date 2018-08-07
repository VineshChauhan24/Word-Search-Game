package com.aar.app.wordsearch.commons.generator;

import com.aar.app.wordsearch.commons.Direction;
import com.aar.app.wordsearch.commons.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abdularis on 06/07/17.
 *
 * Try to pack all string list into grid array
 */

public class StringListGridGenerator extends GridGenerator<List<String>, List<String>> {

    private static final int MIN_GRID_ITERATION_ATTEMPT = 1;

    @Override
    public List<String> setGrid(List<String> dataInput, char[][] grid) {
//        Util.sortByLength(dataInput);

        List<String> usedStrings = new ArrayList<>();
        int usedCount;
        for (int i = 0; i < MIN_GRID_ITERATION_ATTEMPT; i++) {

            usedCount = 0;
            usedStrings.clear();
            resetGrid(grid);
            for (String word : dataInput) {
                if (tryPlaceWord(word, grid)) {
                    usedCount++;
                    usedStrings.add(word);
                }
            }

            if (usedCount >= dataInput.size())
                break;
        }

        Util.fillNullCharWidthRandom(grid);
        return usedStrings;
    }

    private Direction getRandomDirection() {
        Direction dir;
        do {
            dir = Direction.values()[ Util.getRandomInt() % Direction.values().length ];
        } while (dir == Direction.NONE);
        return dir;
    }

    private boolean tryPlaceWord(String word, char gridArr[][]) {
        Direction startDir = getRandomDirection();
        Direction currDir = startDir;

        int row;
        int col;
        int startRow;
        int startCol;

        /*
            coba seluruh kemungkinan arah
         */
        do {

            /*
                coba seluruh kemungkinan row dengan arah currDir
             */
            startRow = Util.getRandomInt() % gridArr.length;
            row = startRow;
            do {

                /*
                    coba seluruh kemungkinan column dan row dengan arah currDir
                 */
                startCol = Util.getRandomInt() % gridArr[0].length;
                col = startCol;
                do {

                    if (isValidPlacement(row, col, currDir, gridArr, word)) {
                        placeWordAt(row, col, currDir, gridArr, word);
                        return true;
                    }

                    col = (++col) % gridArr[0].length;
                } while (col != startCol);

                row = (++row) % gridArr.length;
            } while (row != startRow);

            currDir = currDir.nextDirection();
        } while (currDir != startDir);

        return false;
    }

    /**
     * Cek apakah penempatan di posisi grid col dan row dengan arah dir dan string word
     * memungkinkan
     *
     * @param col starting column
     * @param row starting row
     * @param dir direction of the word
     * @param gridArr grid where the word will be placed
     * @param word the actual word to be checked
     * @return true if it is a valid placement, false otherwise
     */
    private boolean isValidPlacement(int row, int col, Direction dir, char gridArr[][], String word) {
        int wLen = word.length();
        if (dir == Direction.EAST && (col + wLen) >= gridArr[0].length) return false;
        if (dir == Direction.WEST && (col - wLen) < 0) return false;

        if (dir == Direction.NORTH && (row - wLen) < 0) return false;
        if (dir == Direction.SOUTH && (row + wLen) >= gridArr.length) return false;

        if (dir == Direction.SOUTH_EAST && ((col + wLen) >= gridArr[0].length || (row + wLen) >= gridArr.length)) return false;
        if (dir == Direction.NORTH_WEST && ((col - wLen) < 0 || (row - wLen) < 0)) return false;

        if (dir == Direction.SOUTH_WEST && ((col - wLen) < 0 || (row + wLen) >= gridArr.length)) return false;
        if (dir == Direction.NORTH_EAST && ((col + wLen) >= gridArr[0].length || (row - wLen) < 0)) return false;

        for (int i = 0; i < wLen; i++) {
            if (gridArr[row][col] != Util.NULL_CHAR && gridArr[row][col] != word.charAt(i))
                return false;
            col += dir.xOff;
            row += dir.yOff;
        }

        return true;
    }

    /**
     * Letakan word pada posisi awal row, col dengan arah dir pada grid array.
     */
    private void placeWordAt(int row, int col, Direction dir, char gridArr[][], String word) {
        for (int i = 0; i < word.length(); i++) {
            gridArr[row][col] = word.charAt(i);

            col += dir.xOff;
            row += dir.yOff;
        }
    }
}
