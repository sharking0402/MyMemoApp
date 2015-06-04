package com.example.hirose.mymemoapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MemoContentProvider extends ContentProvider {

    // AuthorityおよびContentUriを定義
    public static final String AUTHORITY = "com.example.hirose.mymemoapp.MemoContentProvider";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + MemoContract.Memos.TABLE_NAME);

    // UriMatcherを作成し、ContentUriのマッチングチェックを行えるようにする
    private static final int MEMOS = 1;
    private static final int MEMO_ITEM = 2;
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // 特定のレコードを指定しない場合のUriは、Code 1
        uriMatcher.addURI(AUTHORITY, MemoContract.Memos.TABLE_NAME, MEMOS);
        // 特定のレコードを指定する（PKのID列が#部位となる）場合のUriは、Code 2
        uriMatcher.addURI(AUTHORITY, MemoContract.Memos.TABLE_NAME + "/#", MEMO_ITEM);
    }

    // ContentProviderがCRUDする際に使用するMemosテーブル用OpenHelper
    private MemoOpenHelper memoOpenHelper;

    public MemoContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        memoOpenHelper = new MemoOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // UriMatcherを使ってUriのマッチングをチェック
        switch (uriMatcher.match(uri)){
            case MEMOS:
            case MEMO_ITEM:
                break;
            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }
        // memoOpenHelperを使ってDBに接続し、問い合わせを発行・返却
        SQLiteDatabase db = memoOpenHelper.getWritableDatabase();
        Cursor c = db.query(
                MemoContract.Memos.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        // データの更新を監視（？）
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // UriMatcherを使ってContentUriが正しいかチェック
        if (uriMatcher.match(uri) != MEMO_ITEM) {
            // マッチングしなかった場合は例外をスロー
            throw  new IllegalArgumentException("Invalid URI: " + uri);
        }
        SQLiteDatabase db = memoOpenHelper.getWritableDatabase();
        int updateCount = db.update(
                MemoContract.Memos.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
        // ContentResolverにデータの更新を通知
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }
}
