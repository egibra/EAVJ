package main.eavj.ObjectClasses;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Country {
    private String countryID;
    private String countryName;

    public Country(){
    }

    public Country(String countryID, String countryName) {
        this.countryID = countryID;
        this.countryName = countryName;
    }

    public String getCountryID() {
        return countryID;
    }

    public String getCountryName() {
        return countryName;
    }
}