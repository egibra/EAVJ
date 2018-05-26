package main.eavj.ObjectClasses;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties

public class Review {
    private String id;
    private String author;
    private String comment;
    private String date;

    public Review() {}

    public Review(String id, String author, String comment, String date)
    {
        this.id = id;
        this.author = author;
        this.comment = comment;
        this.date = date;
    }

    /** Getters **/

    public String getId() { return this.id; }

    public String getAuthor() { return this.author; }

    public String getComment() {
        return this.comment;
    }

    public String getDate() { return this.date; }

    /** Setters **/

    public void setId(String id) { this.id = id; }

    public void setAuthor(String author) { this.author = author; }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDate(String date) { this.date = date; }
}