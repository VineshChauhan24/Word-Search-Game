package com.aar.app.wordsearch.gameplay;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.aar.app.wordsearch.commons.SingleLiveEvent;
import com.aar.app.wordsearch.commons.Timer;
import com.aar.app.wordsearch.domain.data.mapper.GameRoundMapper;
import com.aar.app.wordsearch.domain.data.source.GameRoundDataSource;
import com.aar.app.wordsearch.domain.data.source.WordDataSource;
import com.aar.app.wordsearch.domain.model.GameRound;
import com.aar.app.wordsearch.domain.model.UsedWord;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class GamePlayViewModel extends ViewModel {

    private static final int TIMER_TIMEOUT = 1000;

    static abstract class GameState {}
    static class Generating extends GameState {
        int rowCount;
        int colCount;
        String name;
        private Generating(int rowCount, int colCount, String name) {
            this.rowCount = rowCount;
            this.colCount = colCount;
            this.name = name;
        }
    }
    static class Loading extends GameState {
        int gid;
        private Loading(int gid) {
            this.gid = gid;
        }
    }
    static class Finished extends GameState {
        GameRound gameRound;
        private Finished(GameRound gameRound) {
            this.gameRound = gameRound;
        }
    }
    static class Paused extends GameState {
        private Paused() {}
    }
    static class Playing extends GameState {
        GameRound gameRound;
        private Playing(GameRound gameRound) {
            this.gameRound = gameRound;
        }
    }

    static class AnswerResult {
        public boolean correct;
        public int usedWordId;
        AnswerResult(boolean correct, int usedWordId) {
            this.correct = correct;
            this.usedWordId = usedWordId;
        }
    }

    private GameRoundDataSource mGameRoundDataSource;
    private RandomGameRoundBuilder mGameRoundBuilder;
    private GameRound mCurrentGameRound;
    private Timer mTimer;
    private int mCurrentDuration;

    private GameState mCurrentState = null;
    private MutableLiveData<Integer> mOnTimer;
    private MutableLiveData<GameState> mOnGameState;
    private SingleLiveEvent<AnswerResult> mOnAnswerResult;

    public GamePlayViewModel(GameRoundDataSource gameRoundDataSource, WordDataSource wordDataSource) {
        mGameRoundDataSource = gameRoundDataSource;
        mGameRoundBuilder = new RandomGameRoundBuilder(mGameRoundDataSource, wordDataSource);

        mTimer = new Timer(TIMER_TIMEOUT);
        mTimer.addOnTimeoutListener(elapsedTime -> {
            mOnTimer.setValue(mCurrentDuration++);
            mGameRoundDataSource.saveGameRoundDuration(mCurrentGameRound.getInfo().getId(), mCurrentDuration);
        });
        resetLiveData();
    }

    private void resetLiveData() {
        mOnTimer = new MutableLiveData<>();
        mOnGameState = new MutableLiveData<>();
        mOnAnswerResult = new SingleLiveEvent<>();
    }

    public void stopGame() {
        mCurrentGameRound = null;
        mTimer.stop();
        resetLiveData();
    }

    public void pauseGame() {
        mTimer.stop();
        setGameState(new Paused());
    }

    public void resumeGame() {
        if (mCurrentState instanceof Paused) {
            mTimer.start();
            setGameState(new Playing(mCurrentGameRound));
        }
    }

    public void loadGameRound(int gid) {
        if (!(mCurrentState instanceof Generating)) {
            setGameState(new Loading(gid));

            mGameRoundDataSource.getGameRound(gid, gameRound -> {
                mCurrentGameRound = new GameRoundMapper().map(gameRound);
                mCurrentDuration = mCurrentGameRound.getInfo().getDuration();
                if (!mCurrentGameRound.isFinished())
                    mTimer.start();
                setGameState(new Playing(mCurrentGameRound));
            });
        }
    }

    @SuppressLint("CheckResult")
    public void generateNewGameRound(int rowCount, int colCount) {
        if (!(mCurrentState instanceof Generating)) {
            setGameState(new Generating(rowCount, colCount, "Play me"));

            Observable.create((ObservableOnSubscribe<GameRound>) emitter -> {
                GameRound gr =
                        mGameRoundBuilder.createNewGameRound(rowCount, colCount, "Play me");
                emitter.onNext(gr);
                emitter.onComplete();
            }).subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(gameRound -> {
                        mCurrentDuration = 0;
                        mTimer.start();
                        mCurrentGameRound = gameRound;
                        setGameState(new Playing(mCurrentGameRound));
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
                setGameState(new Finished(mCurrentGameRound));
            }
        }
    }

    public LiveData<Integer> getOnTimer() {
        return mOnTimer;
    }

    public LiveData<GameState> getOnGameState() {
        return mOnGameState;
    }

    public LiveData<AnswerResult> getOnAnswerResult() {
        return mOnAnswerResult;
    }

    private void setGameState(GameState state) {
        mCurrentState = state;
        mOnGameState.setValue(mCurrentState);
    }
}
