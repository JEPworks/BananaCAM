package com.ripeyet.bananacam.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.ripeyet.bananacam.OnBananaReturnHandler;
import com.ripeyet.bananacam.R;

import com.parse.*;

import java.util.Calendar;


/**
 * Created by Josephine on 9/13/2014.
 */
public class LoadImageTask extends AsyncTask<String, String, String> {

    private Context ctx;
    private int location;
    private OnBananaReturnHandler handler;

    public LoadImageTask(Context ctx, int location, OnBananaReturnHandler handler) {
        this.ctx = ctx;
        this.location = location;
        this.handler = handler;
    }

    @Override
    protected String doInBackground(String... strings) {
        String[] rooms = ctx.getResources().getStringArray(R.array.rooms_array);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Banana");
        query.whereEqualTo("location", rooms[location]);
        query.orderByDescending("createdAt");

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject photo, ParseException e) {
                if (e == null) {
                    handler.onCallComplete(photo);
                }
                else {
                    handler.onCallComplete(null);
                }
            }
        });

        return null;
    }

}
