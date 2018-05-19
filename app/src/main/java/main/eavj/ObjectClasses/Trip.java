package main.eavj.ObjectClasses;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties

public class Trip {
    private String dateFrom;
    private String dateTo;
    private float price;
    private boolean isDeleted;
    private int tripStatus;

    public Trip() {}

    public Trip(String dateFrom, String dateTo, float price)
    {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.price = price;
        this.isDeleted = false;
        this.tripStatus = 0;
    }

    public Trip(
            String dateFrom,
            String dateTo,
            float price,
            boolean isDeleted,
            int tripStatus
    ) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.price = price;
        this.isDeleted = isDeleted;
        this.tripStatus = tripStatus;
    }

    /** Getters **/

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
