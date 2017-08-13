package com.paperplanes.wordsearch.domain.usecases;

import com.paperplanes.wordsearch.commons.Util;
import com.paperplanes.wordsearch.domain.data.source.GameRoundDataSource;
import com.paperplanes.wordsearch.domain.model.UsedWord;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by abdularis on 18/07/17.
 */

public class AnswerWordUseCase extends UseCase<AnswerWordUseCase.Params, AnswerWordUseCase.Result> {

    private GameRoundDataSource mDataSource;

    @Inject
    public AnswerWordUseCase(GameRoundDataSource dataSource) {
        mDataSource = dataSource;
    }

    @Override
    protected void execute(Params params) {
        boolean correct = false;
        UsedWord correctWord = null;

        String answerStr = params.mString;
        String answerStrRev = Util.getReverseString(answerStr);
        for (UsedWord word : params.mUsedWords) {

            if (word.isAnswered()) continue;

            String wordStr = word.getString();
            if (wordStr.equalsIgnoreCase( answerStr ) ||
                    (wordStr.equalsIgnoreCase( answerStrRev ) && params.reverseMatching)) {

                correct = true;
                correctWord = word;

                correctWord.setAnswered(true);
                correctWord.setAnswerLine(params.mLine);
                break;
            }
        }

        if (correct) {
            mDataSource.markWordAsAnswered(correctWord);
        }

        getCallback().onSuccess(new Result(correct, correctWord));
    }

    public static class Params implements UseCase.Params {
        public String mString;
        public UsedWord.AnswerLine mLine;
        public List<UsedWord> mUsedWords;
        public boolean reverseMatching;

        public Params(String string, UsedWord.AnswerLine line, List<UsedWord> usedWords, boolean reverseMatching) {
            mString = string;
            mLine = line;
            mUsedWords = usedWords;
            this.reverseMatching = reverseMatching;
        }
    }

    public static class Result implements UseCase.Result {
        public boolean mCorrect;
        public UsedWord mUsedWord;

        public Result(boolean correct, UsedWord usedWord) {
            mCorrect = correct;
            mUsedWord = usedWord;
        }
    }
}
