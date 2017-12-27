package com.example.simon.knowyourgovernment;

import android.hardware.camera2.params.Face;

import java.io.Serializable;

/**
 * Created by simon on 3/28/2017.
 */

public class Official  implements Serializable {
    private String Office="";
    private String Name="";
    private String Party="";
    private String Photo_url="";
    private String Office_Address="";
    private String Phone="";
    private String Email="";
    private String Website="";
    private String Facebook="";
    private String Twitter="";
    private String Google="";
    private String Youtube="";

    public String getOffice(){
        return Office;
    }
    public String getName(){
        return Name;
    }
    public String getParty(){
        return Party;
    }
    public String getPhoto_url(){
        return Photo_url;
    }
    public String getOffice_Address(){
        return Office_Address;
    }
    public String getPhone(){
        return Phone;
    }
    public String getEmail(){
        return Email;
    }
    public String getWebsite(){
        return Website;
    }
    public String getFacebook(){
        return Facebook;
    }
    public String getTwitter(){
        return Twitter;
    }
    public String getGoogle(){
        return Google;
    }
    public String getYoutube(){
        return Youtube;
    }
    public void setOffice(String o){
        Office=o;
    }
    public void setName(String n){
        Name=n;
    }
    public void setParty(String p){
        Party=p;
    }
    public void setPhoto_url(String p){
        Photo_url=p;
    }
    public void setOffice_Address(String o){
        Office_Address=o;
    }
    public void setPhone(String p){
        Phone=p;
    }
    public void setEmail(String e){
        Email=e;
    }

    public void setFacebook(String facebook) {
        Facebook = facebook;
    }

    public void setGoogle(String google) {
        Google = google;
    }

    public void setTwitter(String twitter) {
        Twitter = twitter;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public void setYoutube(String youtube) {
        Youtube = youtube;
    }
}
