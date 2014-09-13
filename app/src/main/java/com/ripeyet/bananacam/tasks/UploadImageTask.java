package com.ripeyet.bananacam.tasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.jcraft.jsch.*;

import java.sql.*;

/**
 * Created by Josephine on 9/12/2014.
 */
public class UploadImageTask extends AsyncTask<String,String,String> {

    private Context ctx;
    private Uri fileUri;

    public UploadImageTask(Context ctx, Uri fileUri) {
        this.ctx = ctx;
        this.fileUri = fileUri;
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
            String query = "INSERT INTO bananacam VALUES (NULL, 'testUser', '" + url + "', 0, 0, 'Kitchen')";
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
        }
    }
}
