package com.example.adamlieu.steam_store_web_api_android;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static ArrayList<UpcomingReleases> listGames = new ArrayList<UpcomingReleases>();
    public String upcomingURL = "http://store.steampowered.com/search/?filter=comingsoon";
    public int currentSize = listGames.size();

    public WebView webview;

    private static RecyclerView.Adapter recyclerAdapter;
    private LinearLayoutManager layoutManager;
    private static RecyclerView recyclerView;
    static View.OnClickListener myOnClickListener;

    private boolean loading = true;
    private int counter = 1;

    int pastVisibleItems, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webview = (WebView) findViewById(R.id.steam_webview);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        new Initialize().execute(upcomingURL);

        recyclerAdapter = new CustomAdapter(listGames);
        recyclerView.setAdapter(recyclerAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener () {
            public void onScrolled(RecyclerView rv, int dx, int dy){
                if(dy > 0){
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if((visibleItemCount + pastVisibleItems) >= totalItemCount){
                        loading = false;
                        counter++;
                        String newURL = upcomingURL + "&page=" + counter;
                        new Initialize().execute(newURL);
                        loading = true;

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
            recyclerAdapter.notifyDataSetChanged();
        }
    }

}
