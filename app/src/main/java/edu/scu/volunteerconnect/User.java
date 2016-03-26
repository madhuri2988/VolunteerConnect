package edu.scu.volunteerconnect;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Priyanka on 2/7/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)

public class User {
    String name;
    String emailId;
    String password;
public User(){

}

    User(String n, String e, String p){
        this.name =n;
        this.emailId =e;
        this.password =p;
    }

    public String getEmailId() {
        return emailId;
    }
    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String toString() {
        return "User{username='" + name + "\', emailId='" + emailId + "\', password='" + password + "'}";
    }
}
