package com.aar.app.wordsearch.commons.generator;

/**
 * Created by abdularis on 06/07/17.
 *
 * Base class untuk grid generator
 */

public abstract class GridGenerator<InputType, OutputValue> {

    public abstract OutputValue setGrid(InputType dataInput, char grid[][]);

    protected void resetGrid(char grid[][]) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = '\0';
            }
        }
    }

}
