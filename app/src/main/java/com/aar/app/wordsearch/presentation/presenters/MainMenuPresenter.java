package com.aar.app.wordsearch.presentation.presenters;

import com.aar.app.wordsearch.config.Preferences;
import com.aar.app.wordsearch.domain.model.GameRound;
import com.aar.app.wordsearch.domain.usecases.BuildGameRoundUseCase;
import com.aar.app.wordsearch.domain.usecases.ClearGameRoundsUseCase;
import com.aar.app.wordsearch.domain.usecases.DeleteGameRoundUseCase;
import com.aar.app.wordsearch.domain.usecases.GetGameRoundInfosUseCase;
import com.aar.app.wordsearch.domain.usecases.UseCase;
import com.aar.app.wordsearch.domain.usecases.UseCaseExecutor;
import com.aar.app.wordsearch.presentation.views.MainMenuView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by abdularis on 20/07/17.
 */

public class MainMenuPresenter {

    @Inject
    Preferences mPref;

    private MainMenuView mView;
    private UseCaseExecutor mCaseExecutor;
    private BuildGameRoundUseCase mBuildGameRoundUseCase;
    private GetGameRoundInfosUseCase mGameRoundInfosUseCase;
    private ClearGameRoundsUseCase mClearGameRoundsUseCase;
    private DeleteGameRoundUseCase mDeleteGameRoundUseCase;

    List<GameRound.Info> mInfoList;

    @Inject
    public MainMenuPresenter(UseCaseExecutor caseExecutor,
                             BuildGameRoundUseCase buildGameRoundUseCase, GetGameRoundInfosUseCase gameRoundInfosUseCase,
                             ClearGameRoundsUseCase clearGameRoundsUseCase, DeleteGameRoundUseCase deleteGameRoundUseCase) {
        mCaseExecutor = caseExecutor;
        mBuildGameRoundUseCase = buildGameRoundUseCase;
        mGameRoundInfosUseCase = gameRoundInfosUseCase;
        mClearGameRoundsUseCase = clearGameRoundsUseCase;
        mDeleteGameRoundUseCase = deleteGameRoundUseCase;
    }

    public void setView(MainMenuView view) {
        mView = view;
    }

    public void loadData() {
        mCaseExecutor.execute(mGameRoundInfosUseCase, new UseCase.Callback<GetGameRoundInfosUseCase.Result>() {
            @Override
            public void onSuccess(GetGameRoundInfosUseCase.Result result) {
                mInfoList = result.infoList;
                mView.showGameInfoList(result.infoList);
            }

            @Override
            public void onFailed(String errMsg) {}
        });
    }

    public void clearAll() {
        mCaseExecutor.execute(mClearGameRoundsUseCase, new UseCase.Callback<UseCase.Result>() {
            @Override
            public void onSuccess(UseCase.Result result) {
                mPref.resetSaveGameDataCount();
                mView.clearInfoList();
            }

            @Override
            public void onFailed(String errMsg) {}
        });
    }

    public void deleteGameRound(final GameRound.Info info) {
        mDeleteGameRoundUseCase.setParams(new DeleteGameRoundUseCase.Params(info.getId()));
        mCaseExecutor.execute(mDeleteGameRoundUseCase, new UseCase.Callback<DeleteGameRoundUseCase.Result>() {
            @Override
            public void onSuccess(DeleteGameRoundUseCase.Result result) {
                mView.deleteInfo(info);
            }

            @Override
            public void onFailed(String errMsg) {}
        });
    }

    public void newGameRound(int rowCount, int colCount) {
        mView.setNewGameLoading(true);

        String puzzleName = "Puzzle " + mPref.getPreviouslySavedGameDataCount();
        mBuildGameRoundUseCase.setParams(new BuildGameRoundUseCase.Params(rowCount, colCount, puzzleName));
        mCaseExecutor.execute(mBuildGameRoundUseCase, new UseCase.Callback<BuildGameRoundUseCase.Result>() {
            @Override
            public void onSuccess(BuildGameRoundUseCase.Result result) {
                mPref.incrementSavedGameDataCount();
                mView.setNewGameLoading(false);
                mView.showNewlyCreatedGameRound(result.gameRound);
            }

            @Override
            public void onFailed(String errMsg) { mView.setNewGameLoading(false); }
        });
    }

    public void gameRoundSelected(GameRound.Info info) {
        mView.showGameRound(info.getId());
    }

}
