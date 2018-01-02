package com.example.orphy.imageviewer.data;

import android.content.Context;
import android.util.Log;

import com.example.orphy.imageviewer.images.domain.Image;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created on 12/24/2017.
 */

public class ImageRepository {

    LocalDataStore localDataStore;
    private static final String TAG = ImageRepository.class.getSimpleName();

    public ImageRepository(Context context){localDataStore = new LocalDataStore(context);}

    public Observable<List<Image>> images() {
        Log.v(TAG, "getting images");
        return localDataStore.images();

    }

    public void addImage(Image image) {
        localDataStore.addImage(image);
    }

}
