package com.example.adamlieu.steam_store_web_api_android;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Created by Adam Lieu on 4/13/2017.
 */

public class TopSellersFragment extends Fragment {

    //private static ArrayList<UpcomingReleases> listGames = new ArrayList<UpcomingReleases>();
    private static ArrayList<TopSellerGames> listGames = new ArrayList<TopSellerGames>();
    public String upcomingURL = "http://store.steampowered.com/search/?filter=topsellers";
    public int currentSize = listGames.size();

    public WebView webview;

    private static RecyclerView.Adapter recyclerAdapter;
    private LinearLayoutManager layoutManager;
    private static RecyclerView recyclerView;
    static View.OnClickListener myOnClickListener;

    private boolean loading = true;
    private int counter = 1;

    int pastVisibleItems, visibleItemCount, totalItemCount;

    public TopSellersFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.new_releases, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
        //setContentView(R.layout.activity_main);
        super.onViewCreated(view, savedInstanceState);
        webview = (WebView) view.findViewById(R.id.steam_webview);

        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //new MainActivity.Initialize().execute(upcomingURL);
        new Initialize().execute(upcomingURL);

        recyclerAdapter = new CustomAdapterTopSellers(listGames);
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
                Elements prices = content.getElementsByAttributeValueContaining("class", "col search_price_discount_combined responsive_secondrow");


                for(int i = 0; i < titleName.size(); i++){
                    //UpcomingReleases newTitle = new UpcomingReleases(titleName.get(i).getElementsByClass("title").text(),
                    //        releaseDate.get(i).text(), gameURL.get(i).attr("href"), thumbnail.get(i).attr("src"));

                    TopSellerGames newTitle = new TopSellerGames(titleName.get(i).getElementsByClass("title").text(),
                            releaseDate.get(i).text(), gameURL.get(i).attr("href"),
                            thumbnail.get(i).attr("src"), prices.get(i).getElementsByClass("col search_price  responsive_secondrow").text());


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

        protected void onPostExecute(String result) {
            recyclerAdapter.notifyDataSetChanged();
        }
    }

}