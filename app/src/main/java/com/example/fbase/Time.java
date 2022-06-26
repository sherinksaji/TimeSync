package com.example.fbase;

/**
Documentation:
This is the flow of the app (i put this almost wherever relevant for ease of
reference):
          ForgotPassword <--> LoginActivity <--> RegisterUserActivity(uses User)
						            ^
  (uses viewMeetingsAdapter         |
  & meetingModel)	                v
     ViewMeetingsActivity <--> HomeActivity <--> TimetableActivity(uses TimetableAdapter & Event)
	  ^               ^                                    ^
      |               |                                    |
      v               v                                    v
selectUsers       MeetActivity		      AddEventActivity(uses Event)
Activity        (uses MeetAdapter)
(uses
selectUsersAdapter
&showSelectedUsersAdapter)

Other Backend classes:

SortbyStartTime(used by AddEvent)
  ^
  |
Time--> TimePeriod --> DirtyTimeSet --> AvailableSlots
     (used for AddEvent              (used for MeetActivity)
      &MeetActivity)

Time class constructor requires parameter String time in the 24 HR Clock
form "HH:MM" (e.g. "07:00","17:30",etc.).
Parameter String time is assigned to class attribute this.time.

public String getTime() returns string this.time.

public void setTime(String time) requires parameter String time in the
form "HH:MM" (e.g. "07:00").
this.time is set to the parameter String time.

public int getHour() returns the hour segment of this.time as an integer.
If this.time="07:00", then int 7 is returned.
If this.time="17:00", then int 17 is  returned.

public int getMinute() returns the minute segment of this.time as an integer.
If this.time="07:07", the int 7 is returned.
If this.time="07:17", the int 17 is returned.

public void setHour(int hour) requires parameter int hour.
this.time's hour segment is modified to match parameter int hour.
Let this.time="07:07" originally.
If parameter int hour>=10,say for example int 10,
then this.time is modified to "10:07".
If parameter int hour<10,say for example int 9,
then padding with 0 is required and this.time is modified to "09:07".

(There is no need to worry about weird hour and minute inputs like negative
hours and minutes or non-existent times like 79:79 because users will
input their times inside a timepicker that only allows for valid time inputs)

public void setMinute(int minute) requires parameter int minute.
this.time's minute segment is modified to match parameter int minute.
Let this.time="07:07" originally.
If parameter int minute>=10,say for example int 10,
then this.time is modified to "07:10".
If parameter int hour<10,say for example int 9,
then this.time is modified to "07:09".

public int getIntTime() returns a this.time in its int form
If this.time is "07:07", int 0707 is returned.
This method is useful for ordering time objects based on their chronological order.
*/

public class Time {
    String time;

    //
    //time takes in strings in the form "HH:MM" like "07:00"
    public Time(String time) {
        this.time= time;

    }



    public String getTime () {
        return this.time;
    }

    public void setTime (String time){
        this.time = time;
    }

    public int getHour() {
        char c1= this.time.charAt(0);
        String h1=Character.toString(c1);
        if (h1.equals("0")){           //if hour is like HH=07
            char c2=this.time.charAt(1);
            String h2=Character.toString(c2);
            return Integer.valueOf(h2);//then return single digit int like 7
        }
        else{
            String [] breakDownTime=this.time.split(":");//if hour is like HH=17
            String hour= breakDownTime[0];
            return Integer.valueOf(hour);//then return double digit integer like 17

        }
    }

    public int getMinute() {
        String [] hourMinSeparate=this.time.split(":");
        String m=hourMinSeparate[1];
        String [] m01=m.split("");
        String m0=m01[0];
        String m1=m01[1];
        if (m0.equals("0")){ //if minute is like MM=07
            return Integer.valueOf(m1);
        }
        else{return Integer.valueOf(m);} //if minute is like MM=17
    }

    public void setHour(int hour){
        String hourStr="";
        if (hour>=10){
            hourStr=Integer.toString(hour);
        }
        else{
            hourStr="0"+Integer.toString(hour);//if single digit int hour, need to format as HH=07
        }
        String [] hourMinSeparate = this.time.split(":");
        String str= hourStr +":"+ hourMinSeparate[1];
        this.setTime(str);

    }

    public void setMinute(int minute){
        String minuteStr="";
        if (minute>=10){
            minuteStr=Integer.toString(minute);
        }
        else{
            minuteStr="0"+Integer.toString(minute);//if single digit int minute, need to format as MM=07
        }
        String [] hourMinSeparate = this.time.split(":");
        String str2 =hourMinSeparate[0]+":"+minuteStr;
        this.setTime(str2);

    }

    public int getIntTime() {
        String timeString = this.time;
        timeString = timeString.replace(":", "");
        int startTimeInt = Integer.valueOf(timeString);
        return startTimeInt;
    }






}
