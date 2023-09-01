package com.example.achivements;

import static android.content.Context.MODE_PRIVATE;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    SQLiteDatabase db;
    Context context;
    @Before
    public void setUp() {
        // Инициализируем базу данных в методе setUp.
//        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        context = context.getApplicationContext();
        db = context.openOrCreateDatabase("app.db", Context.MODE_PRIVATE, null);
    }

    @Test
    public void addition_isCorrect() {
        db.execSQL("CREATE TABLE IF NOT EXISTS tasks (name TEXT)");
        String taskName = "Название задачи"; // Замените на фактическое название задачи, которое вы хотите добавить.
        String insertQuery = "INSERT INTO tasks (name) VALUES ('" + taskName + "');";
        db.execSQL(insertQuery);
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