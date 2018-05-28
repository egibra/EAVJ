package main.eavj.ObjectClasses;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class TripItem {
    private String id;
    private String title;
    private String dateFrom;
    private String dateTo;


    public TripItem() {}

    public TripItem(String id,String title, String dateFrom, String dateTo)
    {
        this.id = id;
        this.title = title;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }


//    public TripItem(
//            String id,
//            String city,
//            String dateFrom,
//            String dateTo,
//            float price,
//            boolean isDeleted
//    ) {
//        this.id = id;
//        this.city = city;
//        this.dateFrom = dateFrom;
//        this.dateTo = dateTo;
//        this.price = price;
//        this.isDeleted = isDeleted;
//    }

    /** Getters **/

    public String getId() { return this.id; }

    public String getTitle() { return this.title; }

    public String getDateFrom() {
        return this.dateFrom;
    }

    public String getDateTo() {
        return this.dateTo;
    }

//    public Float getPrice() {
//        return this.price;
//    }

//    public boolean IsDeleted() {
//        return this.isDeleted;
//    }

    /** Setters **/

    public void setId(String id) { this.id = id; }

    public void setTitle(String title) { this.title = title; }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

//    public void setPrice(Float price) {
//        this.price = price;
//    }

//    public void IsDeleted(boolean isDeleted) {
//        this.isDeleted = isDeleted;
//    }
}
