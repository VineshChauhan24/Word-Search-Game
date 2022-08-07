package co.africanwolf.hiddenwords.di

import android.content.Context
import co.africanwolf.hiddenwords.data.WordDataSource
import co.africanwolf.hiddenwords.data.sqlite.DbHelper
import co.africanwolf.hiddenwords.data.sqlite.GameDataSQLiteDataSource
import co.africanwolf.hiddenwords.data.xml.WordXmlDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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
