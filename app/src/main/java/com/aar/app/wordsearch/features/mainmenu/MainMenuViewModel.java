package com.aar.app.wordsearch.features.mainmenu;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.aar.app.wordsearch.data.GameDataSource;
import com.aar.app.wordsearch.data.GameThemeRepository;
import com.aar.app.wordsearch.model.GameDataInfo;
import com.aar.app.wordsearch.model.GameTheme;

import java.util.List;

public class MainMenuViewModel extends ViewModel {

    private GameDataSource mDataSource;
    private GameThemeRepository mGameThemeRepository;

    private MutableLiveData<List<GameTheme>> mOnGameThemeLoaded;

    public MainMenuViewModel(GameDataSource gameDataSource, GameThemeRepository gameThemeRepository) {
        mDataSource = gameDataSource;
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
