package com.aar.app.wordsearch;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

import com.aar.app.wordsearch.R;
import com.aar.app.wordsearch.settings.Preferences;

import javax.inject.Inject;

/**
 * Created by abdularis on 22/07/17.
 */

public class SoundManager {

    public static final int SOUND_CORRECT = 0;
    public static final int SOUND_WRONG = 1;

    private Preferences mPreferences;

    private SoundPool mSoundPool;
    private SparseIntArray mSoundPoolMap;

    @Inject
    public SoundManager(Context context, Preferences preferences) {
        mPreferences = preferences;

        init(context);
    }

    public void play(int sound) {
        if (mPreferences.enableSound()) {
            mSoundPool.play(mSoundPoolMap.get(sound), 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }

    private void init(Context context) {
        mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        mSoundPoolMap = new SparseIntArray();

        mSoundPoolMap.put(SOUND_CORRECT,
                mSoundPool.load(context, R.raw.correct, 1));
        mSoundPoolMap.put(SOUND_WRONG,
                mSoundPool.load(context, R.raw.wrong, 1));
    }

}
