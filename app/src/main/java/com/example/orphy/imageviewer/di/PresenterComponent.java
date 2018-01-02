package com.example.orphy.imageviewer.di;

import com.example.orphy.imageviewer.data.ImageRepository;
import com.example.orphy.imageviewer.images.domain.GetImagesUseCase;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created on 12/30/2017.
 */

@Singleton
@Component(modules = {DataModule.class})
public interface PresenterComponent {
    GetImagesUseCase getImageUseCase();
    ImageRepository getImageReposity();
}
