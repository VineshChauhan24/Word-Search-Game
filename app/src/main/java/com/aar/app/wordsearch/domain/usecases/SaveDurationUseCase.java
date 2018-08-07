package com.aar.app.wordsearch.domain.usecases;

import com.aar.app.wordsearch.domain.data.source.GameRoundDataSource;

import javax.inject.Inject;

/**
 * Created by abdularis on 20/07/17.
 */

public class SaveDurationUseCase extends UseCase<SaveDurationUseCase.Params, SaveDurationUseCase.Result> {

    private GameRoundDataSource mDataSource;

    @Inject
    public SaveDurationUseCase(GameRoundDataSource dataSource) {
        mDataSource = dataSource;
    }

    @Override
    protected void execute(Params params) {
        mDataSource.saveGameRoundDuration(params.gameRoundId, params.duration);
        getCallback().onSuccess(new Result());
    }

    public static class Params implements UseCase.Params {
        public int gameRoundId;
        public int duration;

        public Params(int gameRoundId, int duration) {
            this.gameRoundId = gameRoundId;
            this.duration = duration;
        }
    }

    public static class Result implements UseCase.Result {}
}
