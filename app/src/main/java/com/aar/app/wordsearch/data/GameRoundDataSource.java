package com.aar.app.wordsearch.data;

import com.aar.app.wordsearch.data.entity.GameRoundEntity;
import com.aar.app.wordsearch.model.GameRound;
import com.aar.app.wordsearch.model.GameRoundStat;
import com.aar.app.wordsearch.model.UsedWord;

import java.util.List;

/**
 * Created by abdularis on 18/07/17.
 */

public interface GameRoundDataSource {

    interface GameRoundCallback {

        void onLoaded(GameRoundEntity gameRound);

    }

    interface InfosCallback {

        void onLoaded(List<GameRound.Info> infoList);
    }

    interface StatCallback {

        void onLoaded(GameRoundStat stat);

    }

    void getGameRound(int gid, GameRoundCallback callback);

    void getGameRoundInfos(InfosCallback callback);

    void getGameRoundStat(int gid, StatCallback callback);

    void saveGameRound(GameRoundEntity gameRound);

    void deleteGameRound(int gid);

    void deleteGameRounds();

    void saveGameRoundDuration(int gid, int newDuration);

    void markWordAsAnswered(UsedWord usedWord);
}
