package main.eavj.ObjectClasses;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class City {
    private String cityID;
    private String cityName;
    private String x;
    private String y;

    public City(){
    }

    public City(String cityID, String cityName, String x, String y) {
        this.cityID = cityID;
        this.cityName = cityName;
        this.x = x;
        this.y = y;
    }

    public String getCityID() {
        return cityID;
    }

    public String getCityName() {
        return cityName;
    }
    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }
}