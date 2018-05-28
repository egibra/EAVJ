package main.eavj.ObjectClasses;

public class VisitingPlaceItem {
    private String id;
    private String visitingPlaceId;


    public VisitingPlaceItem() {}

    public VisitingPlaceItem(String id, String visitingPlaceId)
    {
        this.id = id;
        this.visitingPlaceId = visitingPlaceId;
    }



    /** Getters **/

    public String getId() { return this.id; }

    public String getVisitingPlaceId() {
        return this.visitingPlaceId;
    }

    /** Setters **/

    public void setId(String id) { this.id = id; }

    public void setVisitingPlaceId(String dateTo) {
        this.visitingPlaceId = visitingPlaceId;
    }

}
