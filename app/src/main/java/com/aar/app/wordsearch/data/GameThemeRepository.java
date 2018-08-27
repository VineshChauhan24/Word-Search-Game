package com.aar.app.wordsearch.data;

import com.aar.app.wordsearch.model.GameTheme;

import java.util.ArrayList;
import java.util.List;

public class GameThemeRepository {

    public List<GameTheme> getGameThemes() {
        // sample data
        List<GameTheme> themes = new ArrayList<>();
        themes.add(new GameTheme(1, "Fruit"));
        themes.add(new GameTheme(2, "Vegetable"));
        themes.add(new GameTheme(3, "Programming"));
        themes.add(new GameTheme(4, "Food"));
        themes.add(new GameTheme(5, "Animal"));
        themes.add(new GameTheme(5, "Animal 1"));
        themes.add(new GameTheme(5, "Animal 2"));
        themes.add(new GameTheme(5, "Animal 3"));
        themes.add(new GameTheme(5, "Animal 4"));
        themes.add(new GameTheme(5, "Animal 5"));
        themes.add(new GameTheme(5, "Animal 6"));
        return themes;
    }

}
