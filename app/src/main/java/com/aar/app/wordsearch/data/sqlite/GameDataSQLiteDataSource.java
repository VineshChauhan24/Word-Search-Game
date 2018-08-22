package com.aar.app.wordsearch.data.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aar.app.wordsearch.data.entity.GameDataEntity;
import com.aar.app.wordsearch.data.GameDataSource;
import com.aar.app.wordsearch.model.GameData;
import com.aar.app.wordsearch.model.GameDataStatistic;
import com.aar.app.wordsearch.model.UsedWord;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by abdularis on 18/07/17.
 */

public class GameDataSQLiteDataSource implements GameDataSource {

    private DbHelper mHelper;

    @Inject
    public GameDataSQLiteDataSource(DbHelper helper) {
        mHelper = helper;
    }

    @Override
    public void getGameRound(int gid, GameRoundCallback callback) {
        SQLiteDatabase db = mHelper.getReadableDatabase();

        String cols[] = {
                DbContract.GameRound._ID,
                DbContract.GameRound.COL_NAME,
                DbContract.GameRound.COL_DURATION,
                DbContract.GameRound.COL_GRID_ROW_COUNT,
                DbContract.GameRound.COL_GRID_COL_COUNT,
                DbContract.GameRound.COL_GRID_DATA
        };
        String sel = DbContract.GameRound._ID + "=?";
        String selArgs[] = {String.valueOf(gid)};

        Cursor c = db.query(DbContract.GameRound.TABLE_NAME, cols, sel, selArgs, null, null, null);
        GameDataEntity ent = null;
        if (c.moveToFirst()) {
            GameData.Info info = new GameData.Info();
            info.setId(c.getInt(0));
            info.setName(c.getString(1));
            info.setDuration(c.getInt(2));

            ent = new GameDataEntity();
            ent.setInfo(info);
            ent.setGridRowCount(c.getInt(3));
            ent.setGridColCount(c.getInt(4));
            ent.setGridData(c.getString(5));
            ent.setUsedWords(getUsedWords(gid));
        }
        c.close();

        callback.onLoaded(ent);
    }

    @Override
    public void getGameRoundInfos(InfosCallback callback) {
        SQLiteDatabase db = mHelper.getReadableDatabase();

        String cols[] = {
                DbContract.GameRound._ID,
                DbContract.GameRound.COL_NAME,
                DbContract.GameRound.COL_DURATION
        };
        String order = DbContract.GameRound._ID + " DESC";

        Cursor c = db.query(DbContract.GameRound.TABLE_NAME, cols, null, null, null, null, order);

        List<GameData.Info> infoList = new ArrayList<>();
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {

                GameData.Info info = new GameData.Info();
                info.setId(c.getInt(0));
                info.setName(c.getString(1));
                info.setDuration(c.getInt(2));
                infoList.add(info);

                c.moveToNext();
            }
        }
        c.close();

        callback.onLoaded(infoList);
    }

    @Override
    public void getGameRoundStat(int gid, StatCallback callback) {
        String subQ = "(SELECT COUNT(*) FROM " + DbContract.UsedWord.TABLE_NAME + " WHERE " +
                DbContract.UsedWord.COL_GAME_ROUND_ID + "=" + gid + ")";
        String q = "SELECT " +
                DbContract.GameRound.COL_NAME + "," +
                DbContract.GameRound.COL_DURATION + "," +
                DbContract.GameRound.COL_GRID_ROW_COUNT + "," +
                DbContract.GameRound.COL_GRID_COL_COUNT + "," +
                subQ +
                " FROM " + DbContract.GameRound.TABLE_NAME + " WHERE " + DbContract.GameRound._ID +
                "=" + gid;

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor c = db.rawQuery(q, null);
        if (c.moveToFirst()) {
            GameDataStatistic stat = new GameDataStatistic();
            stat.setName(c.getString(0));
            stat.setDuration(c.getInt(1));
            stat.setGridRowCount(c.getInt(2));
            stat.setGridColCount(c.getInt(3));
            stat.setUsedWordCount(c.getInt(4));
            callback.onLoaded(stat);
        }
        c.close();

    }

    @Override
    public void saveGameRound(GameDataEntity gameRound) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbContract.GameRound.COL_NAME, gameRound.getInfo().getName());
        values.put(DbContract.GameRound.COL_DURATION, gameRound.getInfo().getDuration());
        values.put(DbContract.GameRound.COL_GRID_ROW_COUNT, gameRound.getGridRowCount());
        values.put(DbContract.GameRound.COL_GRID_COL_COUNT, gameRound.getGridColCount());
        values.put(DbContract.GameRound.COL_GRID_DATA, gameRound.getGridData());

        long gid = db.insert(DbContract.GameRound.TABLE_NAME, "null", values);
        gameRound.getInfo().setId((int) gid);

        for (UsedWord usedWord : gameRound.getUsedWords()) {
            values.clear();
            values.put(DbContract.UsedWord.COL_GAME_ROUND_ID, gid);
            values.put(DbContract.UsedWord.COL_WORD_STRING, usedWord.getString());
            values.put(DbContract.UsedWord.COL_IS_MYSTERY, usedWord.isMystery() ? "true" : "false");
            values.put(DbContract.UsedWord.COL_REVEAL_COUNT, usedWord.getRevealCount());
            if (usedWord.getAnswerLine() != null) {
                values.put(DbContract.UsedWord.COL_ANSWER_LINE_DATA, usedWord.getAnswerLine().toString());
                values.put(DbContract.UsedWord.COL_LINE_COLOR, usedWord.getAnswerLine().color);
            }

            long insertedId = db.insert(DbContract.UsedWord.TABLE_NAME, "null", values);
            usedWord.setId((int) insertedId);
        }
    }

    @Override
    public void deleteGameRound(int gid) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sel = DbContract.GameRound._ID + "=?";
        String selArgs[] = {String.valueOf(gid)};

        db.delete(DbContract.GameRound.TABLE_NAME, sel, selArgs);

        sel = DbContract.UsedWord.COL_GAME_ROUND_ID + "=?";
        db.delete(DbContract.UsedWord.TABLE_NAME, sel, selArgs);
    }

    @Override
    public void deleteGameRounds() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(DbContract.GameRound.TABLE_NAME, null, null);
        db.delete(DbContract.UsedWord.TABLE_NAME, null, null);
    }

    @Override
    public void saveGameRoundDuration(int gid, int newDuration) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbContract.GameRound.COL_DURATION, newDuration);

        String where = DbContract.GameRound._ID + "=?";
        String whereArgs[] = {String.valueOf(gid)};

        db.update(DbContract.GameRound.TABLE_NAME, values, where, whereArgs);
    }

    @Override
    public void markWordAsAnswered(UsedWord usedWord) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DbContract.UsedWord.COL_ANSWER_LINE_DATA, usedWord.getAnswerLine().toString());
        values.put(DbContract.UsedWord.COL_LINE_COLOR, usedWord.getAnswerLine().color);

        String where = DbContract.UsedWord._ID + "=?";
        String whereArgs[] = {String.valueOf(usedWord.getId())};

        db.update(DbContract.UsedWord.TABLE_NAME, values, where, whereArgs);
    }

    private List<UsedWord> getUsedWords(int gid) {
        SQLiteDatabase db = mHelper.getReadableDatabase();

        String cols[] = {
                DbContract.UsedWord._ID,
                DbContract.UsedWord.COL_WORD_STRING,
                DbContract.UsedWord.COL_ANSWER_LINE_DATA,
                DbContract.UsedWord.COL_LINE_COLOR,
                DbContract.UsedWord.COL_IS_MYSTERY,
                DbContract.UsedWord.COL_REVEAL_COUNT
        };
        String sel = DbContract.UsedWord.COL_GAME_ROUND_ID + "=?";
        String selArgs[] = {String.valueOf(gid)};

        Cursor c = db.query(DbContract.UsedWord.TABLE_NAME, cols, sel, selArgs, null, null, null);

        List<UsedWord> usedWordList = new ArrayList<>();
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                int id = c.getInt(0);
                String str = c.getString(1);
                String lineData = c.getString(2);
                int col = c.getInt(3);

                UsedWord.AnswerLine answerLine = null;
                if (lineData != null) {
                    answerLine = new UsedWord.AnswerLine();
                    answerLine.fromString(lineData);
                    answerLine.color = col;
                }

                UsedWord usedWord = new UsedWord();
                usedWord.setId(id);
                usedWord.setString(str);
                usedWord.setAnswered(lineData != null);
                usedWord.setAnswerLine(answerLine);
                usedWord.setMystery(Boolean.valueOf(c.getString(4)));
                usedWord.setRevealCount(c.getInt(5));

                usedWordList.add(usedWord);

                c.moveToNext();
            }
        }
        c.close();

        return usedWordList;
    }
}
