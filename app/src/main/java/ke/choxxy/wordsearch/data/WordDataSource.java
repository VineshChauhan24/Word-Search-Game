package ke.choxxy.wordsearch.data;

import ke.choxxy.wordsearch.model.Word;

import java.util.List;

/**
 * Created by abdularis on 18/07/17.
 */

public interface WordDataSource {
    List<Word> getWords();
}
