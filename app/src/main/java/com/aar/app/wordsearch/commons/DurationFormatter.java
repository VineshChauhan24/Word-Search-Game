package com.aar.app.wordsearch.commons;

import java.util.Locale;

/**
 * Created by abdularis on 19/07/17.
 */

public abstract class DurationFormatter {

    public static String fromInteger(int duration) {
        if (duration <= 0)
            return "00:00";

        int secs = duration % 60;
        int min = duration / 60;

        return String.format(Locale.US, "%02d:%02d", min, secs);
    }

}
