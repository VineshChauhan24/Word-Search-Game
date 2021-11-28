package ke.choxxy.wordsearch.data;

import ke.choxxy.wordsearch.data.entity.GameDataEntity;
import ke.choxxy.wordsearch.model.GameDataInfo;
import ke.choxxy.wordsearch.model.UsedWord;

import java.util.List;

/**
 * Created by abdularis on 18/07/17.
 */

public interface GameDataSource {

    interface GameRoundCallback {

        void onLoaded(GameDataEntity gameRound);

    }

    interface InfosCallback {

        void onLoaded(List<GameDataInfo> infoList);
    }

    interface StatCallback {

        void onLoaded(GameDataInfo gameDataInfo);

    }

    void getGameData(int gid, GameRoundCallback callback);

    void getGameDataInfos(InfosCallback callback);

    void getGameDataInfo(int gid, StatCallback callback);

    long saveGameData(GameDataEntity gameRound);

    void deleteGameData(int gid);

    void deleteGameDatas();

    void saveGameDataDuration(int gid, int newDuration);

    void markWordAsAnswered(UsedWord usedWord);
}
