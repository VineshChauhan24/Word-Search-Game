package com.aar.app.wordsearch.commons.generator;

import com.aar.app.wordsearch.model.Grid;

/**
 * Created by abdularis on 06/07/17.
 *
 * Parse dataInput kedalam array grid[][]
 */

public class StringGridGenerator extends GridGenerator<String, Boolean> {

    @Override
    public Boolean setGrid(String dataInput, char[][] grid) {
        if (dataInput == null || grid == null) return false;

        dataInput = dataInput.trim();

        int row = 0;
        int col = 0;

        for (int i = 0; i < dataInput.length(); i++) {
            char c = dataInput.charAt(i);

            if (c == Grid.GRID_NEWLINE_SEPARATOR) {
                row++;
                col = 0;
            }
            else {
                if (row >= grid.length || col >= grid[0].length) {
                    resetGrid(grid);
                    return false;
                }

                grid[row][col] = c;

                col++;
            }
        }

        return true;
    }
}
