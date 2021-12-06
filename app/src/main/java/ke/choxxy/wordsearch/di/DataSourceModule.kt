package ke.choxxy.wordsearch.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ke.choxxy.wordsearch.data.WordDataSource
import ke.choxxy.wordsearch.data.sqlite.DbHelper
import ke.choxxy.wordsearch.data.sqlite.GameDataSQLiteDataSource
import ke.choxxy.wordsearch.data.xml.WordXmlDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideGameRoundDataSource(dbHelper: DbHelper): GameDataSQLiteDataSource {
        return GameDataSQLiteDataSource(dbHelper)
    }

    //    @Provides
    //    @Singleton
    //    WordDataSource provideWordDataSource(DbHelper dbHelper) {
    //        return new WordSQLiteDataSource(dbHelper);
    //    }
    @Provides
    @Singleton
    fun provideWordDataSource(@ApplicationContext context: Context): WordDataSource {
        return WordXmlDataSource(context)
    }
}
