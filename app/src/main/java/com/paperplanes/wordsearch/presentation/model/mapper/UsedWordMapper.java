package com.paperplanes.wordsearch.presentation.model.mapper;

import com.paperplanes.wordsearch.commons.Mapper;
import com.paperplanes.wordsearch.domain.model.UsedWord;
import com.paperplanes.wordsearch.presentation.model.UsedWordViewModel;

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
