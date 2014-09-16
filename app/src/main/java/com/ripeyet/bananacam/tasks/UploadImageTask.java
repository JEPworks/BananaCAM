package com.ripeyet.bananacam.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.parse.*;
import com.ripeyet.bananacam.R;

import java.io.ByteArrayOutputStream;

/**
 * Created by Josephine on 9/12/2014.
 */
public class UploadImageTask extends AsyncTask<String, String, String> {

    private Activity act;
    private Uri fileUri;
    private int location;
    ProgressDialog progressDialog;

    public UploadImageTask(Activity act, Uri fileUri, int location) {
        this.act = act;
        this.fileUri = fileUri;
        this.location = location;

        progressDialog = new ProgressDialog(act);
        progressDialog.setMessage("Uploading Banana");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private String getFileName() {
        String[] names = fileUri.getPath().split("/");
        return names[names.length-1];
    }

    @Override
    protected String doInBackground(String... strings) {
        final String[] rooms = act.getResources().getStringArray(R.array.rooms_array);

        // Create image object
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(), options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        byte[] image = stream.toByteArray();

        ParseFile file = new ParseFile(getFileName(), image);
        file.saveInBackground();

        // Update database with image and location
        ParseObject testObject = new ParseObject("Banana");
        testObject.put("image", file);
        testObject.put("location", rooms[location]);
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    progressDialog.cancel();
                    ParsePush push = new ParsePush();
                    push.setChannel(rooms[location].replaceAll("\\s",""));
                    push.setMessage("New Banana Pic for " + rooms[location]);
                    push.sendInBackground();
                    act.finish();
                }
                else {
                    Toast.makeText(act, "Could not upload image", Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();
                }
            }
        });

        return null;
    }

}
