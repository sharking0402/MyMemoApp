package com.example.hirose.mymemoapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class FormActivity extends AppCompatActivity {

    private long memoId;

    private EditText titleText;
    private EditText bodyText;
    private TextView updatedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        titleText = (EditText)findViewById(R.id.titleText);
        bodyText= (EditText)findViewById(R.id.bodyText);
        updatedText = (TextView)findViewById(R.id.updatedText);


        Intent intent = getIntent();
        memoId = intent.getLongExtra(MainActivity.EXTRA_MYID, 0L);

        if (memoId == 0) {

        } else {
            Uri uri = ContentUris.withAppendedId(
                    MemoContentProvider.CONTENT_URI,
                    memoId
            );
            String[] projection = {
                    MemoContract.Memos.COL_TITLE,
                    MemoContract.Memos.COL_BODY,
                    MemoContract.Memos.COL_UPDATED
            };
            Cursor c = getContentResolver().query(
                    uri, // 問い合わせするContentProviderのContent URI
                    projection, // 取得したい列
                    MemoContract.Memos._ID + " = ?", // 問い合わせ条件
                    new String[] { Long.toString(memoId) }, // 問い合わせ条件の引数
                    null // 問い合わせ結果の並び順（今回はIDを指定して1件取得するので、null）
            );
            // 取得したカーソルの最初のレコードに移動
            c.moveToFirst();

            // 取得したカーソルから、各種Viewに値を設定
            titleText.setText(c.getString(c.getColumnIndex(MemoContract.Memos.COL_TITLE)));
            bodyText.setText(c.getString(c.getColumnIndex(MemoContract.Memos.COL_BODY)));
            updatedText.setText("Updated: " + c.getString(c.getColumnIndex(MemoContract.Memos.COL_UPDATED)));

            // カーソルをクローズ
            c.close();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_form, menu);
        return true;
    }

    private void deleteMemo() {

    }

    private void saveMemo() {

        // 現在のメモ情報を取得
        String title = titleText.getText().toString().trim();
        String body = bodyText.getText().toString().trim();
        String updated =
                new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.US)
                .format(new Date());

        // 必須項目（title）が入力されているかチェック
        if (title.isEmpty()){
            // 未入力（ブランク）だった場合はトーストでエラーメッセージを表示
            Toast.makeText(
                    FormActivity.this,
                    "Please enter title",
                    Toast.LENGTH_LONG
            ).show();
        } else {
            // 入力されている（非ブランク）だった場合は、ContentValuesを使って値を更新
            ContentValues values = new ContentValues();
            values.put(MemoContract.Memos.COL_TITLE, title);
            values.put(MemoContract.Memos.COL_BODY, body);
            values.put(MemoContract.Memos.COL_UPDATED, updated);
            if (memoId == 0L) {
                // memoIdがない場合は、追加（Insert）
            } else {
                // memoIdがある場合は、更新（Update）
                Uri uri = ContentUris.withAppendedId(
                        MemoContentProvider.CONTENT_URI,
                        memoId
                );
                getContentResolver().update(
                        uri,
                        values,
                        MemoContract.Memos._ID + " = ?",
                        new String[] { Long.toString(memoId) }
                );
            }
            finish();

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                deleteMemo();
            case R.id.action_save:
                saveMemo();

        }

        return super.onOptionsItemSelected(item);
    }
}
