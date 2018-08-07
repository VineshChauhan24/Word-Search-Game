package com.aar.app.wordsearch.presentation.views;

import com.aar.app.wordsearch.presentation.model.UsedWordViewModel;

import java.util.List;

/**
 * Created by abdularis on 18/07/17.
 */

public interface GamePlayView {

    void doneLoadingContent();

    void showLoading(boolean enable);

    void showLetterGrid(char grid[][]);

    void showDuration(int duration);

    void showUsedWords(List<UsedWordViewModel> usedWords);

    void showAnsweredWordsCount(int count);

    void showWordsCount(int count);

    void showFinishGame();

    void setGameAsAlreadyFinished();

    void wordAnswered(boolean correct, int usedWordId);

}
