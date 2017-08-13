package com.paperplanes.wordsearch.presentation.views;

import com.paperplanes.wordsearch.domain.model.GameRound;

import java.util.List;

/**
 * Created by abdularis on 20/07/17.
 */

public interface MainMenuView {

    void setNewGameLoading(boolean enable);

    void showGameInfoList(List<GameRound.Info> infoList);

    void showNewlyCreatedGameRound(GameRound gameRound);

    void showGameRound(int gid);

    void clearInfoList();

    void deleteInfo(GameRound.Info info);

}
