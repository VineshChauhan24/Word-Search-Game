package ke.choxxy.wordsearch.di.component;

import ke.choxxy.wordsearch.di.modules.AppModule;
import ke.choxxy.wordsearch.di.modules.DataSourceModule;
import ke.choxxy.wordsearch.features.FullscreenActivity;
import ke.choxxy.wordsearch.features.gamehistory.GameHistoryActivity;
import ke.choxxy.wordsearch.features.gameover.GameOverActivity;
import ke.choxxy.wordsearch.features.gameplay.GamePlayActivity;
import ke.choxxy.wordsearch.features.mainmenu.MainMenuActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by abdularis on 18/07/17.
 */

@Singleton
@Component(modules = {AppModule.class, DataSourceModule.class})
public interface AppComponent {

    void inject(GamePlayActivity activity);

    void inject(MainMenuActivity activity);

    void inject(GameOverActivity activity);

    void inject(FullscreenActivity activity);

    void inject(GameHistoryActivity activity);

}
