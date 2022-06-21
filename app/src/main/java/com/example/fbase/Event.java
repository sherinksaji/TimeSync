package com.example.fbase;

public class Event {
    public String period;
    public String title;
    public String key;
    public int startTimeInt;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }



    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Event(){}

    public Event(String title, String period, String key) {
        this.period = period;
        //need startTimeInt to order events in firebase by their startTime
        this.startTimeInt=new TimePeriod(period).startTime.getIntTime();
        this.title = title;
        this.key= key;
    }


    @Override
    public String toString(){
        return this.title+"#"+this.period;
    }


}
