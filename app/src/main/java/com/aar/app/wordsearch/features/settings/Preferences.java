package com.aar.app.wordsearch.features.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.aar.app.wordsearch.R;
import com.aar.app.wordsearch.custom.StreakView;

import javax.inject.Inject;

/**
 * Created by abdularis on 19/07/17.
 */

public class Preferences {
    private static String KEY_SHOW_GRID_LINE;
    private static String KEY_SNAP_TO_GRID;
    private static String KEY_ENABLE_SOUND;
    private static String KEY_DELETE_AFTER_FINISH;
    private static String KEY_ENABLE_FULLSCREEN;
    private static String KEY_AUTO_SCALE_GRID;
    private static String KEY_REVERSE_MATCHING;
    private static String KEY_GRAYSCALE;
    private static String KEY_PREV_SAVE_GAME_DATA_COUNT = "prevSaveGameDataCount";

    private static String DEF_SNAP_TO_GRID;

    private SharedPreferences mPreferences;

    @Inject
    public Preferences(Context context, SharedPreferences preferences) {
        mPreferences = preferences;

        KEY_SHOW_GRID_LINE = context.getString(R.string.pref_showGridLine);
        KEY_SNAP_TO_GRID = context.getString(R.string.pref_snapToGrid);
        KEY_ENABLE_SOUND = context.getString(R.string.pref_enableSound);
        KEY_DELETE_AFTER_FINISH = context.getString(R.string.pref_deleteAfterFinish);
        KEY_ENABLE_FULLSCREEN = context.getString(R.string.pref_enableFullscreen);
        KEY_AUTO_SCALE_GRID = context.getString(R.string.pref_autoScaleGrid);
        KEY_REVERSE_MATCHING = context.getString(R.string.pref_reverseMatching);
        KEY_GRAYSCALE = context.getString(R.string.pref_grayscale);
        DEF_SNAP_TO_GRID = context.getString(R.string.snap_to_grid_def_val);
    }

    public boolean showGridLine() {
        return mPreferences.getBoolean(KEY_SHOW_GRID_LINE, false);
    }

    public StreakView.SnapType getSnapToGrid() {
        String str = mPreferences.getString(KEY_SNAP_TO_GRID, DEF_SNAP_TO_GRID);
        return StreakView.SnapType.valueOf(str);
    }

    public boolean enableSound() {
        return mPreferences.getBoolean(KEY_ENABLE_SOUND, true);
    }

    public boolean enableFullscreen() {
        return mPreferences.getBoolean(KEY_ENABLE_FULLSCREEN, false);
    }

    public boolean deleteAfterFinish() {
        return mPreferences.getBoolean(KEY_DELETE_AFTER_FINISH, true);
    }

    public boolean autoScaleGrid() {
        return mPreferences.getBoolean(KEY_AUTO_SCALE_GRID, true);
    }

    public boolean reverseMatching() {
        return mPreferences.getBoolean(KEY_REVERSE_MATCHING, true);
    }

    public boolean grayscale() {
        return mPreferences.getBoolean(KEY_GRAYSCALE, false);
    }

    public void resetSaveGameDataCount() {
        mPreferences.edit()
                .putInt(KEY_PREV_SAVE_GAME_DATA_COUNT, 0)
                .apply();
    }

    public void incrementSavedGameDataCount() {
        mPreferences.edit()
                .putInt(KEY_PREV_SAVE_GAME_DATA_COUNT, getPreviouslySavedGameDataCount() + 1)
                .apply();
    }

    public int getPreviouslySavedGameDataCount() {
        return mPreferences.getInt(KEY_PREV_SAVE_GAME_DATA_COUNT, 0);
    }
}
