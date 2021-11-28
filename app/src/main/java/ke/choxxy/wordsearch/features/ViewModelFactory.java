package ke.choxxy.wordsearch.features;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import ke.choxxy.wordsearch.features.gamehistory.GameHistoryViewModel;
import ke.choxxy.wordsearch.features.gameover.GameOverViewModel;
import ke.choxxy.wordsearch.features.gameplay.GamePlayViewModel;
import ke.choxxy.wordsearch.features.mainmenu.MainMenuViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private GameOverViewModel mGameOverViewModel;
    private GamePlayViewModel mGamePlayViewModel;
    private MainMenuViewModel mMainMenuViewModel;
    private GameHistoryViewModel mGameHistoryViewModel;

    public ViewModelFactory(GameOverViewModel gameOverViewModel,
                            GamePlayViewModel gamePlayViewModel,
                            MainMenuViewModel mainMenuViewModel,
                            GameHistoryViewModel gameHistoryViewModel) {
        mGameOverViewModel = gameOverViewModel;
        mGamePlayViewModel = gamePlayViewModel;
        mMainMenuViewModel = mainMenuViewModel;
        mGameHistoryViewModel = gameHistoryViewModel;
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
        } else if (modelClass.isAssignableFrom(GameHistoryViewModel.class)) {
            return (T) mGameHistoryViewModel;
        }
        throw new IllegalArgumentException("Unknown view model");
    }
}
