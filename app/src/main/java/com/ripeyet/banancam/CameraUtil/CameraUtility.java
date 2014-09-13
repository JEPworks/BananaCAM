package com.ripeyet.banancam.CameraUtil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by James on 9/12/2014.
 */
public class CameraUtility {

    private Uri fileUri;
    private Activity activity;
    public static int RESULT_TAKE_PICTURE = 1;

    CameraUtility(Activity activity, Uri uri) {
        this.activity = activity;
        this.fileUri = uri;
    }

    public void takePicture() {
        Intent intent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);

        // Saves the output image uri
        fileUri = Utils_Image_Options
                .getOutputMediaFileUri(Utils_Image_Options.MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        activity.startActivityForResult(intent, RESULT_TAKE_PICTURE);
    }



}
