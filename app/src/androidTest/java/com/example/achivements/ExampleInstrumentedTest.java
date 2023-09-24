package com.example.achivements;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.achivements.db.LocalDatabase;
import com.example.achivements.db.model.AchivementImage;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    SQLiteDatabase db;
    Context context;

    public static LocalDatabase database;
    @Before
    public void setUp() {
        // Инициализируем базу данных в методе setUp.
//        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        context = getApplicationContext();
        database = Room.databaseBuilder(context, LocalDatabase.class, "testRoomDBOne")
                .allowMainThreadQueries() // Позволяет выполнять запросы на главном потоке (для демонстрации, не рекомендуется в реальном приложении)
                .build();
    }

    @Test
    public void createAchivementImage(){
        AchivementImage achivementImage = new AchivementImage();
        achivementImage.achivementId = 3;
        database.achivementImageDao().insert(achivementImage);
        getAllAchivementImages();
        AchivementImage achivementImage1 = database.achivementImageDao().getImageByAchivementId(3);
        achivementImage1.achivementId = 5;
        database.achivementImageDao().insert(achivementImage1);
        getAllAchivementImages();
    }

    @Test
    public void getAchivementImageById(){
        getAllAchivementImages();
        System.out.println(database.achivementImageDao().getImageByAchivementId(3));
    }

    @Test
    public void getAllAchivementImages(){
        System.out.println(database.achivementImageDao().getAllAchivementImages());
    }
}