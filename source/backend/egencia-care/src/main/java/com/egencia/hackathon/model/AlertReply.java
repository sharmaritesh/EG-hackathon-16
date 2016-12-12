package com.egencia.hackathon.model;

/**
 * Created by gurssingh on 12/12/16.
 */
public class AlertReply {

    private boolean status;
    private float latitude;
    private float longitute;
    private String city;
    private String country;
    private String userId;
    private String message;
    private String number;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitute() {
        return longitute;
    }

    public void setLongitute(float longitute) {
        this.longitute = longitute;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
