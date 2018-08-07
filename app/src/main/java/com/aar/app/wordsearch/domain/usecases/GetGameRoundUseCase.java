package com.aar.app.wordsearch.domain.usecases;

import com.aar.app.wordsearch.domain.data.entity.GameRoundEntity;
import com.aar.app.wordsearch.domain.data.mapper.GameRoundMapper;
import com.aar.app.wordsearch.domain.data.source.GameRoundDataSource;
import com.aar.app.wordsearch.domain.model.GameRound;

import javax.inject.Inject;

/**
 * Created by abdularis on 18/07/17.
 */

public class GetGameRoundUseCase extends UseCase<GetGameRoundUseCase.Params, GetGameRoundUseCase.Result> {

    private GameRoundDataSource mGameRoundDataSource;

    @Inject
    public GetGameRoundUseCase(GameRoundDataSource gameRoundDataSource) {
        mGameRoundDataSource = gameRoundDataSource;
    }

    @Override
    protected void execute(final Params params) {
        mGameRoundDataSource.getGameRound(params.gameRoundId, new GameRoundDataSource.GameRoundCallback() {
            @Override
            public void onLoaded(GameRoundEntity gameRoundEnt) {
                GameRound gameRound = new GameRoundMapper().map(gameRoundEnt);
                getCallback().onSuccess(new Result(gameRound));
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
        public GameRound gameRound;

        public Result(GameRound gameRound) {
            this.gameRound = gameRound;
        }
    }

}
