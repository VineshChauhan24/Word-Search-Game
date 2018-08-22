package com.aar.app.wordsearch.data;

import com.aar.app.wordsearch.data.entity.GameDataEntity;
import com.aar.app.wordsearch.model.GameData;
import com.aar.app.wordsearch.model.GameDataStatistic;
import com.aar.app.wordsearch.model.UsedWord;

import java.util.List;

/**
 * Created by abdularis on 18/07/17.
 */

public interface GameDataSource {

    interface GameRoundCallback {

        void onLoaded(GameDataEntity gameRound);

    }

    interface InfosCallback {

        void onLoaded(List<GameData.Info> infoList);
    }

    interface StatCallback {

        void onLoaded(GameDataStatistic stat);

    }

    void getGameData(int gid, GameRoundCallback callback);

    void getGameDataInfos(InfosCallback callback);

    void getGameDataStat(int gid, StatCallback callback);

    void saveGameData(GameDataEntity gameRound);

    void deleteGameData(int gid);

    void deleteGameDatas();

    void saveGameDataDuration(int gid, int newDuration);

    void markWordAsAnswered(UsedWord usedWord);
}
