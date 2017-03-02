package com.example.adamlieu.steam_store_web_api_android;

/**
 * Created by Adam Lieu on 3/1/2017.
 */

public class UpcomingReleases {
    private String titleName;
    private String releaseDate;
    private String storeURL;
    private String imgURL;

    private boolean isWindows;
    private boolean isMac;
    private boolean isLinux;

    public UpcomingReleases(String titleName, String releaseDate, String storeURL, String imgURL){
        this.titleName = titleName;
        this.releaseDate = releaseDate;
        this.storeURL = storeURL;
        this.imgURL = imgURL;
    }

    public String getTitleName() { return titleName; }

    public String getStoreURL(){ return storeURL; }

    public String toText(){ return titleName + "\n" + releaseDate; }

}
