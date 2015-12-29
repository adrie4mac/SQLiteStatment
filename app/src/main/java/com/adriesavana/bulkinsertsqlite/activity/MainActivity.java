package com.adriesavana.bulkinsertsqlite.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.adriesavana.bulkinsertsqlite.R;

/**
 * Created by adri on 12/30/15.
 */
public class MainActivity extends Activity implements View.OnClickListener
{
    private static final String SAMPLE_DB_NAME = "MathNerdDB";
    private static final String SAMPLE_TABLE_NAME = "MulitplicationTable";
    private SQLiteDatabase sampleDB;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDB();
        findViewById(R.id.standard_insert_button).setOnClickListener(this);
        findViewById(R.id.bulk_insert_button).setOnClickListener(this);
    }

    private void initDB()
    {
        sampleDB =  this.openOrCreateDatabase(SAMPLE_DB_NAME, MODE_PRIVATE, null);
        sampleDB.execSQL("CREATE TABLE IF NOT EXISTS " + SAMPLE_TABLE_NAME +
                         " (FirstNumber INT, SecondNumber INT," +
                         " Result INT);");
        sampleDB.delete(SAMPLE_TABLE_NAME, null, null);
    }

    private void insertOneHundredRecords()
    {
        for (int i = 0; i < 100; i++)
        {
            ContentValues values = new ContentValues();
            values.put("FirstNumber", i);
            values.put("SecondNumber", i);
            values.put("Result", i*i);
            sampleDB.insert(SAMPLE_TABLE_NAME,null,values);
        }
    }

    private void bulkInsertOneHundredRecords()
    {
        String sql = "INSERT INTO "+ SAMPLE_TABLE_NAME +" VALUES (?,?,?);";

        SQLiteStatement statement = sampleDB.compileStatement(sql);

        sampleDB.beginTransaction();

        for (int i = 0; i < 100; i++)
        {
            statement.clearBindings();
            statement.bindLong(1, i);
            statement.bindLong(2, i);
            statement.bindLong(3, i*i);
            statement.execute();
        }

        sampleDB.setTransactionSuccessful();
        sampleDB.endTransaction();
    }

    @Override
    public void onClick(View v)
    {
        sampleDB.delete(SAMPLE_TABLE_NAME, null, null);

        long startTime = System.currentTimeMillis();

        if (v.getId() == R.id.standard_insert_button)
        {
            insertOneHundredRecords();
        }
        else
        {
            bulkInsertOneHundredRecords();
        }

        long diff = System.currentTimeMillis() - startTime;

        ((TextView) findViewById(R.id.exec_time_label)).setText("Exec Time: " + Long.toString(diff) + "ms");
    }

    @Override
    protected void onDestroy()
    {
        sampleDB.close();
        super.onDestroy();
    }

}