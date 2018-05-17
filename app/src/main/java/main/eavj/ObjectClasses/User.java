package main.eavj.ObjectClasses;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties

public class User {
    private String userName;
    private String role;
    private String userID;

    public User()
    {

    }
    public User(String userName, String role, String userID)
    {
        this.userName = userName;
        this.role = role;
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }
}
