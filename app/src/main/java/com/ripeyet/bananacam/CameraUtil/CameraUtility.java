package com.ripeyet.bananacam.CameraUtil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by James on 9/12/2014.
 */
public class CameraUtility {

    public Activity activity;
    public static int RESULT_TAKE_PICTURE = 1;

    public CameraUtility(Activity activity) {
        this.activity = activity;
    }

    public Uri takePicture() {
        Intent intent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);

        // Saves the output image uri
        Uri fileUri = Utils_Image_Options
                .getOutputMediaFileUri(Utils_Image_Options.MEDIA_TYPE_IMAGE);


        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        activity.startActivityForResult(intent, RESULT_TAKE_PICTURE);
        return fileUri;
    }



}
