package com.aar.app.wordsearch.gameplay;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.aar.app.wordsearch.commons.SingleLiveEvent;
import com.aar.app.wordsearch.commons.Timer;
import com.aar.app.wordsearch.domain.data.mapper.GameRoundMapper;
import com.aar.app.wordsearch.domain.data.source.GameRoundDataSource;
import com.aar.app.wordsearch.domain.model.GameRound;
import com.aar.app.wordsearch.domain.model.UsedWord;


public class GamePlayViewModel extends ViewModel {

    private static final int TIMER_TIMEOUT = 1000;

    public enum GameState {
        NONE,
        GENERATING,
        FINISHED,
        PAUSED,
        PLAYING
    }

    public static class AnswerResult {
        public boolean correct;
        public int usedWordId;
        AnswerResult(boolean correct, int usedWordId) {
            this.correct = correct;
            this.usedWordId = usedWordId;
        }
    }

    private GameRoundDataSource mGameRoundDataSource;
    private GameRound mCurrentGameRound;
    private Timer mTimer;
    private int mCurrentDuration;

    private GameState mCurrentState = GameState.NONE;
    private MutableLiveData<Integer> mOnTimer = new MutableLiveData<>();
    private MutableLiveData<GameState> mOnGameState = new MutableLiveData<>();
    private MutableLiveData<GameRound> mOnGameRoundLoaded = new MutableLiveData<>();
    private SingleLiveEvent<AnswerResult> mOnAnswerResult = new SingleLiveEvent<>();

    public GamePlayViewModel(GameRoundDataSource gameRoundDataSource) {
        mGameRoundDataSource = gameRoundDataSource;

        mTimer = new Timer(TIMER_TIMEOUT);
        mTimer.addOnTimeoutListener(elapsedTime -> {
            mOnTimer.setValue(mCurrentDuration++);
            mGameRoundDataSource.saveGameRoundDuration(mCurrentGameRound.getInfo().getId(), mCurrentDuration);
        });
    }

    public void pauseGame() {
        mTimer.stop();
        setGameState(GameState.PAUSED);
    }

    public void resumeGame() {
        if (mCurrentState == GameState.PAUSED) {
            mTimer.start();
            setGameState(GameState.PLAYING);
        }
    }

    public void loadGameRound(int gid) {
        if (mCurrentState != GameState.GENERATING) {
            setGameState(GameState.GENERATING);

            mGameRoundDataSource.getGameRound(gid, gameRound -> {
                mCurrentGameRound = new GameRoundMapper().map(gameRound);

                mCurrentDuration = mCurrentGameRound.getInfo().getDuration();
                mOnGameRoundLoaded.setValue(mCurrentGameRound);
                if (!mCurrentGameRound.isFinished())
                    mTimer.start();
                setGameState(GameState.PLAYING);
            });
        }
    }

    public void answerWord(String answerStr, UsedWord.AnswerLine answerLine, boolean reverseMatching) {
        UsedWord correctWord = mCurrentGameRound.markWordAsAnswered(answerStr, answerLine, reverseMatching);

        boolean correct = correctWord != null;
        mOnAnswerResult.setValue(new AnswerResult(correct, correctWord != null ? correctWord.getId() : -1));
        if (correct) {
            mGameRoundDataSource.markWordAsAnswered(correctWord);
            if (mCurrentGameRound.isFinished()) {
                setGameState(GameState.FINISHED);
            }
        }
    }

    public LiveData<Integer> getOnTimer() {
        return mOnTimer;
    }

    public LiveData<GameState> getOnGameState() {
        return mOnGameState;
    }

    public LiveData<GameRound> getOnGameRoundLoaded() {
        return mOnGameRoundLoaded;
    }

    public LiveData<AnswerResult> getOnAnswerResult() {
        return mOnAnswerResult;
    }

    private void setGameState(GameState state) {
        mCurrentState = state;
        mOnGameState.setValue(mCurrentState);
    }
}
