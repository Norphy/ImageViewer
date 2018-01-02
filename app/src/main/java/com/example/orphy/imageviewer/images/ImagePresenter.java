package com.example.orphy.imageviewer.images;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.example.orphy.imageviewer.data.ImageRepository;
import com.example.orphy.imageviewer.di.DaggerPresenterComponent;
import com.example.orphy.imageviewer.di.DataModule;
import com.example.orphy.imageviewer.di.PresenterComponent;
import com.example.orphy.imageviewer.images.domain.GetImagesUseCase;
import com.example.orphy.imageviewer.images.domain.Image;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

/**
 * Created on 12/24/2017.
 */

public class ImagePresenter {


    //@BindView(R.id.main_rv)RecyclerView mRecyclerView;
    private ImageRepository imageRepository;
    private ImageAdapter mImageAdapter;
    private List<Image> mList = new ArrayList<>();

    private GetImagesUseCase getImagesUseCase;
    private Intent mCameraIntent = null;
    private String mPhotoPath;
    private static final String TAG = ImagePresenter.class.getSimpleName();
    private Context mContext;


    public static final int REQUEST_IMAGE_CAPTURE = 1;

    ImagePresenter(Context context, ImageAdapter imageAdapter) {

        mContext = context;
        mImageAdapter = imageAdapter;
        Log.v(TAG, "Presenter made");
        PresenterComponent component = DaggerPresenterComponent.builder()
                .dataModule( new DataModule(mContext))
                .build();
        getImagesUseCase = component.getImageUseCase();
        imageRepository = component.getImageReposity();
        //Intent testCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //mActivity.startActivity(testCameraIntent);
    }

    void init() {

        //getImagesUseCase = new GetImagesUseCase(mContext);
        //imageRepository = new ImageRepository(mContext);
        Log.v(TAG, "Presenter initiated");
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,
//                LinearLayoutManager.VERTICAL, false);
//        mRecyclerView = new RecyclerView(mContext);
//        mRecyclerView.setLayoutManager(linearLayoutManager);
//        mImageAdapter = new ImageAdapter(mList, mActivity.getApplicationContext());
//        mRecyclerView.setAdapter(mImageAdapter);
//        mRecyclerView.setHasFixedSize(true);
        /*
            Dexter.withActivity(mActivity)
                    .withPermission(Manifest.permission.CAMERA)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {*/
        //mCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if(mCameraIntent.resolveActivity(mActivity.getPackageManager()) != null) {
//            Completable.fromAction(new Action() {
//                @Override
//                public void run() throws Exception {
//                    Uri photoUri = getImageUri();
//                    mCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
//                    mActivity.startActivityForResult(mCameraIntent, REQUEST_IMAGE_CAPTURE);
//                    Log.v(TAG, "not returned for result");
//
//                }
//            }).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe();

        getImages();
    }/*
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            Toast.makeText(mActivity.getApplicationContext(), "Camera Permission Denied", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        }
                    }).check();*/

    public Uri getImageUri() {
        Log.v(TAG, "getImageUri");
        File photoFile = null;
        Uri fileUri = null;
        try{
            photoFile = createImageFile();
        } catch(IOException e) {
            e.printStackTrace();
        }
        if(photoFile != null) {
            fileUri = FileProvider.getUriForFile(mContext,
                    "com.example.android.fileprovider", photoFile);
        }

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            mCameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            mCameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        }
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            List<ResolveInfo> resolvedIntentActivities = mContext
                    .getPackageManager()
                    .queryIntentActivities(mCameraIntent, PackageManager.MATCH_DEFAULT_ONLY);

            for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                String packageName = resolvedIntentInfo.activityInfo.packageName;

                mContext.grantUriPermission(packageName,
                        fileUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
        return fileUri;
    }

    private File createImageFile() throws IOException {
        Log.v(TAG, "createImageFile");
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Log.v(TAG, "Can write to external directory");
        } else {
            Log.v(TAG, "Cannot write to external directory");
        }
        File fileDirectory = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "images");
        if(!fileDirectory.mkdirs()) {
            Log.v(TAG, "File Directory was not created");
        }
        //File imageFile = new File(fileDirectory, (timeStamp + ".jpg"));
        File imageFile = File.createTempFile(
                "JPEG_" + timeStamp + "_" ,  /* prefix */
                ".jpg",         /* suffix */
                fileDirectory      /* directory */
        );
        String imagePath =  imageFile.getAbsolutePath();
        mPhotoPath = imagePath;
        Image newImage = new Image(timeStamp, imagePath);
        Log.v(TAG, "new image created path: " + mPhotoPath);
        imageRepository.addImage(newImage);
        //mList.add(newImage);
        //mImageAdapter.swapList(mList);
        return imageFile;
    }



    public Intent returnCameraIntent() {
        return mCameraIntent;
    }

    public void passCameraIntent(Intent cameraIntent) {
        mCameraIntent = cameraIntent;
    }




    public void showImages() {
        //mRecyclerView.setVisibility(View.VISIBLE);
    }

    public void getImages() {
        getImagesUseCase.execute(new Observer(), null);
    }

    public void disposeObservable() {
        getImagesUseCase.dispose();
        Log.v(TAG, "Dispose Observable");
    }

    private class Observer extends DisposableObserver<List<Image>> {

        @Override
        public void onNext(List<Image> list) {
            mImageAdapter.swapList(list);
            Log.v(TAG, "New list created, swapping adapter");
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    }
}
