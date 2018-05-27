package main.eavj.ObjectClasses;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties

public class Trip {
    private String id;
    private String title;
    private String dateFrom;
    private String dateTo;
    private float price;
    private boolean isDeleted;
    private int tripStatus;
    private Boolean searchForFriend;

    public Trip() {}

    public Trip(String id, String title, String dateFrom, String dateTo, float price, Boolean searchForFriend)
    {
        this.id = id;
        this.title = title;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.price = price;
        this.isDeleted = false;
        this.tripStatus = 0;
        this.searchForFriend = searchForFriend;
    }

    public Trip(
            String id,
            String title,
            String dateFrom,
            String dateTo,
            float price,
            boolean isDeleted,
            int tripStatus,
            boolean searchForFriend
    ) {
        this.id = id;
        this.title = title;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.price = price;
        this.isDeleted = isDeleted;
        this.tripStatus = tripStatus;
        this.searchForFriend = searchForFriend;
    }

    /** Getters **/

    public String getId() { return this.id; }

    public String getTitle() { return this.title; }

    public Boolean getSearchForFriend() {
        return searchForFriend;
    }

    public void setSearchForFriend(Boolean searchForFriend) {
        this.searchForFriend = searchForFriend;
    }

    public String getDateFrom() {
        return this.dateFrom;
    }

    public String getDateTo() {
        return this.dateTo;
    }

    public Float getPrice() {
        return this.price;
    }

    public boolean IsDeleted() {
        return this.isDeleted;
    }

    public int getTripStatus() {
        return this.tripStatus;
    }

    /** Setters **/

    public void setId(String id) { this.id = id; }

    public void setTitle(String title) { this.title = title; }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void IsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setTripStatus(int tripStatus) {
        this.tripStatus = tripStatus;
    }
}
