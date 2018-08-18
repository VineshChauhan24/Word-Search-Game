package com.aar.app.wordsearch.di.component;

import com.aar.app.wordsearch.di.modules.AppModule;
import com.aar.app.wordsearch.di.modules.DataSourceModule;
import com.aar.app.wordsearch.FullscreenActivity;
import com.aar.app.wordsearch.gameover.GameOverActivity;
import com.aar.app.wordsearch.gameplay.GamePlayActivity;
import com.aar.app.wordsearch.mainmenu.MainMenuActivity;

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

}
