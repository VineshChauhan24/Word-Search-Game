package co.africanwolf.hiddenwords.data

import co.africanwolf.hiddenwords.model.GameTheme
import java.util.ArrayList
import javax.inject.Inject

class GameThemeRepository @Inject constructor() {
    // sample data
    val gameThemes: List<GameTheme>
        get() {
            // sample data
            val themes: MutableList<GameTheme> = ArrayList()
            themes.add(GameTheme(1, "Fruit"))
            themes.add(GameTheme(2, "Vegetable"))
            themes.add(GameTheme(3, "Programming"))
            themes.add(GameTheme(4, "Food"))
            themes.add(GameTheme(5, "Animal"))
            themes.add(GameTheme(5, "Animal 1"))
            themes.add(GameTheme(5, "Animal 2"))
            themes.add(GameTheme(5, "Animal 3"))
            themes.add(GameTheme(5, "Animal 4"))
            themes.add(GameTheme(5, "Animal 5"))
            themes.add(GameTheme(5, "Animal 6"))
            return themes
        }
}
