package com.example.fbase;

/**
Event class constructor requires  parameters String title (e.g. "math"), String period("07:00 - 07:30"),
String key("4NyKMXCovHQB4Y4OQeuXSug9dim1") which are assigned to class attributes this.title, this.period,
this.key.
A new TimePeriod is object is created. The parameter String period is passed in as
a parameter for the TimePeriod constructor. Then the integer value of the startTime of the ]
TimePeriod object is assigned to this.startTimeInt.
For example, if the parameter Sting period is "07:00 - 07:30", then this.startTimeInt=700.
In the TimetableActivity, when reading the events from firebase, we can use
orderByChild("startTimeInt") to order the events chronologically.

this.period is only a string and we do not make it a TimePeriod object because we want to push simple
key-value pairs up into the firebase database, not nested objects.

There are getters and setters for this.title, this.key, this.period and this.startTimeInt.

String toString method returns this.title+"#"+this.period.
For example, if this.title="math" and this.period="07:00 - 07:30",
then toString methods a string "math#07:00 - 07:30"




*/

public class Event {
    public String period;
    public String title;
    public String key;

    public int getStartTimeInt() {
        return this.startTimeInt;
    }

    public void setStartTimeInt(int startTimeInt) {
        this.startTimeInt = startTimeInt;
    }

    public int startTimeInt;



    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }



    public String getPeriod() {
        return this.period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getTitle() {
        return this.title;
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
