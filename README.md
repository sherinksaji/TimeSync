# TimeSync
TimeSync is an app that syncs Timetables. This app uses Firebase RealtimeDatabase.        





          ForgotPassword <--> LoginActivity <--> RegisterUserActivity(uses User)
						                        ^
  (uses viewMeetingsAdapter         |
  & meetingModel)	                  v
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
