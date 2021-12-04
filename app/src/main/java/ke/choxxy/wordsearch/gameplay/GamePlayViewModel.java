package ke.choxxy.wordsearch.gameplay;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import dagger.hilt.android.lifecycle.HiltViewModel;
import ke.choxxy.wordsearch.commons.SingleLiveEvent;
import ke.choxxy.wordsearch.commons.Timer;
import ke.choxxy.wordsearch.data.GameDataSource;
import ke.choxxy.wordsearch.data.entity.Game;
import ke.choxxy.wordsearch.data.entity.GameDataMapper;
import ke.choxxy.wordsearch.data.WordDataSource;
import ke.choxxy.wordsearch.data.entity.GameType;
import ke.choxxy.wordsearch.data.sqlite.GameDataSQLiteDataSource;
import ke.choxxy.wordsearch.model.GameData;
import ke.choxxy.wordsearch.model.UsedWord;
import ke.choxxy.wordsearch.model.Word;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


@HiltViewModel
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
    static class Update extends GameState {
        GameData mGameData;
        private Update(GameData gameData) {
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

    private GameDataSQLiteDataSource mGameDataSource;
    private WordDataSource mWordDataSource;
    private GameDataCreator mGameDataCreator;
    private GameData mCurrentGameData;
    private Timer mTimer;
    private CountDownTimer mCountDowntimer;
    private long mCurrentDuration;

    private GameState mCurrentState = null;
    private MutableLiveData<Long> mOnTimer;
    private MutableLiveData<GameState> mOnGameState;
    private SingleLiveEvent<AnswerResult> mOnAnswerResult;
    private boolean timeTrial = false;

    @Inject
    public GamePlayViewModel(GameDataSQLiteDataSource gameDataSource, WordDataSource wordDataSource) {
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

    private void initCountDownTimer(long millisInFuture){
        mCountDowntimer = new CountDownTimer(millisInFuture, TIMER_TIMEOUT) {
            @Override
            public void onTick(long millisUntilFinished) {
                mOnTimer.setValue(millisUntilFinished/ 1000);
            }
            @Override
            public void onFinish() {

            }
        };
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

    private void startTimer(){
        if (timeTrial)
            mCountDowntimer.start();
        else
            mTimer.start();
    }

    @SuppressLint("CheckResult")
    public void generateNewGameRound(int rowCount, int colCount, Game game) {
        if (!(mCurrentState instanceof Generating)) {
            setGameState(new Generating(rowCount, colCount, "Play me"));

            if(game.getType() == GameType.TIME_TRIAL) {
                initCountDownTimer(300000);
                timeTrial = true;
            }

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
                        mCurrentDuration  = 0;
                        startTimer();
                        mCurrentGameData = gameRound;
                        setGameState(new Playing(mCurrentGameData));
                    });
        }
    }

    public void answerWord(String answerStr, UsedWord.AnswerLine answerLine, boolean reverseMatching) {
        UsedWord correctWord = mCurrentGameData.markWordAsAnswered(answerStr, answerLine, reverseMatching);

        boolean correct = correctWord != null;
        mOnAnswerResult.setValue(new AnswerResult(correct,
                correctWord != null ? correctWord.getId() : -1));

        if (correct) {
            mGameDataSource.markWordAsAnswered(correctWord);
            setGameState(new Update(mCurrentGameData));

            if (mCurrentGameData.isFinished()) {
                setGameState(new Finished(mCurrentGameData));
                if(timeTrial)
                    mCountDowntimer.cancel();
                else
                    mTimer.stop();
            }
        }
    }

    public LiveData<Long> getOnTimer() {
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
