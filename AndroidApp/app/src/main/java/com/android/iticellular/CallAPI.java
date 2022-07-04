package com.android.iticellular;

import android.os.AsyncTask;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class CallAPI extends AsyncTask<String,String, String> {

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }
    @Override
    protected String doInBackground(String... params) {

        String URLString = params[0]; // required URL
        String data = params[1]; // data needed to POST
        OutputStream out = null;

        try {
            URL serverAddress = new URL(URLString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) serverAddress.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Content-Type", "Application/json");
            httpURLConnection.setRequestMethod("POST");

            out = new BufferedOutputStream(httpURLConnection.getOutputStream());
            OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");

            System.out.println(data);

            writer.write(data);
            writer.flush();
            writer.close();
            out.close();

            httpURLConnection.connect();
            int responseCode = httpURLConnection.getResponseCode();
            System.out.println("POST Response Code :: " + responseCode);

        } catch (MalformedURLException | ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
