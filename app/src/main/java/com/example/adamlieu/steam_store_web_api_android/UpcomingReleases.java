package com.example.adamlieu.steam_store_web_api_android;

/**
 * Created by Adam Lieu on 3/1/2017.
 */

public class UpcomingReleases {
    private String titleName;
    private String releaseDate;

    private boolean isWindows;
    private boolean isMac;
    private boolean isLinux;

    public UpcomingReleases(String titleName, String releaseDate){
        this.titleName = titleName;
        this.releaseDate = releaseDate;
    }

    public String display(){
        return titleName + "\n" + releaseDate;
    }

}
