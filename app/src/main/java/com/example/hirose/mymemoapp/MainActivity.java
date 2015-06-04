package com.example.hirose.mymemoapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter adapter;

    public final static String EXTRA_MYID = "com.example.hirose.mymemoapp.MYID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] from = {
                MemoContract.Memos.COL_TITLE,
                MemoContract.Memos.COL_UPDATED
        };

        int[] to = {
                android.R.id.text1,
                android.R.id.text2
        };

        // Adapterの生成
        adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                null, // Cursor (CursorLoaderが作成するのでnull)
                from, // どのフィールドとデータを紐付けるかをFrom~Toで指定
                to,
                0 // Adapterの動作を規定するフラグ
        );

        ListView myListView = (ListView)findViewById(R.id.myListView);
        myListView.setAdapter(adapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, FormActivity.class);
                intent.putExtra(EXTRA_MYID, id);
                startActivity(intent);
            }
        });

        // Loaderの初期化
        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Loaderを作った時に実行されるQueryを返却する

        String[] projection = {
                MemoContract.Memos._ID,
                MemoContract.Memos.COL_TITLE,
                MemoContract.Memos.COL_UPDATED
        };

        return new CursorLoader(
                this,
                MemoContentProvider.CONTENT_URI,
                projection,
                null,
                null,
                MemoContract.Memos.COL_UPDATED + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // ContentProviderからデータが返ってきたときに呼ばれるメソッド

        // Adapterに返ってきたデータを設定
        adapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // 何らかの理由でローダーがリセットされた時に呼ばれるメソッド

        // AdapterのCoursorを初期化
        adapter.swapCursor(null);

    }
}
