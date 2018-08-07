package com.aar.app.wordsearch.mainmenu.domain;

import com.aar.app.wordsearch.domain.data.source.GameRoundDataSource;
import com.aar.app.wordsearch.domain.UseCase;

import javax.inject.Inject;

/**
 * Created by abdularis on 20/07/17.
 */

public class ClearGameRoundsUseCase extends UseCase<UseCase.Params, UseCase.Result> {

    private GameRoundDataSource mDataSource;

    @Inject
    public ClearGameRoundsUseCase(GameRoundDataSource dataSource) {
        mDataSource = dataSource;
    }

    @Override
    protected void execute(Params params) {
        mDataSource.deleteGameRounds();
        getCallback().onSuccess(new Result());
    }

    public static class Result implements UseCase.Result {
    }
}
