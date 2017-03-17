package com.example.adamlieu.steam_store_web_api_android;

/**
 * Created by Adam Lieu on 3/1/2017.
 */

public class UpcomingReleases {
    private String titleName;
    private String releaseDate;
    private String storeURL;
    private String imgURL;

    public boolean isWindows = false;
    public boolean isMac = false;
    public boolean isLinux = false;

    public UpcomingReleases(String titleName, String releaseDate, String storeURL, String imgURL){
        this.titleName = titleName;
        this.releaseDate = releaseDate;
        this.storeURL = storeURL;
        this.imgURL = imgURL;
    }

    public String getTitleName() { return titleName; }

    public String getReleaseDate() {
        if(releaseDate.isEmpty())
            releaseDate = "TBD";
        return releaseDate;
    }

    public String getPlatform(){
        String platform = "Platform: ";

        if(isWindows) platform += "Windows, ";
        if(isMac) platform += "Mac, ";
        if(isLinux) platform += "Linux, ";

        return platform;
    }

    public String getStoreURL(){ return storeURL; }

    public String getImgURL(){ return imgURL; }

    /*
    public String toText(){
        String platform = "Platform: ";

        if(isWindows) platform += "Windows, ";
        if(isMac) platform += "Mac, ";
        if(isLinux) platform += "Linux, ";

        if(releaseDate.isEmpty())
            releaseDate = "TBD";


        return titleName + "\n" + releaseDate + "\n" + platform;
    }*/

}
