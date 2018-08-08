package com.aar.app.wordsearch;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.aar.app.wordsearch.gameover.GameOverViewModel;
import com.aar.app.wordsearch.gameplay.GamePlayViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private GameOverViewModel mGameOverViewModel;
    private GamePlayViewModel mGamePlayViewModel;

    public ViewModelFactory(GameOverViewModel gameOverViewModel,
                            GamePlayViewModel gamePlayViewModel) {
        mGameOverViewModel = gameOverViewModel;
        mGamePlayViewModel = gamePlayViewModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GameOverViewModel.class)) {
            return (T) mGameOverViewModel;
        } else if (modelClass.isAssignableFrom(GamePlayViewModel.class)) {
            return (T) mGamePlayViewModel;
        }
        throw new IllegalArgumentException("Unknown view model");
    }
}
