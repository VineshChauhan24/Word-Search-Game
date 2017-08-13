package com.paperplanes.wordsearch.domain.usecases;

import com.paperplanes.wordsearch.domain.data.source.GameRoundDataSource;
import com.paperplanes.wordsearch.domain.model.GameRound;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by abdularis on 20/07/17.
 */

public class GetGameRoundInfosUseCase extends UseCase<GetGameRoundInfosUseCase.Params, GetGameRoundInfosUseCase.Result> {

    private GameRoundDataSource mDataSource;

    @Inject
    public GetGameRoundInfosUseCase(GameRoundDataSource dataSource) {
        mDataSource = dataSource;
    }

    @Override
    protected void execute(Params params) {
        mDataSource.getGameRoundInfos(new GameRoundDataSource.InfosCallback() {
            @Override
            public void onLoaded(List<GameRound.Info> infoList) {
                getCallback().onSuccess(new Result(infoList));
            }
        });
    }

    public static class Params implements UseCase.Params {
    }

    public static class Result implements UseCase.Result {
        public List<GameRound.Info> infoList;

        public Result(List<GameRound.Info> infoList) {
            this.infoList = infoList;
        }
    }

}
