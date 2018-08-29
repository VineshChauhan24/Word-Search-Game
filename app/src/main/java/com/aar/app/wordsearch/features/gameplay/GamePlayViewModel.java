package com.aar.app.wordsearch.features.gameplay;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.aar.app.wordsearch.commons.SingleLiveEvent;
import com.aar.app.wordsearch.commons.Timer;
import com.aar.app.wordsearch.data.GameDataSource;
import com.aar.app.wordsearch.data.entity.GameDataMapper;
import com.aar.app.wordsearch.data.WordDataSource;
import com.aar.app.wordsearch.model.GameData;
import com.aar.app.wordsearch.model.UsedWord;
import com.aar.app.wordsearch.model.Word;

import java.util.List;

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
        GameData mGameData;
        private Finished(GameData gameData) {
            this.mGameData = gameData;
        }
    }
    static class Paused extends GameState {
        private Paused() {}
    }
    static class Playing extends GameState {
        GameData mGameData;
        private Playing(GameData gameData) {
            this.mGameData = gameData;
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

    private GameDataSource mGameDataSource;
    private WordDataSource mWordDataSource;
    private GameDataCreator mGameDataCreator;
    private GameData mCurrentGameData;
    private Timer mTimer;
    private int mCurrentDuration;

    private GameState mCurrentState = null;
    private MutableLiveData<Integer> mOnTimer;
    private MutableLiveData<GameState> mOnGameState;
    private SingleLiveEvent<AnswerResult> mOnAnswerResult;

    public GamePlayViewModel(GameDataSource gameDataSource, WordDataSource wordDataSource) {
        mGameDataSource = gameDataSource;
        mWordDataSource = wordDataSource;
        mGameDataCreator = new GameDataCreator();

        mTimer = new Timer(TIMER_TIMEOUT);
        mTimer.addOnTimeoutListener(elapsedTime -> {
            mOnTimer.setValue(mCurrentDuration++);
            mGameDataSource.saveGameDataDuration(mCurrentGameData.getId(), mCurrentDuration);
        });
        resetLiveData();
    }

    private void resetLiveData() {
        mOnTimer = new MutableLiveData<>();
        mOnGameState = new MutableLiveData<>();
        mOnAnswerResult = new SingleLiveEvent<>();
    }

    public void stopGame() {
        mCurrentGameData = null;
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
            setGameState(new Playing(mCurrentGameData));
        }
    }

    public void loadGameRound(int gid) {
        if (!(mCurrentState instanceof Generating)) {
            setGameState(new Loading(gid));

            mGameDataSource.getGameData(gid, gameRound -> {
                mCurrentGameData = new GameDataMapper().map(gameRound);
                mCurrentDuration = mCurrentGameData.getDuration();
                if (!mCurrentGameData.isFinished())
                    mTimer.start();
                setGameState(new Playing(mCurrentGameData));
            });
        }
    }

    @SuppressLint("CheckResult")
    public void generateNewGameRound(int rowCount, int colCount) {
        if (!(mCurrentState instanceof Generating)) {
            setGameState(new Generating(rowCount, colCount, "Play me"));

            Observable.create((ObservableOnSubscribe<GameData>) emitter -> {
                List<Word> wordList = mWordDataSource.getWords();
                GameData gr = mGameDataCreator.newGameData(wordList, rowCount, colCount, "Play me");
                long gid = mGameDataSource.saveGameData(new GameDataMapper().revMap(gr));
                gr.setId((int) gid);
                emitter.onNext(gr);
                emitter.onComplete();
            }).subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(gameRound -> {
                        mCurrentDuration = 0;
                        mTimer.start();
                        mCurrentGameData = gameRound;
                        setGameState(new Playing(mCurrentGameData));
                    });
        }
    }

    public void answerWord(String answerStr, UsedWord.AnswerLine answerLine, boolean reverseMatching) {
        UsedWord correctWord = mCurrentGameData.markWordAsAnswered(answerStr, answerLine, reverseMatching);

        boolean correct = correctWord != null;
        mOnAnswerResult.setValue(new AnswerResult(correct, correctWord != null ? correctWord.getId() : -1));
        if (correct) {
            mGameDataSource.markWordAsAnswered(correctWord);
            if (mCurrentGameData.isFinished()) {
                setGameState(new Finished(mCurrentGameData));
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
