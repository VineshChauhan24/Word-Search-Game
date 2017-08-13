package com.paperplanes.wordsearch.di.modules;

import android.content.Context;

import com.paperplanes.wordsearch.data.sqlite.DbHelper;
import com.paperplanes.wordsearch.data.sqlite.GameRoundSQLiteDataSource;
import com.paperplanes.wordsearch.data.sqlite.WordSQLiteDataSource;
import com.paperplanes.wordsearch.data.xml.WordXmlDataSource;
import com.paperplanes.wordsearch.domain.data.source.GameRoundDataSource;
import com.paperplanes.wordsearch.domain.data.source.WordDataSource;

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
    GameRoundDataSource provideGameRoundDataSource(DbHelper dbHelper) {
        return new GameRoundSQLiteDataSource(dbHelper);
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
