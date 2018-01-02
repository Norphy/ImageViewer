package com.example.orphy.imageviewer.di;

import android.content.Context;

import com.example.orphy.imageviewer.data.ImageRepository;
import com.example.orphy.imageviewer.images.domain.GetImagesUseCase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created on 1/2/2018.
 */
@Module
public class DataModule {
    final private Context mContext;

    public DataModule(Context context) {
        this.mContext = context;
    }

    @Provides
    @Singleton
    GetImagesUseCase provideGetImagesUseCase() {
        return new GetImagesUseCase(mContext);
    }

    @Provides
    @Singleton
    ImageRepository providesImageRepository() {
        return new ImageRepository(mContext);
    }
}
