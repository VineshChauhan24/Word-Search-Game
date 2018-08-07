package com.aar.app.wordsearch.di.component;

import com.aar.app.wordsearch.di.modules.AppModule;
import com.aar.app.wordsearch.di.modules.DataSourceModule;
import com.aar.app.wordsearch.presentation.ui.activity.FullscreenActivity;
import com.aar.app.wordsearch.presentation.ui.activity.FinishActivity;
import com.aar.app.wordsearch.presentation.ui.activity.GamePlayActivity;
import com.aar.app.wordsearch.presentation.ui.activity.MainMenuActivity;

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

    void inject(FinishActivity activity);

    void inject(FullscreenActivity activity);

}
