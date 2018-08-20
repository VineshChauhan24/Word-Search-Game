package com.aar.app.wordsearch.gameplay.mapper;

import com.aar.app.wordsearch.commons.Mapper;
import com.aar.app.wordsearch.model.UsedWord;
import com.aar.app.wordsearch.gameplay.UsedWordViewModel;

/**
 * Created by abdularis on 18/07/17.
 */

public class UsedWordMapper extends Mapper<UsedWord, UsedWordViewModel> {
    @Override
    public UsedWordViewModel map(UsedWord obj) {
        return new UsedWordViewModel(obj);
    }

    @Override
    public UsedWord revMap(UsedWordViewModel obj) {
        return obj.getUsedWord();
    }
}
