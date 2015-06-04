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
                    uri, // �₢���킹����ContentProvider��Content URI
                    projection, // �擾��������
                    MemoContract.Memos._ID + " = ?", // �₢���킹����
                    new String[] { Long.toString(memoId) }, // �₢���킹�����̈���
                    null // �₢���킹���ʂ̕��я��i�����ID���w�肵��1���擾����̂ŁAnull�j
            );
            // �擾�����J�[�\���̍ŏ��̃��R�[�h�Ɉړ�
            c.moveToFirst();

            // �擾�����J�[�\������A�e��View�ɒl��ݒ�
            titleText.setText(c.getString(c.getColumnIndex(MemoContract.Memos.COL_TITLE)));
            bodyText.setText(c.getString(c.getColumnIndex(MemoContract.Memos.COL_BODY)));
            updatedText.setText("Updated: " + c.getString(c.getColumnIndex(MemoContract.Memos.COL_UPDATED)));

            // �J�[�\�����N���[�Y
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

        // ���݂̃��������擾
        String title = titleText.getText().toString().trim();
        String body = bodyText.getText().toString().trim();
        String updated =
                new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.US)
                .format(new Date());

        // �K�{���ځititle�j�����͂���Ă��邩�`�F�b�N
        if (title.isEmpty()){
            // �����́i�u�����N�j�������ꍇ�̓g�[�X�g�ŃG���[���b�Z�[�W��\��
            Toast.makeText(
                    FormActivity.this,
                    "Please enter title",
                    Toast.LENGTH_LONG
            ).show();
        } else {
            // ���͂���Ă���i��u�����N�j�������ꍇ�́AContentValues���g���Ēl���X�V
            ContentValues values = new ContentValues();
            values.put(MemoContract.Memos.COL_TITLE, title);
            values.put(MemoContract.Memos.COL_BODY, body);
            values.put(MemoContract.Memos.COL_UPDATED, updated);
            if (memoId == 0L) {
                // memoId���Ȃ��ꍇ�́A�ǉ��iInsert�j
            } else {
                // memoId������ꍇ�́A�X�V�iUpdate�j
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
