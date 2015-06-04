package com.example.hirose.mymemoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hirose on 2015/05/28.
 */
public class MemoOpenHelper extends SQLiteOpenHelper {

    // DBの名前とバージョン
    public static final String DB_NAME = "myapp.db";
    public static final int DB_VERSION = 3;

    // 発行するSQL定数
    public static final String CREATE_TABLE =
            "create table memos (" +
                    "_id integer primary key autoincrement, " +
                    "title text, " +
                    "body text, " +
                    "created datetime default current_timestamp, " +
                    "updated datetime default current_timestamp)";
    public static final String INIT_TABLE =
            "insert into memos (title, body) values " +
            "('title1', 'body1'), " +
            "('title2', 'body2'), " +
            "('title3', 'body3')";
    public static final String DROP_TABLE =
            "drop table if exists " + MemoContract.Memos.TABLE_NAME;

    public MemoOpenHelper(Context c){
        super(c, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(INIT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
