package com.aar.app.wordsearch.presentation.presenters;

import android.os.Handler;

import com.aar.app.wordsearch.commons.Timer;
import com.aar.app.wordsearch.domain.model.UsedWord;
import com.aar.app.wordsearch.domain.usecases.AnswerWordUseCase;
import com.aar.app.wordsearch.domain.usecases.GetGameRoundUseCase;
import com.aar.app.wordsearch.domain.usecases.SaveDurationUseCase;
import com.aar.app.wordsearch.domain.usecases.UseCase;
import com.aar.app.wordsearch.domain.usecases.UseCaseExecutor;
import com.aar.app.wordsearch.presentation.custom.StreakView;
import com.aar.app.wordsearch.presentation.model.mapper.StreakLineMapper;
import com.aar.app.wordsearch.presentation.model.mapper.UsedWordMapper;
import com.aar.app.wordsearch.presentation.views.GamePlayView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by abdularis on 18/07/17.
 */

public class GamePlayPresenter {

    private static final StreakLineMapper STREAK_LINE_MAPPER = new StreakLineMapper();

    private GamePlayView mView;
    private UseCaseExecutor mCaseExecutor;
    private GetGameRoundUseCase mGetGameRoundUseCase;
    private AnswerWordUseCase mAnswerWordUseCase;
    private SaveDurationUseCase mSaveDurationUseCase;

    private int mCurrGameRound;
    private List<UsedWord> mCurrUsedWord;
    private boolean mGenerating;
    private boolean mAlreadyFinished;
    private boolean mGameLoaded;
    private int mAnsweredWordsCount;

    private Timer mTimer;
    private int mCurrDuration;

    @Inject
    public GamePlayPresenter(UseCaseExecutor caseExecutor,
                             GetGameRoundUseCase getGameRoundUseCase,
                             AnswerWordUseCase answerWordUseCase,
                             SaveDurationUseCase saveDurationUseCase) {
        mView = null;
        mCaseExecutor = caseExecutor;
        mGetGameRoundUseCase = getGameRoundUseCase;
        mAnswerWordUseCase = answerWordUseCase;
        mSaveDurationUseCase = saveDurationUseCase;
        mGenerating = false;
        mCurrDuration = 0;

        mTimer = new Timer(1000);
        mTimer.addOnTimeoutListener(new Timer.OnTimeoutListener() {
            @Override
            public void onTimeout(long ellapsedTime) {
                mView.showDuration(++mCurrDuration);
                mSaveDurationUseCase.setParams(new SaveDurationUseCase.Params(mCurrGameRound, mCurrDuration));
                mCaseExecutor.execute(mSaveDurationUseCase, new UseCase.Callback<SaveDurationUseCase.Result>() {
                    @Override
                    public void onSuccess(SaveDurationUseCase.Result result) {}

                    @Override
                    public void onFailed(String errMsg) {}
                });
            }
        });
    }

    public void setView(GamePlayView view) {
        mView = view;
    }

    public void stopGame() {
        mTimer.stop();
    }

    public void resumeGame() {
        if (!mAlreadyFinished && mGameLoaded) {
            mTimer.start();
        }
    }

    public void loadGameRound(int gid) {
        if (mGenerating) return;

        mGenerating = true;
        mCurrGameRound = gid;
        mView.showLoading(true);
        mGetGameRoundUseCase.setParams(new GetGameRoundUseCase.Params(gid));
        mCaseExecutor.execute(mGetGameRoundUseCase, new UseCase.Callback<GetGameRoundUseCase.Result>() {
            @Override
            public void onSuccess(GetGameRoundUseCase.Result result) {
                mView.showLetterGrid(result.gameRound.getGrid().getArray());
                mView.showDuration(result.gameRound.getInfo().getDuration());
                mView.showUsedWords(new UsedWordMapper().map(result.gameRound.getUsedWords()));
                mView.showWordsCount(result.gameRound.getUsedWords().size());
                mAnsweredWordsCount = result.gameRound.getAnsweredWordsCount();
                mView.showAnsweredWordsCount(mAnsweredWordsCount);

                mCurrUsedWord = result.gameRound.getUsedWords();
                mCurrDuration = result.gameRound.getInfo().getDuration();
                mGenerating = false;
                mGameLoaded = true;
                mView.showLoading(false);
                mView.doneLoadingContent();

                if (mAnsweredWordsCount >= mCurrUsedWord.size()) {
                    mView.setGameAsAlreadyFinished();
                    mAlreadyFinished = true;
                }
                else {
                    mAlreadyFinished = false;
                    resumeGame();
                }
            }

            @Override
            public void onFailed(String errMsg) {
                mView.showLoading(false);
                mGenerating = false;
            }
        });
    }

    public void answerWord(String str, StreakView.StreakLine streakLine, boolean reverseMatching) {
        mAnswerWordUseCase.setParams(
                new AnswerWordUseCase.Params(str, STREAK_LINE_MAPPER.revMap(streakLine), mCurrUsedWord, reverseMatching));

        mCaseExecutor.execute(mAnswerWordUseCase, new UseCase.Callback<AnswerWordUseCase.Result>() {
            @Override
            public void onSuccess(AnswerWordUseCase.Result result) {
                if (result.mCorrect && result.mUsedWord != null) {
                    mAnsweredWordsCount++;
                    mView.showAnsweredWordsCount(mAnsweredWordsCount);
                    mView.wordAnswered(true, result.mUsedWord.getId());
                }
                else {
                    mView.wordAnswered(false, -1);
                }

                if (mAnsweredWordsCount >= mCurrUsedWord.size()) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mView.showFinishGame();
                        }
                    }, 800);
                }
            }

            @Override
            public void onFailed(String errMsg) {}
        });
    }
}
