package com.aar.app.wordsearch;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.aar.app.wordsearch.gameover.GameOverViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private GameOverViewModel mGameOverViewModel;

    public ViewModelFactory(GameOverViewModel gameOverViewModel) {
        mGameOverViewModel = gameOverViewModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GameOverViewModel.class)) {
            return (T) mGameOverViewModel;
        }
        throw new IllegalArgumentException("Unknown view model");
    }
}
