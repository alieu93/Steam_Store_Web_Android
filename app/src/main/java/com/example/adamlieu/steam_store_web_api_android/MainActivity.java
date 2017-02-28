package com.example.adamlieu.steam_store_web_api_android;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> listGames = new ArrayList<String>();
    private ListView listView;
    private ArrayAdapter<String> adapter;

    private int pageCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview1);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        listView.setAdapter(adapter);

        new Initialize().execute();

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){}

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState){
                int threshold = 1;
                int count = listView.getCount();

                if(scrollState == SCROLL_STATE_IDLE){
                    if(listView.getLastVisiblePosition() >= count - threshold && pageCount < 2){
                        Log.i("Info", "Scroll Bottom");
                    }
                }
            }
        });
    }

    private class Initialize extends AsyncTask<Void, Void, String> {
        //String ht = "";
        protected String doInBackground(Void... params){
            try {
                /*URL url = new URL("http://store.steampowered.com/search/?filter=comingsoon");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                ht = readStream(in);*/

                Document document = Jsoup.connect("http://store.steampowered.com/search/?filter=comingsoon").get();
                Element content = document.getElementById("search_result_container");

                Elements titleName = content.getElementsByAttributeValueContaining("class", "col search_name ellipsis");
                Elements releaseDate = content.getElementsByAttributeValueContaining("class", "col search_released responsive_secondrow");

                for(int i = 0; i < titleName.size(); i++){
                    //ht += titleName.get(i).getElementsByClass("title").text() + "\n" + "- " + releaseDate.get(i).text() + "\n";
                    listGames.add(titleName.get(i).getElementsByClass("title").text() + "\n" + "- " + releaseDate.get(i).text());
                }
            } catch (MalformedURLException e) {
                Log.e("MalformedURL: ", e.toString());
            } catch (IOException e) {
                Log.e("URL IOException: ", e.toString());
            }


            return null;
        }

        protected void onPostExecute(String result){
            adapter.addAll(listGames);
            adapter.notifyDataSetChanged();
        }
    }

}
