package com.aar.app.wordsearch.domain.usecases;

import com.aar.app.wordsearch.domain.data.source.GameRoundDataSource;
import com.aar.app.wordsearch.domain.model.GameRoundStat;

import javax.inject.Inject;

/**
 * Created by abdularis on 24/07/17.
 */

public class GetGameRoundStatUseCase
        extends UseCase<GetGameRoundStatUseCase.Params, GetGameRoundStatUseCase.Result> {

    private GameRoundDataSource mDataSource;

    @Inject
    public GetGameRoundStatUseCase(GameRoundDataSource dataSource) {
        mDataSource = dataSource;
    }

    @Override
    protected void execute(Params params) {
        mDataSource.getGameRoundStat(params.gameRoundId, new GameRoundDataSource.StatCallback() {
            @Override
            public void onLoaded(GameRoundStat stat) {
                getCallback().onSuccess(new Result(stat));
            }
        });
    }

    public static class Params implements UseCase.Params {
        public int gameRoundId;

        public Params(int gameRoundId) {
            this.gameRoundId = gameRoundId;
        }
    }

    public static class Result implements UseCase.Result {
        public GameRoundStat gameRoundStat;

        public Result(GameRoundStat gameRoundStat) {
            this.gameRoundStat = gameRoundStat;
        }
    }
}
