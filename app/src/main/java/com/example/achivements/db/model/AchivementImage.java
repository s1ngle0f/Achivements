package com.example.achivements.db.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Arrays;

@Entity(tableName = "achivement_image")
public class AchivementImage {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int achivementId;
    public byte[] imageData;

    @Override
    public String toString() {
        return "AchivementImage{" +
                "id=" + id +
                ", achivementId=" + achivementId +
                ", imageData=" + Arrays.toString(imageData) +
                '}';
    }
}
