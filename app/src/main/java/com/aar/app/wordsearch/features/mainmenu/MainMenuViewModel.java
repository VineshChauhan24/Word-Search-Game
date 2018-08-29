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

    private MutableLiveData<List<GameDataInfo>> mOnGameRoundInfoLoaded;
    private MutableLiveData<List<GameTheme>> mOnGameThemeLoaded;

    public MainMenuViewModel(GameDataSource gameDataSource, GameThemeRepository gameThemeRepository) {
        mDataSource = gameDataSource;
        mGameThemeRepository = gameThemeRepository;

        mOnGameRoundInfoLoaded = new MutableLiveData<>();
        mOnGameThemeLoaded = new MutableLiveData<>();
    }

    public void loadData() {
        mDataSource.getGameDataInfos(infoList -> mOnGameRoundInfoLoaded.setValue(infoList));
        mOnGameThemeLoaded.setValue(mGameThemeRepository.getGameThemes());
    }

    public void clearAll() {
        mDataSource.deleteGameDatas();
    }

    public void deleteGameRound(final GameDataInfo info) {
        mDataSource.deleteGameData(info.getId());
        if (mOnGameRoundInfoLoaded.getValue() != null) {
            mOnGameRoundInfoLoaded.getValue().remove(info);
            mOnGameRoundInfoLoaded.setValue(mOnGameRoundInfoLoaded.getValue());
        }
    }

    public LiveData<List<GameDataInfo>> getOnGameRoundInfoLoaded() {
        return mOnGameRoundInfoLoaded;
    }

    public LiveData<List<GameTheme>> getOnGameThemeLoaded() {
        return mOnGameThemeLoaded;
    }
}
