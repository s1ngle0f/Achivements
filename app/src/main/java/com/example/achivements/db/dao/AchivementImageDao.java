package com.example.achivements.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.achivements.db.model.AchivementImage;

import java.util.List;

@Dao
public interface AchivementImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AchivementImage achivementImage);

    @Query("SELECT * FROM achivement_image WHERE achivementId = :achivementId LIMIT 1")
    AchivementImage getImageByAchivementId(int achivementId);

    @Query("SELECT * FROM achivement_image WHERE achivementId = :achivementId")
    List<AchivementImage> getImagesByAchivementId(int achivementId);

    @Query("SELECT * FROM achivement_image")
    List<AchivementImage> getAllAchivementImages();
}
