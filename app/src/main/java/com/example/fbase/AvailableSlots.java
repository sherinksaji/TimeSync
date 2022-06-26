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

 AvailableSlots class takes in a String array of OccuppiedTimeSlotsOfDay.
 AvailableSlots has array defaultSlots that contains half an hour time period strings
 from 07:00 to 22:00 as we assume that people only want to meet in those hours.
 availableSlots is cloned from defaultSlots. void popOccuppiedTimeSlots (DirtyTimeSet t ) rounds
 the DirtyTimeSet ,t, and finds the  DurationInHalfHours. Then, t.matchStart() and t.matchEnd() and
 all the halfAnHours between are removed from availableSlots.
 ArrayList <String> DailyAvailableSlots returns all the available slots that are left.
 */
import java.util.ArrayList;
import java.util.Arrays;

public class AvailableSlots {
    //defaultSlots has potentially every halfAnHour slot a group can possibly meet up in
    //we assume that people only want to meet from 07:00 to 22:00
    public static ArrayList<String> defaultSlots= new ArrayList<String>( Arrays.asList("07:00 - 07:30","07:30 - 08:00","08:00 - 08:30","08:30 - 09:00","09:00 - 09:30","09:30 - 10:00","10:00 - 10:30",
            "10:30 - 11:00","11:00 - 11:30","11:30 - 12:00","12:00 - 12:30","12:30 - 13:00","13:00 - 13:30","13:30 - 14:00","14:00 - 14:30","14:30 - 15:00","15:00 - 15:30","15:30 - 16:00",
            "16:00 - 16:30","16:30 - 17:00","17:00 - 17:30","17:30 - 18:00","18:00 - 18:30","18:30 - 19:00","19:00 - 19:30","19:30 - 20:00","20:00 - 20:30","20:30 - 21:00","21:00 - 21:30",
            "21:30 - 22:00"));
    //availableSlots is the array that is eventually returned in DailyAvailableSlots method
    public ArrayList<String> availableSlots= (ArrayList) defaultSlots.clone();
    //OccupiedTimeslots is the array containing timePeriods of all the selected members from firebase.
    //The timePeriods in OccupiedTimeSlots can overlap and be repeated
    public ArrayList<String> OccupiedTimeSlots=new ArrayList <String> ();



    AvailableSlots(ArrayList <String> OccupiedTimeSlotsOfDay){
        this.defaultSlots=defaultSlots;
        this.availableSlots=availableSlots;
        this.OccupiedTimeSlots=OccupiedTimeSlotsOfDay;


    }

    public void reset(){
        this.availableSlots=new ArrayList<String>();
        this.availableSlots=(ArrayList) defaultSlots.clone();
    }

    //popOccupiedSlots(DirtyTimeSet t) takes in a DirtyTimeSet input and will remove all the timeslots in
    //availableSlots that are also in the DirtyTimeSet
    public void popOccupiedSlots(DirtyTimeSet t){
        //rounding a startTime like 07:01 to 07:00
        t.roundStartTime();
        //rounding an endTime like 07:55 to 08:00
        t.roundEndTime();
        //now that t's startTime and endTime are already rounded,
        //we find the number of half an hours within t's duration
        //and assign it to the number of occupied slots
        int OccupiedSlotsInt=t.durationInHalfHours();
        //t.matchStart() and t.matchEnd() can be matched to corresponding slots in this.availableSlots


        //if there is only one occupiedSlot, then we only have to
        //remove t.matchStart() from availableSlots because
        //for a t like 07:00 - 07:30, both t.matchStart() and t.matchEnd() are 07:00 - 07:30
        //--> we only have the timeslot once
        if (OccupiedSlotsInt==1){
            if (this.availableSlots.contains(t.matchStart())) {
                this.availableSlots.remove(t.matchStart());
            }
        }

        //if there is 2 occupiedSlots,like for t="07:00 - 08:00",
        //t.matchStart() will be "07:00 - 07:30" and t.matchEnd() will be "07:30 - 08:00"
        //they are both different and we have to remove both of them from availableSlots

        if (OccupiedSlotsInt==2){
            if (this.availableSlots.contains(t.matchStart())) {
                this.availableSlots.remove(t.matchStart());
            }
            if (this.availableSlots.contains(t.matchEnd())) {
                this.availableSlots.remove(t.matchEnd());
            }

        }
        //if OccupiedSlotsInt is greater than 2, then we need to remove t.matchStart() and t.matchEnd() and all the slots in
        //between

        else{
            int index1=this.defaultSlots.indexOf (t.matchStart());
            int index2=this.defaultSlots.indexOf (t.matchEnd());
            for (int i = index1; i <=index2; i++) {
                String occupiedSlot=this.defaultSlots.get(i);
                if (this.availableSlots.contains(occupiedSlot)){
                    this.availableSlots.remove(occupiedSlot);
                }



            }
        }


    }
    //DailyAvailableSlots returns an this.availableSlots that is clean of any of the timeperiods inside
    //the this.OccupiedTimeSlots
    public ArrayList<String> DailyAvailableSlots (){
        reset();
        for (String OccupiedTimeSlot: this.OccupiedTimeSlots){

            TimePeriod OccupiedTimeSet=new TimePeriod(OccupiedTimeSlot);
            DirtyTimeSet DirtyTime= new DirtyTimeSet(OccupiedTimeSet);
            popOccupiedSlots(DirtyTime);
        }

        return this.availableSlots;

    }


}
