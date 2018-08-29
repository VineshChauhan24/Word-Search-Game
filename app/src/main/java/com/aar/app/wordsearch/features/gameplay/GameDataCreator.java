package com.aar.app.wordsearch.features.gameplay;


import com.aar.app.wordsearch.commons.Util;
import com.aar.app.wordsearch.commons.generator.StringListGridGenerator;
import com.aar.app.wordsearch.model.GameData;
import com.aar.app.wordsearch.model.Grid;
import com.aar.app.wordsearch.model.UsedWord;
import com.aar.app.wordsearch.model.Word;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by abdularis on 20/07/17.
 */

public class GameDataCreator {

    public GameData newGameData(final List<Word> words,
                                final int rowCount, final int colCount,
                                final String name) {
        final GameData gameData = new GameData();

        Util.randomizeList(words);

        Grid grid = new Grid(rowCount, colCount);
        int maxCharCount = Math.min(rowCount, colCount);
        List<String> usedStrings =
                new StringListGridGenerator()
                        .setGrid(getStringListFromWord(words, 100, maxCharCount), grid.getArray());

        gameData.addUsedWords(buildUsedWordFromString(usedStrings));
        gameData.setGrid(grid);
        if (name == null || name.isEmpty()) {
            String name1 = "Puzzle " +
                    new SimpleDateFormat("HH.mm.ss", Locale.ENGLISH)
                            .format(new Date(System.currentTimeMillis()));
            gameData.setName(name1);
        }
        else {
            gameData.setName(name);
        }
        return gameData;
    }

    private List<UsedWord> buildUsedWordFromString(List<String> strings) {
        int mysteryWordCount = Util.getRandomIntRange(strings.size() / 2, strings.size());
        List<UsedWord> usedWords = new ArrayList<>();
        for (int i = 0; i < strings.size(); i++) {
            String str = strings.get(i);

            UsedWord uw = new UsedWord();
            uw.setString(str);
            uw.setAnswered(false);
            if (mysteryWordCount > 0) {
                uw.setIsMystery(true);
                uw.setRevealCount(Util.getRandomIntRange(0, str.length() - 1));
                mysteryWordCount--;
            }

            usedWords.add(uw);
        }

        Util.randomizeList(usedWords);
        return usedWords;
    }

    private List<String> getStringListFromWord(List<Word> words, int count, int maxCharCount) {
        count = Math.min(count, words.size());

        List<String> stringList = new ArrayList<>();
        String temp;
        for (int i = 0; i < words.size(); i++) {
            if (stringList.size() >= count) break;

            temp = words.get(i).getString();
            if (temp.length() <= maxCharCount) {
                stringList.add(temp);
            }
        }

        return stringList;
    }
}
