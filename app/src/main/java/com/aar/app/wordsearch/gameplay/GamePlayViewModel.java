package com.aar.app.wordsearch.gameplay;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.aar.app.wordsearch.commons.SingleLiveEvent;
import com.aar.app.wordsearch.commons.Timer;
import com.aar.app.wordsearch.commons.Util;
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
        ALREADY_FINISHED,
        PAUSED,
        PLAYING
    }

    public static class AnswerResult {
        public boolean correct;
        public int usedWordId;
        public AnswerResult(boolean correct, int usedWordId) {
            this.correct = correct;
            this.usedWordId = usedWordId;
        }
    }

    private GameRoundDataSource mGameRoundDataSource;
    private GameRound mCurrentGameRound;
    private Timer mTimer;
    private int mCurrentDuration;
    private int mAnsweredWordsCount;

    private GameState mCurrentState = GameState.NONE;
    private MutableLiveData<Integer> mOnTimer = new MutableLiveData<>();
    private MutableLiveData<GameState> mOnGameState = new MutableLiveData<>();
    private MutableLiveData<GameRound> mOnGameRoundLoaded = new MutableLiveData<>();
    private SingleLiveEvent<AnswerResult> mOnAnswerResult = new SingleLiveEvent<>();

    public GamePlayViewModel(GameRoundDataSource gameRoundDataSource) {
        mGameRoundDataSource = gameRoundDataSource;

        mTimer = new Timer(TIMER_TIMEOUT);
        mTimer.addOnTimeoutListener(ellapsedTime -> {
            mOnTimer.setValue(mCurrentDuration++);
            mGameRoundDataSource.saveGameRoundDuration(mCurrentGameRound.getInfo().getId(), mCurrentDuration);
        });
    }

    public void stopGame() {
        mCurrentState = GameState.PAUSED;
        mTimer.stop();
        mOnGameState.setValue(mCurrentState);
    }

    public void resumeGame() {
        if (mCurrentState == GameState.PAUSED) {
            mTimer.start();
            mCurrentState = GameState.PLAYING;
            mOnGameState.setValue(mCurrentState);
        }
    }

    public void loadGameRound(int gid) {
        if (mCurrentState != GameState.GENERATING) {
            mCurrentState = GameState.GENERATING;
            mGameRoundDataSource.getGameRound(gid, gameRound -> {
                mCurrentGameRound = new GameRoundMapper().map(gameRound);

                mAnsweredWordsCount = mCurrentGameRound.getAnsweredWordsCount();
                if (mAnsweredWordsCount >= gameRound.getUsedWords().size()) {
                    mCurrentState = GameState.ALREADY_FINISHED;
                } else {
                    mCurrentState = GameState.PLAYING;
                    mTimer.start();
                }
                mCurrentDuration = gameRound.getInfo().getDuration();
                mOnGameState.setValue(mCurrentState);
                mOnGameRoundLoaded.setValue(mCurrentGameRound);
            });
        }
    }

    public void answerWord(String answerStr, UsedWord.AnswerLine answerLine, boolean reverseMatching) {
        boolean correct = false;
        UsedWord correctWord = null;

        String answerStrRev = Util.getReverseString(answerStr);
        for (UsedWord word : mCurrentGameRound.getUsedWords()) {

            if (word.isAnswered()) continue;

            String wordStr = word.getString();
            if (wordStr.equalsIgnoreCase(answerStr) ||
                    (wordStr.equalsIgnoreCase( answerStrRev ) && reverseMatching)) {

                correct = true;
                correctWord = word;

                correctWord.setAnswered(true);
                correctWord.setAnswerLine(answerLine);
                break;
            }
        }

        mOnAnswerResult.setValue(new AnswerResult(correct, correctWord != null ? correctWord.getId() : -1));
        if (correct) {
            mGameRoundDataSource.markWordAsAnswered(correctWord);
            mAnsweredWordsCount++;
            if (mAnsweredWordsCount >= mCurrentGameRound.getUsedWords().size()) {
                mCurrentState = GameState.FINISHED;
                mOnGameState.setValue(mCurrentState);
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
}
