package com.aar.app.wordsearch.mainmenu;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.aar.app.wordsearch.domain.data.source.GameRoundDataSource;
import com.aar.app.wordsearch.model.GameRound;

import java.util.List;

public class MainMenuViewModel extends ViewModel {

    private GameRoundDataSource mDataSource;

    private MutableLiveData<List<GameRound.Info>> mOnGameRoundInfoLoaded;

    public MainMenuViewModel(GameRoundDataSource gameRoundDataSource) {
        mDataSource = gameRoundDataSource;

        mOnGameRoundInfoLoaded = new MutableLiveData<>();
    }

    public void loadData() {
        mDataSource.getGameRoundInfos(infoList -> mOnGameRoundInfoLoaded.setValue(infoList));
    }

    public void clearAll() {
        mDataSource.deleteGameRounds();
    }

    public void deleteGameRound(final GameRound.Info info) {
        mDataSource.deleteGameRound(info.getId());
    }

    public LiveData<List<GameRound.Info>> getOnGameRoundInfoLoaded() {
        return mOnGameRoundInfoLoaded;
    }
}
