package com.aar.app.wordsearch.gameover;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.aar.app.wordsearch.data.GameDataSource;
import com.aar.app.wordsearch.model.GameDataStatistic;

public class GameOverViewModel extends ViewModel {

    private GameDataSource mGameDataSource;
    private MutableLiveData<GameDataStatistic> mOnGameRoundStatLoaded = new MutableLiveData<>();

    public GameOverViewModel(GameDataSource gameDataSource) {
        mGameDataSource = gameDataSource;
    }

    public void loadData(int gid) {
        mGameDataSource.getGameDataStat(gid, mOnGameRoundStatLoaded::setValue);
    }

    public void deleteGameRound(int gid) {
        mGameDataSource.deleteGameData(gid);
    }

    public LiveData<GameDataStatistic> getOnGameRoundStatLoaded() {
        return mOnGameRoundStatLoaded;
    }
}
