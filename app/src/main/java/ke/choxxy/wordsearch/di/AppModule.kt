package ke.choxxy.wordsearch.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ke.choxxy.wordsearch.ViewModelFactory
import ke.choxxy.wordsearch.data.GameThemeRepository
import ke.choxxy.wordsearch.data.WordDataSource
import ke.choxxy.wordsearch.data.sqlite.DbHelper
import ke.choxxy.wordsearch.data.sqlite.GameDataSQLiteDataSource
import ke.choxxy.wordsearch.gamehistory.GameHistoryViewModel
import ke.choxxy.wordsearch.gameover.GameOverViewModel
import ke.choxxy.wordsearch.gameplay.GamePlayViewModel
import ke.choxxy.wordsearch.mainmenu.MainMenuViewModel
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
