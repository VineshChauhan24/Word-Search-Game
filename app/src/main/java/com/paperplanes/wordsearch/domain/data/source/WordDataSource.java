package com.paperplanes.wordsearch.domain.data.source;

import com.paperplanes.wordsearch.domain.model.Word;

import java.util.List;

/**
 * Created by abdularis on 18/07/17.
 */

public interface WordDataSource {

    interface Callback {

        void onWordsLoaded(List<Word> words);

    }

    void getWords(Callback callback);

}
