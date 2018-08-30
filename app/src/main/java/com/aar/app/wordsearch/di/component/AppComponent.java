package com.aar.app.wordsearch.di.component;

import com.aar.app.wordsearch.di.modules.AppModule;
import com.aar.app.wordsearch.di.modules.DataSourceModule;
import com.aar.app.wordsearch.features.FullscreenActivity;
import com.aar.app.wordsearch.features.gamehistory.GameHistoryActivity;
import com.aar.app.wordsearch.features.gameover.GameOverActivity;
import com.aar.app.wordsearch.features.gameplay.GamePlayActivity;
import com.aar.app.wordsearch.features.mainmenu.MainMenuActivity;

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
