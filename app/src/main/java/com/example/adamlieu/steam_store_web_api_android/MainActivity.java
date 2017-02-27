package com.example.adamlieu.steam_store_web_api_android;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Initialize().execute();
    }

    private class Initialize extends AsyncTask<Void, Void, String> {
        String ht;
        protected String doInBackground(Void... params){
            try {
                URL url = new URL("http://store.steampowered.com/search/?filter=comingsoon");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                ht = readStream(in);
            } catch (MalformedURLException e) {
                Log.e("MalformedURL: ", e.toString());
            } catch (IOException e) {
                Log.e("URL IOException: ", e.toString());
            }


            return ht;
        }

        protected void onPostExecute(String result){
            TextView t = (TextView) findViewById(R.id.textview1);
            //t.setText(ht);
            t.setText(ht);
            t.setMovementMethod(new ScrollingMovementMethod());
        }
    }

    private String readStream(InputStream in){
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        String line;
        boolean flag = false;

        try {
            br = new BufferedReader(new InputStreamReader(in));
            while((line = br.readLine()) != null){
                //TODO: find some way to filter out everything but whatever falls into this div category
                if(line.contains("search_result_container")){
                    flag = true;
                }
                if(flag) sb.append(line);

            }
        } catch(IOException e){
            Log.e("BufferedReader", e.toString());
        } finally {
            if(br != null){
                try {
                    br.close();
                } catch (IOException e){
                    Log.e("BufferedReader close", e.toString());
                }
            }
        }

        Log.e("readStream", sb.toString());
        return sb.toString();
        //TextView text = (TextView) findViewById(R.id.textview1);
        //text.setText(sb.toString());
    }


}
