package com.paperplanes.wordsearch.domain.usecases;

import com.paperplanes.wordsearch.domain.data.source.GameRoundDataSource;

import javax.inject.Inject;

/**
 * Created by abdularis on 20/07/17.
 */

public class DeleteGameRoundUseCase extends UseCase<DeleteGameRoundUseCase.Params, DeleteGameRoundUseCase.Result> {

    private GameRoundDataSource mDataSource;

    @Inject
    public DeleteGameRoundUseCase(GameRoundDataSource dataSource) {
        mDataSource = dataSource;
    }

    @Override
    protected void execute(Params params) {
        mDataSource.deleteGameRound(params.gameRoundId);
        getCallback().onSuccess(new Result());
    }

    public static class Params implements UseCase.Params {
        public int gameRoundId;

        public Params(int gameRoundId) {
            this.gameRoundId = gameRoundId;
        }
    }

    public static class Result implements UseCase.Result {}
}
