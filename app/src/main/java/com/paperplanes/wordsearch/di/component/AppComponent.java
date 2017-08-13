package com.paperplanes.wordsearch.di.component;

import com.paperplanes.wordsearch.di.modules.AppModule;
import com.paperplanes.wordsearch.di.modules.DataSourceModule;
import com.paperplanes.wordsearch.presentation.ui.activity.FullscreenActivity;
import com.paperplanes.wordsearch.presentation.ui.activity.FinishActivity;
import com.paperplanes.wordsearch.presentation.ui.activity.GamePlayActivity;
import com.paperplanes.wordsearch.presentation.ui.activity.MainMenuActivity;

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
