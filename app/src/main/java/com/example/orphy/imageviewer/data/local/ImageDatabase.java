package com.example.orphy.imageviewer.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.orphy.imageviewer.images.domain.Image;

/**
 * Created on 12/24/2017.
 */

@Database(entities = {Image.class}, version = 1)
public abstract class ImageDatabase extends RoomDatabase {

    private static ImageDatabase INSTANCE;

    public abstract ImageDao imageDao();

    public static ImageDatabase getImageDatabase(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ImageDatabase.class, "image-databse").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


}
