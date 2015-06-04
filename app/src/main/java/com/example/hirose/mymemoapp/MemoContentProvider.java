package com.example.hirose.mymemoapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MemoContentProvider extends ContentProvider {

    // Authority�����ContentUri���`
    public static final String AUTHORITY = "com.example.hirose.mymemoapp.MemoContentProvider";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + MemoContract.Memos.TABLE_NAME);

    // UriMatcher���쐬���AContentUri�̃}�b�`���O�`�F�b�N���s����悤�ɂ���
    private static final int MEMOS = 1;
    private static final int MEMO_ITEM = 2;
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // ����̃��R�[�h���w�肵�Ȃ��ꍇ��Uri�́ACode 1
        uriMatcher.addURI(AUTHORITY, MemoContract.Memos.TABLE_NAME, MEMOS);
        // ����̃��R�[�h���w�肷��iPK��ID��#���ʂƂȂ�j�ꍇ��Uri�́ACode 2
        uriMatcher.addURI(AUTHORITY, MemoContract.Memos.TABLE_NAME + "/#", MEMO_ITEM);
    }

    // ContentProvider��CRUD����ۂɎg�p����Memos�e�[�u���pOpenHelper
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
        // UriMatcher���g����Uri�̃}�b�`���O���`�F�b�N
        switch (uriMatcher.match(uri)){
            case MEMOS:
            case MEMO_ITEM:
                break;
            default:
                throw new IllegalArgumentException("Invalid URI: " + uri);
        }
        // memoOpenHelper���g����DB�ɐڑ����A�₢���킹�𔭍s�E�ԋp
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
        // �f�[�^�̍X�V���Ď��i�H�j
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // UriMatcher���g����ContentUri�����������`�F�b�N
        if (uriMatcher.match(uri) != MEMO_ITEM) {
            // �}�b�`���O���Ȃ������ꍇ�͗�O���X���[
            throw  new IllegalArgumentException("Invalid URI: " + uri);
        }
        SQLiteDatabase db = memoOpenHelper.getWritableDatabase();
        int updateCount = db.update(
                MemoContract.Memos.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
        // ContentResolver�Ƀf�[�^�̍X�V��ʒm
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }
}
