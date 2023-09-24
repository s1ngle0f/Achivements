package com.example.achivements.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.achivements.db.dao.AchivementImageDao;
import com.example.achivements.db.model.AchivementImage;

@Database(entities = {AchivementImage.class}, version = 1)
public abstract class LocalDatabase extends RoomDatabase {
    public abstract AchivementImageDao achivementImageDao();
}