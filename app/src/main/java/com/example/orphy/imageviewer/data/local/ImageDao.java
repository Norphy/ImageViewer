package com.example.orphy.imageviewer.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.orphy.imageviewer.images.domain.Image;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created on 12/24/2017.
 */
@Dao
public interface ImageDao {

    @Query("select * From Image")
    Flowable<List<Image>> getImages();

    @Insert
    long addImage(Image image);
}
