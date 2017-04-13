package com.example.adamlieu.steam_store_web_api_android;

/**
 * Created by Adam Lieu on 4/13/2017.
 */

public class TopSellerGames extends UpcomingReleases {
    /*private String titleName;
    private String releaseDate;
    private String storeURL;
    private String imgURL;*/
    private String gamePrice;

    /*public boolean isWindows = false;
    public boolean isMac = false;
    public boolean isLinux = false;*/

    public TopSellerGames(String titleName, String releaseDate, String storeURL, String imgURL, String price){
        super(titleName, releaseDate, storeURL, imgURL);
        gamePrice = price;

    }

    /*public String getTitleName() { return titleName; }

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

    public String getImgURL(){ return imgURL; }*/

    public String getGamePrice() { return gamePrice; }
}
