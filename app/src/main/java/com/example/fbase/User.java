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


*/
public class User {
    public String fullName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String email;
    public String uid;



    public User(){
    }

    public User(String fullName, String email, String uid) {
        this.fullName = fullName.toLowerCase();
        this.email = email;
        this.uid= uid;


    }

}
