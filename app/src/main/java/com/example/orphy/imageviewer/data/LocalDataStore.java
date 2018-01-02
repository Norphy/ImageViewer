package com.example.orphy.imageviewer.data;

import android.content.Context;
import android.util.Log;

import com.example.orphy.imageviewer.data.local.ImageDatabase;
import com.example.orphy.imageviewer.images.domain.Image;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 12/24/2017.
 */

public class LocalDataStore implements DataStore {

    private ImageDatabase mImageDatabase;
    private static final String TAG = LocalDataStore.class.getSimpleName();

    public LocalDataStore(Context context) {
        mImageDatabase = ImageDatabase.getImageDatabase(context);
    }

    @Override
    public Observable<List<Image>> images() {
        Log.v(TAG, "getting images");
       return mImageDatabase.imageDao().getImages().toObservable();
    }

    @Override
    public void addImage(final Image image) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {

                long id = mImageDatabase.imageDao().addImage(image);
                if(id > 0) {
                    Log.v(TAG, "Successfully added new image id: " + id);
                } else {
                    Log.v(TAG, "Failed to add new image id: " + id);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe();
    }
}
