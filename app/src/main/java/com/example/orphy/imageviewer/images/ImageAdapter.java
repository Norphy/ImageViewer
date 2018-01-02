package com.example.orphy.imageviewer.images;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.orphy.imageviewer.R;
import com.example.orphy.imageviewer.images.domain.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 12/24/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    @BindView(R.id.photo_image_view)
    ImageView mImageView;
    @BindView(R.id.time_text_view)
    TextView mTextView;

    private List<Image> mImageList = new ArrayList<>();
    private Context mContext;
    private static final String TAG = ImageAdapter.class.getSimpleName();


    ImageAdapter(Context context) {

        mContext = context;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_layout, parent, false);
        ButterKnife.bind(this, view);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Image currentImage = mImageList.get(position);
        String currentPhotoPath = currentImage.getImagePath();
        String currentPhotoTimeStamp = currentImage.getTimeStamp();
        Log.v(TAG, mContext.toString());
        Log.v(TAG, currentPhotoPath);
        Glide.with(mContext)
                .load(new File(currentPhotoPath))
                .into(mImageView);
        mTextView.setText(currentPhotoTimeStamp);
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    void swapList(List<Image> newImageList) {
        mImageList = newImageList;
        notifyDataSetChanged();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageViewHolder(View itemView) {
            super(itemView);
        }
    }
}
