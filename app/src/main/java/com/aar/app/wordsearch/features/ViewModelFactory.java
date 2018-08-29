package com.aar.app.wordsearch.features;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.aar.app.wordsearch.features.gameover.GameOverViewModel;
import com.aar.app.wordsearch.features.gameplay.GamePlayViewModel;
import com.aar.app.wordsearch.features.mainmenu.MainMenuViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private GameOverViewModel mGameOverViewModel;
    private GamePlayViewModel mGamePlayViewModel;
    private MainMenuViewModel mMainMenuViewModel;

    public ViewModelFactory(GameOverViewModel gameOverViewModel,
                            GamePlayViewModel gamePlayViewModel,
                            MainMenuViewModel mainMenuViewModel) {
        mGameOverViewModel = gameOverViewModel;
        mGamePlayViewModel = gamePlayViewModel;
        mMainMenuViewModel = mainMenuViewModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GameOverViewModel.class)) {
            return (T) mGameOverViewModel;
        } else if (modelClass.isAssignableFrom(GamePlayViewModel.class)) {
            return (T) mGamePlayViewModel;
        } else if (modelClass.isAssignableFrom(MainMenuViewModel.class)) {
            return (T) mMainMenuViewModel;
        }
        throw new IllegalArgumentException("Unknown view model");
    }
}
