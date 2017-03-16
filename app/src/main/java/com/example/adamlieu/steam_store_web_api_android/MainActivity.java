package com.example.adamlieu.steam_store_web_api_android;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //ArrayList<String> listGames = new ArrayList<String>();
    ArrayList<UpcomingReleases> listGames = new ArrayList<UpcomingReleases>();
    private ListView listView;
    private ArrayAdapter<String> adapter;

    public String upcomingURL = "http://store.steampowered.com/search/?filter=comingsoon";

    private int pageCount = 0;
    public int counter = 1;
    public int currentSize = listGames.size();

    public WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webview = (WebView) findViewById(R.id.steam_webview);

        listView = (ListView) findViewById(R.id.listview1);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        new Initialize().execute(upcomingURL);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id){
                String test = listGames.get(position).getStoreURL();

                Intent intent = new Intent(MainActivity.this, WebViewController.class);
                intent.putExtra("URL", test);
                startActivity(intent);

                //Toast.makeText(getApplicationContext(), test, Toast.LENGTH_LONG).show();
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){}

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState){
                int threshold = 1;
                int count = listView.getCount();

                if(scrollState == SCROLL_STATE_IDLE){
                    if(listView.getLastVisiblePosition() >= count - threshold && pageCount < 2){
                        counter++;
                        String test = upcomingURL + "&page=" + counter;
                        Log.v("Test: ", test);
                        new Initialize().execute(upcomingURL + "&page=" + counter);
                    }
                }
            }
        });
    }

    private class Initialize extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params){
            try {
                Document document = Jsoup.connect(params[0]).get();
                Log.e("Reached", params[0]);
                Element content = document.getElementById("search_result_container");

                //Title Name
                Elements titleName = content.getElementsByAttributeValueContaining("class", "col search_name ellipsis");
                //Release Date
                Elements releaseDate = content.getElementsByAttributeValueContaining("class", "col search_released responsive_secondrow");
                //Getting the link to the game's URL
                Elements gameURL = content.select("a");
                Elements thumbnail = content.getElementsByAttributeValueContaining("class", "col search_capsule").select("img");


                for(int i = 0; i < titleName.size(); i++){
                    UpcomingReleases newTitle = new UpcomingReleases(titleName.get(i).getElementsByClass("title").text(),
                            releaseDate.get(i).text(), gameURL.get(i).attr("href"), thumbnail.get(i).attr("src"));

                    //Log.v("Platform: ", titleName.get(i).select("span[class]").toString());
                    //Log.v("Platform: ", titleName.get(i).select("span[class]").toString());
                    if(titleName.get(i).select("span[class]").toString().contains("platform_img win")) newTitle.isWindows = true;
                    if(titleName.get(i).select("span[class]").toString().contains("platform_img mac")) newTitle.isMac = true;
                    if(titleName.get(i).select("span[class]").toString().contains("platform_img linux")) newTitle.isLinux = true;


                    listGames.add(newTitle);
                }
            } catch (MalformedURLException e) {
                Log.e("MalformedURL: ", e.toString());
            } catch (IOException e) {
                Log.e("URL IOException: ", e.toString());
            }
            return null;
        }

        protected void onPostExecute(String result){
            /*for(UpcomingReleases u : listGames) {
                adapter.add(u.toText());
            }*/
            //Log.v("CurrentSize|listGames: ", "" + currentSize +"|"+ listGames.size());
            for(int i = currentSize; i < listGames.size(); i++){
                UpcomingReleases u = listGames.get(i);
                adapter.add(u.toText());
            }
            currentSize = listGames.size();
            adapter.notifyDataSetChanged();
        }
    }

}
