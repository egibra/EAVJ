package main.eavj.ObjectClasses;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class VisitingPlace {

    private String visitingPlaceID;
    private String Name;
    private String Category;
    private String Address;
    private String x;
    private String y;
    public VisitingPlace(){
    }

    public VisitingPlace(String visitingPlaceID, String Name, String Category, String Address, String x, String y) {
        this.visitingPlaceID = visitingPlaceID;
        this.Name = Name;
        this.Category = Category;
        this. Address = Address;
        this.x = x;
        this.y = y;
    }

    public String getVisitingPlaceID() {
        return visitingPlaceID;
    }

    public void setVisitingPlaceID(String visitingPlaceID) {
        this.visitingPlaceID = visitingPlaceID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

}