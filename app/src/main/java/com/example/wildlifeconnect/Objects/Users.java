package com.example.wildlifeconnect.Objects;

public class Users {
    private String uid;
    private int type;   //0->president 1->Sub Leader
    private String userName;
    private String ogrName;
    private String regNumber;
    private String email;
    private String presUid;
    private float lat;
    private float lon;
    private String password;
    public Users() {
    }

    public Users(String uid, int type, String userName, String ogrName, String regNumber, String email, String presUid, float lat, float lon) {
        this.uid = uid;
        this.type = type;
        this.userName = userName;
        this.ogrName = ogrName;
        this.regNumber = regNumber;
        this.email = email;
        this.presUid = presUid;
        this.lat = lat;
        this.lon = lon;
    }

    public String getPresUid() {
        return presUid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPresUid(String presUid) {
        this.presUid = presUid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOgrName() {
        return ogrName;
    }

    public void setOgrName(String ogrName) {
        this.ogrName = ogrName;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }
}
