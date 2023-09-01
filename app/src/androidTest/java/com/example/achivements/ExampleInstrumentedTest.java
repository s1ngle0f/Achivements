package com.example.achivements;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    SQLiteDatabase db;
    Context context;
    @Before
    public void setUp() {
        // Инициализируем базу данных в методе setUp.
//        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        context = getApplicationContext();
        db = context.openOrCreateDatabase("app.db", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS tasks (name TEXT)");
    }

    @Test
    public void addition_isCorrect() {
        String taskName = "Название задачи"; // Замените на фактическое название задачи, которое вы хотите добавить.
        String insertQuery = "INSERT INTO tasks (name) VALUES ('" + taskName + "');";
        db.execSQL(insertQuery);

        String selectQuery = "SELECT * FROM tasks;";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String taskName1 = cursor.getString(cursor.getColumnIndex("name"));
                System.out.println(taskName1);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }

    @Test
    public void get_All() {
        String selectQuery = "SELECT * FROM tasks;";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String taskName = cursor.getString(cursor.getColumnIndex("name"));
                System.out.println(taskName);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }
}