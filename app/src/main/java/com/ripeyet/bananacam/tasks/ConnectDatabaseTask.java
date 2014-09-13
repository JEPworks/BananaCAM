package com.ripeyet.bananacam.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.jcraft.jsch.*;

import java.sql.*;

/**
 * Created by Josephine on 9/12/2014.
 */
public class ConnectDatabaseTask extends AsyncTask<String,String,String> {

    private Context ctx;

    public ConnectDatabaseTask(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(String... strings) {
        JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession("root", "23.94.32.228", 22);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword("pass");
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            sftpChannel.get("remotefile.txt", "localfile.txt");
            sftpChannel.exit();
            session.disconnect();
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        }

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection("jdbc:mysql://23.94.32.228:3306/ripeyet?user=root&password=pass");
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM bananacam");

            while (res.next()) {
                Date timestamp = res.getDate("timestamp");
                String user = res.getString("user");
                String photo = res.getString("photo");
                int ripeness = res.getInt("ripeness");
                int count = res.getInt("count");

                return user+" "+photo+" "+ripeness+" "+count;
            }
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
