package com.aar.app.wordsearch.domain.data.source;

import com.aar.app.wordsearch.domain.data.entity.GameRoundEntity;
import com.aar.app.wordsearch.domain.model.GameRound;
import com.aar.app.wordsearch.domain.model.GameRoundStat;
import com.aar.app.wordsearch.domain.model.UsedWord;

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
