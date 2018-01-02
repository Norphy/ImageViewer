package com.example.orphy.imageviewer.images.domain;

import android.content.Context;

import com.example.orphy.imageviewer.UseCase;
import com.example.orphy.imageviewer.data.ImageRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * Created on 12/24/2017.
 */
@Singleton
public class GetImagesUseCase extends UseCase<List<Image>, Void> {

    private ImageRepository mImageRepository;

    @Inject
    public GetImagesUseCase(Context context) {
        mImageRepository = new ImageRepository(context);
    }

    @Override
    public Observable<List<Image>> buildUseCaseObservable(Void aVoid) {
        return mImageRepository.images();
    }
}
