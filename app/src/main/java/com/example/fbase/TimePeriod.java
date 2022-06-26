package com.example.fbase;

/**
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

TimePeriod class constructor requires a parameter String timePeriod in the 24 HR Clock form
"HH:MM<space>-<space>HH:MM"(e.g. "07:00 - 07:30").
Parameter String timePeriod is split by regex " - " to form a String array,startEndSeparate.
The first element of startEndSeparate is used as parameter for a new Time object. The new time
object is assigned to class attribute this.startTime.
The second element of startEndSeparate is used as parameter for another new Time object. This
new time object is assigned to class attribute this.endTime.

public void setTimePeriod requires a parameter String timePeriod in the 24 HR Clock form
"HH:MM<space>-<space>HH:MM"(e.g. "07:00 - 07:30").
Parameter String timePeriod is split by regex " - " to form a String array,startEndSeparate.
The first element of startEndSeparate is used as parameter for a new Time object. The new time
object is assigned to class attribute this.startTime.
The second element of startEndSeparate is used as parameter for another new Time object. This
new time object is assigned to class attribute this.endTime.
If parameter String timePeriod is "07:00 - 07:30", this.startTime.getTime()= "07:00"
and this.endTime.getTime()="07:30"


public String getTimePeriod() returns a string in the form "HH:MM - HH:MM" representing
start time - end time. e.g. "07:00 - 07:30"

public String toString() returns a string in the form "HH:MM - HH:MM" representing
start time - end time. e.g. "07:00 - 07:30"


public int [] Duration () returns an int array
[difference in hours between this.startTime and this.endTime,
difference in minutes between this.startTime and this.endTime]
e.g. if parameter String timePeriod="07:00 - 10:30",
then this.startTime="07:00" and this.endTime="10:30" (these assignments are done in the constructor)
Then, the int array returned is [3,30].







*/

public class TimePeriod {
    Time startTime;
    Time endTime;

    //TimePeriod takes in String like "07:00 - 08:00"
    public TimePeriod(String timePeriod){
        String [] startEndSeparate=timePeriod.split(" - ");
        this.startTime=new Time(startEndSeparate[0]);
        this.endTime=new Time(startEndSeparate[1]);
    }

    public void setTimePeriod(String timePeriod){
        String [] startEndSeparate=timePeriod.split(" - ");
        this.startTime.setTime(startEndSeparate[0]);
        this.endTime.setTime(startEndSeparate[1]);
    }

    public String getTimePeriod(){
        return this.startTime.getTime()+" - "+this.endTime.getTime();
    }

    //returns an array [diffInHours,diffInMins]
    public int [] Duration(){
        int diffInHours;

        if (this.endTime.getHour()!=0) {
            diffInHours = this.endTime.getHour() - this.startTime.getHour();
        }
        else{diffInHours=24-this.startTime.getHour();}
        int diffInMins=this.endTime.getMinute()-this.startTime.getMinute();
        if (diffInMins<0){//borrow 60 minutes from diffInHours
            diffInHours-=1;
            diffInMins+=60;
        }
        int [] duration=new int[2];
        duration[0]=diffInHours;
        duration[1]=diffInMins;

        return duration;
    }

    @Override
    public String toString(){
        return this.startTime.getTime()+" - "+this.endTime.getTime();
    }


}
