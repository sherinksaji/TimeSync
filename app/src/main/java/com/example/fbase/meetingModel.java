package com.example.fbase;

public class meetingModel {
    public String getMeetingKey() {
        return meetingKey;
    }

    public void setMeetingKey(String meetingKey) {
        this.meetingKey = meetingKey;
    }

    public String getMeetingTitle() {
        return meetingTitle;
    }

    public void setMeetingTitle(String meetingTitle) {
        this.meetingTitle = meetingTitle;
    }

    String meetingKey;
    String meetingTitle;
    String creatorUid;





    public void setCreatorUid(String creatorUid) {
        this.creatorUid = creatorUid;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    String creatorName;

    public String getCreatorUid() {
        return creatorUid;
    }

    public void setCreator(String creatorUid) {
        this.creatorUid = creatorUid;
    }

    public meetingModel(){}

    public meetingModel(String meetingKey, String meetingTitle, String creatorUid,String creatorName) {
        this.meetingKey = meetingKey;
        this.creatorUid = creatorUid;
        this.meetingTitle=meetingTitle;
        this.creatorName=creatorName;


    }



}
