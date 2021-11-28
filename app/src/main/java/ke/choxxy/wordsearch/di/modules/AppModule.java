package ke.choxxy.wordsearch.di.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ke.choxxy.wordsearch.features.ViewModelFactory;
import ke.choxxy.wordsearch.data.GameDataSource;
import ke.choxxy.wordsearch.data.GameThemeRepository;
import ke.choxxy.wordsearch.data.WordDataSource;
import ke.choxxy.wordsearch.features.gamehistory.GameHistoryViewModel;
import ke.choxxy.wordsearch.features.gameover.GameOverViewModel;
import ke.choxxy.wordsearch.features.gameplay.GamePlayViewModel;
import ke.choxxy.wordsearch.features.mainmenu.MainMenuViewModel;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by abdularis on 18/07/17.
 */

@Module
public class AppModule {

    private Application mApp;

    public AppModule(Application application) {
        mApp = application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return mApp;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    ViewModelFactory provideViewModelFactory(GameDataSource gameDataSource,
                                             WordDataSource wordDataSource) {
        return new ViewModelFactory(
                new GameOverViewModel(gameDataSource),
                new GamePlayViewModel(gameDataSource, wordDataSource),
                new MainMenuViewModel(new GameThemeRepository()),
                new GameHistoryViewModel(gameDataSource)
        );
    }
}
