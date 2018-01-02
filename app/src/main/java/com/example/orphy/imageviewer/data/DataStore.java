package com.example.orphy.imageviewer.data;

import com.example.orphy.imageviewer.images.domain.Image;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created on 12/24/2017.
 */

public interface DataStore {

    Observable<List<Image>> images();

    void addImage(Image image);
}
