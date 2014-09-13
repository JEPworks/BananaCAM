package com.ripeyet.bananacam.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.jcraft.jsch.*;
import com.ripeyet.bananacam.R;

import java.sql.*;
import java.util.*;

/**
 * Created by Josephine on 9/12/2014.
 */
public class UploadImageTask extends AsyncTask<String,String,String> {

    private Context ctx;
    private Uri fileUri;
    private int locationIdx;
    ProgressDialog progressDialog;

    public UploadImageTask(Context ctx, Uri fileUri, int locationIdx) {
        this.ctx = ctx;
        this.fileUri = fileUri;
        this.locationIdx = locationIdx;
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage("Uploading Banana");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    private String getFileName() {
        String[] names = this.fileUri.getPath().split("/");
        return names[names.length-1];
    }

    @Override
    protected String doInBackground(String... strings) {
        JSch jsch = new JSch();
        Session session = null;

        try {
            // Upload image
            session = jsch.getSession("root", "23.94.32.228", 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword("pass");
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            String remoteUri = "/var/www/bananacam/" + getFileName();
            sftpChannel.put(fileUri.getPath(), remoteUri);
            sftpChannel.exit();
            session.disconnect();

            // Update database
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:mysql://23.94.32.228:3306/ripeyet?user=root&password=pass");
            Statement stmt = conn.createStatement();
            String url = "http://23.94.32.228/bananacam/" + getFileName();
            String[] rooms = ctx.getResources().getStringArray(R.array.rooms_array);
            String query = "INSERT INTO bananacam VALUES (NOW(), '" + url + "', '" + rooms[locationIdx] + "')";
            int res = stmt.executeUpdate(query);

            return ""+res;
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
        } else {
            Toast.makeText(this.ctx,"Upload Failed",Toast.LENGTH_LONG).show();
        }
        progressDialog.cancel();
    }
}
