package com.ripeyet.bananacam.tasks;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ripeyet.bananacam.OnBananaReturnHandler;
import com.ripeyet.bananacam.R;

/**
 * Created by James on 9/13/2014.
 */
public class BananaViewerFragment extends Fragment {

    private int position;

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_room_selection, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ImageView imageView = (ImageView) view.findViewById(R.id.ivCurrentBanana);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.pbImages);
        OnBananaReturnHandler handler = new OnBananaReturnHandler() {
            @Override
            public void onCallComplete(String url) {
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(url, imageView, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        progressBar.setVisibility(View.GONE);
                        ((ImageView) view).setImageResource(R.drawable.banana_sad);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        progressBar.setVisibility(View.GONE);
                    }
                });

            }
        };

        LoadImageTask task = new LoadImageTask(getActivity().getBaseContext(),
                position, handler);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
}
