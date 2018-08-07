package com.aar.app.wordsearch.commons;

/**
 * Created by abdularis on 29/06/17.
 *
 * Direction (Arah)
 */

public enum Direction {
    NONE(0, 0),
    EAST(1, 0),
    WEST(-1, 0),
    NORTH(0, -1),
    SOUTH(0, 1),
    SOUTH_EAST(1, 1),
    NORTH_WEST(-1, -1),
    SOUTH_WEST(-1, 1),
    NORTH_EAST(1, -1);

    public final int xOff;
    public final int yOff;

    Direction(int xOff, int yOff) {
        this.xOff = xOff;
        this.yOff = yOff;
    }

    public Direction nextDirection() {
        int idx = (ordinal() + 1) % values().length;
        if (values()[idx] == NONE)
            idx++;
        return values()[idx];
    }

    public static Direction fromLine(GridIndex start, GridIndex end) {

        /*
            Horizontal
         */
        if (start.row == end.row && start.col != end.col) {
            if (start.col < end.col) return EAST;
            else return WEST;
        }
        /*
            Vertical
         */
        else if (start.col == end.col && start.row != end.row) {
            if (start.row < end.row) return SOUTH;
            else return NORTH;
        }
        else {
            int diffX = Math.abs(start.col - end.col);
            int diffY = Math.abs(start.row - end.row);

            /*
                Diagonal
             */
            if (diffX == diffY && diffX != 0) {
                if (start.col < end.col && start.row < end.row) return SOUTH_EAST;
                if (start.col > end.col && start.row > end.row) return NORTH_WEST;
                if (start.col > end.col && start.row < end.row) return SOUTH_WEST;
                else return NORTH_EAST;
            }
        }

        return NONE;
    }
}