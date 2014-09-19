package com.ripeyet.bananacam;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.ripeyet.bananacam.tasks.LoadImageTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
        final View rootView = inflater.inflate(R.layout.fragment_room_selection, container, false);
        final TextView date = (TextView) rootView.findViewById(R.id.createdDate);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipelayout);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE,Color.RED,Color.GREEN,Color.YELLOW);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final ImageView imageView = (ImageView) rootView.findViewById(R.id.ivCurrentBanana);
                final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.pbImages);
                OnBananaReturnHandler handler = new OnBananaReturnHandler() {
                    @Override
                    public void onCallComplete(final ParseObject parseObject) {
                        ParseFile fileObject = parseObject.getParseFile("image");
                        if (fileObject != null) {
                            fileObject.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] image, ParseException e) {
                                    if (e == null) {
                                        Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
                                        imageView.setImageBitmap(bmp);
                                        progressBar.setVisibility(View.GONE);
                                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy  hh:mm a");
                                        String reportDate = df.format(parseObject.getCreatedAt());
                                        date.setText(reportDate);
                                        swipeRefreshLayout.setRefreshing(false);

                                    }
                                    else {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getActivity(), "Could not load image", Toast.LENGTH_SHORT).show();
                                        imageView.setImageResource(R.drawable.distressed_banana);
                                        swipeRefreshLayout.setRefreshing(false);
                                    }
                                }
                            });
                        }
                        else {
                            progressBar.setVisibility(View.GONE);
                            imageView.setImageResource(R.drawable.distressed_banana);
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                };

                LoadImageTask task = new LoadImageTask(getActivity().getBaseContext(),
                        position, handler);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ImageView imageView = (ImageView) view.findViewById(R.id.ivCurrentBanana);
        final TextView date = (TextView) view.findViewById(R.id.createdDate);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.pbImages);
        OnBananaReturnHandler handler = new OnBananaReturnHandler() {
            @Override
            public void onCallComplete(final ParseObject parseObject) {
                ParseFile fileObject = parseObject.getParseFile("image");
                if (fileObject != null) {
                    fileObject.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] image, ParseException e) {
                            if (e == null) {
                                Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
                                imageView.setImageBitmap(bmp);
                                progressBar.setVisibility(View.GONE);
                                DateFormat df = new SimpleDateFormat("MM/dd/yyyy  hh:mm a");
                                String reportDate = df.format(parseObject.getCreatedAt());
                                date.setText(reportDate);

                            }
                            else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "Could not load image", Toast.LENGTH_SHORT).show();
                                imageView.setImageResource(R.drawable.distressed_banana);
                            }
                        }
                    });
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    imageView.setImageResource(R.drawable.distressed_banana);
                }
            }
        };

        LoadImageTask task = new LoadImageTask(getActivity().getBaseContext(),
                position, handler);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
