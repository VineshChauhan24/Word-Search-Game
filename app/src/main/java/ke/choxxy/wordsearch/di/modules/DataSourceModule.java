package ke.choxxy.wordsearch.di.modules;

import android.content.Context;

import ke.choxxy.wordsearch.data.sqlite.DbHelper;
import ke.choxxy.wordsearch.data.sqlite.GameDataSQLiteDataSource;
import ke.choxxy.wordsearch.data.xml.WordXmlDataSource;
import ke.choxxy.wordsearch.data.GameDataSource;
import ke.choxxy.wordsearch.data.WordDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by abdularis on 18/07/17.
 */

@Module
public class DataSourceModule {

    @Provides
    @Singleton
    DbHelper provideDbHelper(Context context) {
        return new DbHelper(context);
    }

    @Provides
    @Singleton
    GameDataSource provideGameRoundDataSource(DbHelper dbHelper) {
        return new GameDataSQLiteDataSource(dbHelper);
    }

//    @Provides
//    @Singleton
//    WordDataSource provideWordDataSource(DbHelper dbHelper) {
//        return new WordSQLiteDataSource(dbHelper);
//    }

    @Provides
    @Singleton
    WordDataSource provideWordDataSource(Context context) {
        return new WordXmlDataSource(context);
    }

}
