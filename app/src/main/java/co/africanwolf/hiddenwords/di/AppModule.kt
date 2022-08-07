package co.africanwolf.hiddenwords.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import co.africanwolf.hiddenwords.ViewModelFactory
import co.africanwolf.hiddenwords.data.GameThemeRepository
import co.africanwolf.hiddenwords.data.WordDataSource
import co.africanwolf.hiddenwords.data.sqlite.DbHelper
import co.africanwolf.hiddenwords.data.sqlite.GameDataSQLiteDataSource
import co.africanwolf.hiddenwords.gamehistory.GameHistoryViewModel
import co.africanwolf.hiddenwords.gameover.GameOverViewModel
import co.africanwolf.hiddenwords.gameplay.GamePlayViewModel
import co.africanwolf.hiddenwords.mainmenu.MainMenuViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("word-search", MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideViewModelFactory(
        gameDataSource: GameDataSQLiteDataSource,
        wordDataSource: WordDataSource
    ): ViewModelFactory {
        return ViewModelFactory(
            GameOverViewModel(gameDataSource),
            GamePlayViewModel(gameDataSource, wordDataSource),
            MainMenuViewModel(GameThemeRepository()),
            GameHistoryViewModel(gameDataSource)
        )
    }

    @Provides
    @Singleton
    fun provideDbHelper(@ApplicationContext context: Context?): DbHelper {
        return DbHelper(context)
    }
}
