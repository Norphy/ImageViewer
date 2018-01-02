package com.example.orphy.imageviewer.images;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.orphy.imageviewer.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class ImageActivity extends AppCompatActivity {


    private ImagePresenter mPresenter;

    @BindView(R.id.main_rv) RecyclerView mRecyclerView;
    @BindView(R.id.add_image_fab) FloatingActionButton mFab;
    private Intent mCameraIntent = null;
    private static final String TAG = ImageActivity.class.getSimpleName();
    private static final int IMAGE_REQUEST_CODE = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(TAG, "Activity onCreated");
        ButterKnife.bind(this);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.hide();
        }

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.main_collapsing_toolbar);
        collapsingToolbarLayout.setTitle("ImageViewer");

        ImageAdapter imageAdapter = new ImageAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(imageAdapter);
        mPresenter = new ImagePresenter( this, imageAdapter);
        mPresenter.init();

        /*
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView = findViewById(R.id.main_rv);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mImageAdapter = new ImageAdapter(mPathList, this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mImageAdapter);
        imageRepository = new ImageRepository(this);
        getImagesUseCase = new GetImagesUseCase(this);

        mCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (mCameraIntent.resolveActivity(getPackageManager()) != null) {
            Completable.fromAction(new Action() {
                @Override
                public void run() throws Exception {
                    Uri imageUri = createImageFileUri();
                    Log.v(TAG, "new image created path: " + imageUri);
                    mCameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(mCameraIntent, IMAGE_REQUEST_CODE);

                }
            }).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe();

        }*/
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "Camera fab fired");
                mCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (mCameraIntent.resolveActivity(getPackageManager()) != null) {
                    Completable.fromAction(new Action() {
                        @Override
                        public void run() throws Exception {
                            mPresenter.passCameraIntent(mCameraIntent);
                            Uri photoUri = mPresenter.getImageUri();
                            Log.v(TAG, "Uri in intent: " + photoUri);
                            mCameraIntent = mPresenter.returnCameraIntent();
                            mCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

                            Dexter.withActivity(ImageActivity.this)
                                    .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                            Manifest.permission.CAMERA)
                                    .withListener(new MultiplePermissionsListener() {
                                        @Override
                                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                                            if(report.areAllPermissionsGranted()) {
                                                startActivityForResult(mCameraIntent, ImagePresenter.REQUEST_IMAGE_CAPTURE);
                                                Log.v(TAG, "Permissions granted");
                                            } else {
                                                Log.v(TAG, "One or more permissions denied");
                                            }
                                        }

                                        @Override
                                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                                        }
                                    }).check();

                            Log.v(TAG, "not returned for result");

                        }
                    }).subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).subscribe();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v(TAG, "Returned from intent");
        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.v(TAG, "OnActivityResult: Result_Ok");
        } else if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            Log.v(TAG, "OnActivityResult: Result_Cancelled");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "Activity onDestroyed");
        mPresenter.disposeObservable();
    }

    //    private Uri createImageFileUri() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String state = Environment.getExternalStorageState();
//        if (Environment.MEDIA_MOUNTED.equals(state)) {
//            Log.v(TAG, "Can write to external directory");
//        } else {
//            Log.v(TAG, "Cannnot write to external directory");
//        }
//        File fileDirectory = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "images");
//        if (!fileDirectory.mkdirs()) {
//            Log.v(TAG, "File Directory was not created");
//        }
//        //File imageFile = new File(fileDirectory, (timeStamp + ".jpg"));
//        File imageFile = File.createTempFile(
//                "JPEG_" + timeStamp + "_",  /* prefix */
//                ".jpg",         /* suffix */
//                fileDirectory      /* directory */
//        );
//        String imagePath = imageFile.getAbsolutePath();
//        mPathList.add(imagePath);
//        mImageAdapter.swapList(mPathList);
//        Uri imageFileUri = null;
//
//        imageFileUri = FileProvider.getUriForFile(getApplicationContext(),
//                "com.example.android.fileprovider",
//                imageFile);
//        return imageFileUri;
//    }
}
