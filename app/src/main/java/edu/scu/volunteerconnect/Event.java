package edu.scu.volunteerconnect;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Priyanka on 2/21/2016.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Event implements Serializable {
    String contactName;
    String eventTitle;
    String address;
    String zipcode;
    String state;
    String organLink;
    String phoneNumber;
    String evenDetails;
    String startTime;
    String endTime;
    String country;
    String imaglebase64;
    String date;
    String category;
    String volunteercount;
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }



    Event( String contactName,
            String eventTitle,
            String address,
            String zipcode,
            String state,
            String organLink,
            String phoneNumber,
            String evenDetails,
            String startTime,
            String endTime,
            String country,
            String imaglebase64,
            String date,
            String category){
        this.contactName=contactName;
        this.eventTitle=eventTitle;
        this.zipcode=zipcode;
        this.state=state;
        this.organLink=organLink;
        this.phoneNumber=phoneNumber;
        this.evenDetails=evenDetails;
        this.startTime=startTime;
        this.endTime=endTime;
        this.country=country;
                this.imaglebase64=imaglebase64;
        this.address=address;
        this.date=date;
        this.category=category;
        this.volunteercount=volunteercount;


}
    Event(){

    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public String getEvenDetails() {
        return evenDetails;
    }

    public void setEvenDetails(String evenDetails) {
        this.evenDetails = evenDetails;
    }

    public String getImaglebase64() {
        return imaglebase64;
    }

    public void setImaglebase64(String imaglebase64) {
        this.imaglebase64 = imaglebase64;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }


    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public String getOrganLink() {
        return organLink;
    }

    public void setOrganLink(String organLink) {
        this.organLink = organLink;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getVolunteercount() {
        return volunteercount;
    }

    public void setVolunteercount(String volunteercount) {
        this.volunteercount = volunteercount;
    }

}
