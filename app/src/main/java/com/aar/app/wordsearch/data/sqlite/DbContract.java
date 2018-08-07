package com.aar.app.wordsearch.data.sqlite;

import android.provider.BaseColumns;

/**
 * Created by abdularis on 18/07/17.
 */

abstract class DbContract {

    static class WordBank implements BaseColumns {
        static final String TABLE_NAME = "word_bank";

        static final String COL_STRING = "string";
    }

    static class GameRound implements BaseColumns {
        static final String TABLE_NAME = "game_round";

        static final String COL_NAME = "name";
        static final String COL_DURATION = "duration";
        static final String COL_GRID_ROW_COUNT = "grid_row_count";
        static final String COL_GRID_COL_COUNT = "grid_col_count";
        static final String COL_GRID_DATA = "grid_data";
    }

    static class UsedWord implements BaseColumns {
        static final String TABLE_NAME = "used_words";

        static final String COL_GAME_ROUND_ID = "game_round_id";
        static final String COL_WORD_STRING = "word_id";
        static final String COL_ANSWER_LINE_DATA = "answer_line_data";
        static final String COL_LINE_COLOR = "line_color";
        static final String COL_IS_MYSTERY = "mystery";
        static final String COL_REVEAL_COUNT = "reveal_count";
    }
}
