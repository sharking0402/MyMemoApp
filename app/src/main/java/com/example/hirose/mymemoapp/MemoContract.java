package com.example.hirose.mymemoapp;

import android.provider.BaseColumns;

/**
 * Created by hirose on 2015/05/28.
 */
public final class MemoContract {

    public MemoContract() {}

    public static abstract class Memos implements BaseColumns {

        // �e�[�u����
        public static final String TABLE_NAME = "memos";

        // ��
        public static final String COL_TITLE = "title";
        public static final String COL_BODY = "body";
        public static final String COL_CREATED = "created";
        public static final String COL_UPDATED = "updated";

    }
}
