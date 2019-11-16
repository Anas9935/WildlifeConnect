package com.example.wildlifeconnect.Objects;

import java.util.ArrayList;

public class FlagObject {
    private String fid;
    private int status; //0->Black 1->Red 2->green
    private String raiserId;
    private String subject;
    private ArrayList<String> tags;
    private int approval; //0->not yet Approved 1->approved  2->rejected
    private float fLat;
    private float fLon;
    private long raiseTime;
    private long finalDay;

    public FlagObject() {

    }

    public FlagObject(String fid, int status, String raiserId, String subject, ArrayList<String> tags,
                      int approval, float fLat, float fLon, long raiseTime, long finalDay) {
        this.fid = fid;
        this.status = status;
        this.raiserId = raiserId;
        this.subject = subject;
        this.tags = tags;
        this.approval = approval;
        this.fLat = fLat;
        this.fLon = fLon;
        this.raiseTime = raiseTime;
        this.finalDay = finalDay;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRaiserId() {
        return raiserId;
    }

    public void setRaiserId(String raiserId) {
        this.raiserId = raiserId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public int getApproval() {
        return approval;
    }

    public void setApproval(int approval) {
        this.approval = approval;
    }

    public float getfLat() {
        return fLat;
    }

    public void setfLat(float fLat) {
        this.fLat = fLat;
    }

    public float getfLon() {
        return fLon;
    }

    public void setfLon(float fLon) {
        this.fLon = fLon;
    }

    public long getRaiseTime() {
        return raiseTime;
    }

    public void setRaiseTime(long raiseTime) {
        this.raiseTime = raiseTime;
    }

    public long getFinalDay() {
        return finalDay;
    }

    public void setFinalDay(long finalDay) {
        this.finalDay = finalDay;
    }
}
