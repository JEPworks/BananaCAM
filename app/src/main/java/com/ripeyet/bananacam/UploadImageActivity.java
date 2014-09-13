package com.ripeyet.bananacam;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ripeyet.bananacam.tasks.UploadImageTask;
import com.ripeyet.banancam.CameraUtil.CameraUtility;

/**
 * Created by James on 9/13/2014.
 */
public class UploadImageActivity extends Activity {

    private ImageView cameraImage;
    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_upload_prompt);

        ImageView uploadImage = (ImageView) findViewById(R.id.cameralarge);
        cameraImage = uploadImage;
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UploadImageActivity.this, "CAMERA PUSHED", Toast.LENGTH_LONG).show();
                CameraUtility cameraUtility = new CameraUtility(UploadImageActivity.this);
                fileUri = cameraUtility.takePicture();
            }
        });

        final Context ctx = this;

        Button uploadButton = (Button) findViewById(R.id.bUpload);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImageTask task = new UploadImageTask(ctx, fileUri);
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Cancelled",
                        Toast.LENGTH_LONG).show();
            } else {
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage(fileUri.toString(), cameraImage);

            }



    }
}
