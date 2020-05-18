package com.aar.app.wordsearch.features.mainmenu;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aar.app.wordsearch.data.GameThemeRepository;
import com.aar.app.wordsearch.model.GameTheme;

import java.util.List;

public class MainMenuViewModel extends ViewModel {

    private GameThemeRepository mGameThemeRepository;

    private MutableLiveData<List<GameTheme>> mOnGameThemeLoaded;

    public MainMenuViewModel(GameThemeRepository gameThemeRepository) {
        mGameThemeRepository = gameThemeRepository;
        mOnGameThemeLoaded = new MutableLiveData<>();
    }

    public void loadData() {
        mOnGameThemeLoaded.setValue(mGameThemeRepository.getGameThemes());
    }

    public LiveData<List<GameTheme>> getOnGameThemeLoaded() {
        return mOnGameThemeLoaded;
    }
}
