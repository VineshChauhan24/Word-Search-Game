package com.aar.app.wordsearch.gameplay;


import com.aar.app.wordsearch.commons.Util;
import com.aar.app.wordsearch.commons.generator.StringListGridGenerator;
import com.aar.app.wordsearch.data.mapper.GameRoundMapper;
import com.aar.app.wordsearch.data.GameRoundDataSource;
import com.aar.app.wordsearch.data.WordDataSource;
import com.aar.app.wordsearch.model.GameRound;
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

public class RandomGameRoundBuilder {

    private GameRoundDataSource mGameRoundDataSource;
    private WordDataSource mWordDataSource;

    public RandomGameRoundBuilder(GameRoundDataSource gameRoundDataSource, WordDataSource wordDataSource) {
        mGameRoundDataSource = gameRoundDataSource;
        mWordDataSource = wordDataSource;
    }

    public GameRound createNewGameRound(final int rowCount, final int colCount, final String name) {
        final GameRound gameRound = new GameRound();

        mWordDataSource.getWords(words -> {
            Util.randomizeList(words);

            Grid grid = new Grid(rowCount, colCount);
            int maxCharCount = Math.min(rowCount, colCount);
            List<String> usedStrings =
                    new StringListGridGenerator().setGrid(getStringListFromWord(words, 100, maxCharCount), grid.getArray());

            gameRound.addUsedWords(buildUsedWordFromString(usedStrings));
            gameRound.setGrid(grid);
            if (name == null || name.isEmpty()) {
                String name1 = "Puzzle " +
                        new SimpleDateFormat("HH.mm.ss", Locale.ENGLISH).format(new Date(System.currentTimeMillis()));
                gameRound.getInfo().setName(name1);
            }
            else {
                gameRound.getInfo().setName(name);
            }

            mGameRoundDataSource.saveGameRound(new GameRoundMapper().revMap(gameRound));
        });

        return gameRound;
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
                uw.setMystery(true);
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
