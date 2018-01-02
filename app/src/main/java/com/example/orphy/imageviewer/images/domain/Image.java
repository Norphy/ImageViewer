package com.example.orphy.imageviewer.images.domain;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created on 12/24/2017.
 */

@Entity
public class Image {

    @PrimaryKey(autoGenerate = true)
    private int mId;
    @ColumnInfo(name = "timeStamp")
    private String mTimeStamp;
    @ColumnInfo(name = "imagePath")
    private String mImagePath;

    @Ignore
    public Image(String timeStamp, String imagePath) {
        this.mTimeStamp = timeStamp;
        this.mImagePath = imagePath;
    }


    public Image(int id, String timeStamp, String imagePath) {
        this.mId = id;
        this.mTimeStamp = timeStamp;
        this.mImagePath = imagePath;
    }

    //Getter methods
    public int getId() {
        return mId;
    }
    public String getTimeStamp() {
        return mTimeStamp;
    }

    public String getImagePath() {
        return mImagePath;
    }
}
