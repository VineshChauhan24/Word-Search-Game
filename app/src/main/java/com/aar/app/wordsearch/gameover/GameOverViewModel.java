package com.aar.app.wordsearch.gameover;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.aar.app.wordsearch.domain.data.source.GameRoundDataSource;
import com.aar.app.wordsearch.domain.model.GameRoundStat;

public class GameOverViewModel extends ViewModel {

    private GameRoundDataSource mGameRoundDataSource;
    private MutableLiveData<GameRoundStat> mOnGameRoundStatLoaded = new MutableLiveData<>();

    public GameOverViewModel(GameRoundDataSource gameRoundDataSource) {
        mGameRoundDataSource = gameRoundDataSource;
    }

    public void loadData(int gid) {
        mGameRoundDataSource.getGameRoundStat(gid, mOnGameRoundStatLoaded::setValue);
    }

    public void deleteGameRound(int gid) {
        mGameRoundDataSource.deleteGameRound(gid);
    }

    public LiveData<GameRoundStat> getOnGameRoundStatLoaded() {
        return mOnGameRoundStatLoaded;
    }
}
