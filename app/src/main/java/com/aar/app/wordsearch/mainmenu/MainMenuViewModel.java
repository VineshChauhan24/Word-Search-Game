package com.aar.app.wordsearch.mainmenu;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.aar.app.wordsearch.data.GameDataSource;
import com.aar.app.wordsearch.model.GameData;

import java.util.List;

public class MainMenuViewModel extends ViewModel {

    private GameDataSource mDataSource;

    private MutableLiveData<List<GameData.Info>> mOnGameRoundInfoLoaded;

    public MainMenuViewModel(GameDataSource gameDataSource) {
        mDataSource = gameDataSource;

        mOnGameRoundInfoLoaded = new MutableLiveData<>();
    }

    public void loadData() {
        mDataSource.getGameRoundInfos(infoList -> mOnGameRoundInfoLoaded.setValue(infoList));
    }

    public void clearAll() {
        mDataSource.deleteGameRounds();
    }

    public void deleteGameRound(final GameData.Info info) {
        mDataSource.deleteGameRound(info.getId());
    }

    public LiveData<List<GameData.Info>> getOnGameRoundInfoLoaded() {
        return mOnGameRoundInfoLoaded;
    }
}
