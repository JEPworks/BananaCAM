package com.ripeyet.bananacam.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ripeyet.bananacam.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by Josephine on 9/13/2014.
 */
public class LoadImageTask extends AsyncTask<String, String, String> {

    private Context ctx;
    private String location;

    public LoadImageTask(Context ctx, String location) {
        this.ctx = ctx;
        this.location = location;
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:mysql://23.94.32.228:3306/ripeyet?user=root&password=pass");
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM bananacam WHERE location = '" + this.location + "' ORDER BY timestamp DESC LIMIT 1";
            ResultSet res = stmt.executeQuery(query);

            if (res == null) {
                return null;
            }

            return res.getString("photo");
        }
        catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (s != null) {
            Toast.makeText(this.ctx, s, Toast.LENGTH_LONG).show();
        }
    }
}
