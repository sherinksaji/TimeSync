package com.example.fbase;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;
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


This is how the firebase database looks like(i put this almost wherever relevant for ease of
seeing):
Link to firebase database
|
|->Friday
|   |->uid
|	|   |->eventKey
|	|   |		|->key:eventKey
|	|   |		|->period:"08:00 - 09:00"
|	|   |		|->startTimeInt:800
|	|   |		|->title:"CS"
|	|   |->.
|	|	   .
|   |      .
|	|->.
|      .
|      .
|->Meetings
|     |->meetingKey
|	  |	|->creatorName:"bobby"
|	  |	|->creatorUid:"uid"
|	  |	|->meetingKey:"meetingKey"
|	  |	|->meetingTitle:"abc"
|	  |	|->selectedUsers
|	  |			|->uid
|	  |			|    |->email:"robin@gmail.com"
|     |         |    |->fullName:"robin"
|     |         |    |->uid:"uid"
|     |         |->.
|     |            .
|     |            .
|     |->.
|        .
|        .
|->Members
|	|->uid
|	|    |->email:"robin@gmail.com"
|	|    |->fullName:"robin"
|	|    |->uid:"uid"
|	|->.
|      .
|      .
|
|->Monday
|   |->uid
|	|   |->eventKey
|	|   |		|->key:eventKey
|	|   |		|->period:"08:00 - 09:00"
|	|   |		|->startTimeInt:800
|	|   |		|->title:"CS"
|	|   |->.
|	|	   .
|   |      .
|	|->.
|      .
|      .
|->Thursday
|   |->uid
|	|   |->eventKey
|	|   |		|->key:eventKey
|	|   |		|->period:"08:00 - 09:00"
|	|   |		|->startTimeInt:800
|	|   |		|->title:"CS"
|	|   |->.
|	|	   .
|   |      .
|	|->.
|      .
|      .
|->Tuesday
|   |->uid
|	|   |->eventKey
|	|   |		|->key:eventKey
|	|   |		|->period:"08:00 - 09:00"
|	|   |		|->startTimeInt:800
|	|   |		|->title:"CS"
|	|   |->.
|	|	   .
|   |      .
|	|->.
|      .
|      .
|->Wednesday
|     |->uid
|	  |   |->eventKey
|	  |   |		|->key:eventKey
|	  |   |		|->period:"08:00 - 09:00"
|	  |   |		|->startTimeInt:800
|	  |   |		|->title:"CS"
|	  |   |->.
|	  |	     .
|     |      .
|	  |->.
|        .
|        .
|->myMeetings
	|->uid
    |    |->meetingKey
	|    |	     |->creatorName:"bobby"
	|    |	     |->creatorUid:"uid"
	|    |	     |->meetingKey:"meetingKey"
	|    |	     |->meetingTitle:"abc"
    |    |->.
	|	    .
    |       .
    |->.
       .
       .

In the onCreate method,we set R.layout.activity_addevent as layout of the AddEventActivity
We get the intent from TimetableActivity, intentFromTimetable
We find the current user of the app and assign it to member variable myUID.
We find the day to add new Event to from intentFromTimetable and assign it to member variable day.
We assign member Button variable addButton to its corresponding resource id in the
R.layout.activity_addevent.
We make member DatabaseReference ref point to this node in the firebase database:
Link to firebase database
|
|->day
|   |->uid         <----ref points to this node

For example, if day="Monday" and uid="4NyKMXCovHQB4Y4OQeuXSug9dim1"
Then ref will pint to this node:
Link to firebase database
|
|->Monday
|   |->4NyKMXCovHQB4Y4OQeuXSug9dim1         <----ref points to this node
However if the nodes, do not exist yet, the nodes are created and then ref points to the node.
We assign member variable editTextTitle, stopButton and startButton to their corresponding resource
ids in R.layout.activity_addevent.
We assign false to member variable overlap.
Then, we call getfbData().

In onClick method, for case R.id.addButton, we call checkInput() method and
for case R.id.backToMyTimetable, set intent back to TimetableActivity class.

In activity_addevent, in Button with R.id. startButton,we set public void popStartTimePicker
as onClick method. In public void popStartTimePicker(View view)
we open a TimePickerDialog and we set a onTimeSetListener. We override the public void
onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute).
 Member int variable beginHour is the hour segment of the start time of event.
 int selectedHour is assigned to member int variable beginHour.
 Member variable beginMinute is the minute segment of the start time of event.
 int selectedMinute is assigned to member int variable beginMinute.
 Then we make a string in the form "beginHour:beginMinute" and assign it to member variable
 String beginTime.Then we set text of startButton with beginTime.
 This gives a nice effect during app run time: before pressing the startButton, the text on it is
 "Select Start Time". After pressing the button and selecting the start time on the
 popStartTimePicker, the the text immediately changes to beginTime like 07:00.
 This allows users to see the time that they selected.

 In activity_addevent, in Button with R.id. stopButton,we set public void popStopTimePicker
 as onClick method. In public void popStopTimePicker(View view)
 we open a TimePickerDialog and we set a onTimeSetListener. We override the public void
 onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute).
 Member int variable finishHour is the hour segment of the stop time of event.
 int selectedHour is assigned to member int variable finishHour.
 Member variable finishMinute is the minute segment of the stop time of event.
 int selectedMinute is assigned to member int variable finishMinute.
 Then we make a string in the form "finishHour:finishMinute" and assign it to member variable
 String finishTime.Then we set text of stopButton with finishTime.
 This gives a nice effect during app run time: before pressing the stopButton, the text on it is
 "Select Stop Time". After pressing the button and selecting the stop time on the
 popStartTimePicker, the the text immediately changes to finishTime like 07:00.
 This allows users to see the time that they selected.

 The user flow in the AddEvent Activity is that the user keys in the Title of the event in
 the EditTextTitle and selects the start time and the stop time of the event. Lastly, he
 presses the ADD EVENT button (addButton) to add the event.

 When the addButton is pressed, input is checked using checkInput().
 First we check whether Title, beginTime or finishTime is null and send toast messages
 accordingly. Then, we check that beginTime is before  finishTime by using getIntTime()
 method from Time class. Now, we can make a valid TimePeriod timePeriod with beginTime and
 finishTime. Next we check that during timePeriod, there is no event already running
 during that timePeriod.  We have been collecting existing events's periods from firebase
 database in the getFbData() method. We use the periods to create TimePeriod objects  and we
 have been storing it in the fbData<TimePeriod> arraylist.
 So for each TimePeriod object in fbData, we check whether the TimePeriod object
 overlaps with new timePeriod. If there is coincident TimePeriod, then we send toast message
 that there already exists an event during that time period.
 If all the inputs are valid, then we call addDatatoFirebase(title,timePeriod.toString()).
 We pass title and timePeriod.toString().

 When adding event to firebase, we need to keep in mind that we need to find the event again
 in firebase when the user needs to delete event. Thus, we need to find it in a key value
 pair like eventKey:eventObject and eventKey must be stored in the key attribute of event.





*/
public class AddEventActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextTitle;

    Button startButton,stopButton;
    int beginHour, beginMinute,finishHour,finishMinute;
    String beginTime,finishTime;
    String myUID;

    DatabaseReference ref;
    Button addButton,backToMyTimetable;
    ValueEventListener valueEventListener;
    Boolean overlap;
    ArrayList<TimePeriod>fbData=new ArrayList<>();

    private static String day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addevent);
        Intent intentFromTimetable = getIntent();
        myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        day = intentFromTimetable.getStringExtra("day");
        addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(this);
        ref = FirebaseDatabase.getInstance().getReference().child(day).child(myUID);

        backToMyTimetable = (Button) findViewById(R.id.backToMyTimetable);
        backToMyTimetable.setOnClickListener(this);

        editTextTitle = (EditText) findViewById(R.id.editTextTitle);

        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        overlap = false;
        getfbData();
    }


    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.addButton:
                checkInput();
                break;
           case R.id.backToMyTimetable:
                startActivity(new Intent(this, TimetableActivity.class));
                break;
        }
    }
    public void popStartTimePicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                beginHour = selectedHour;
                beginMinute = selectedMinute;
                beginTime=String.format(Locale.getDefault(), "%02d:%02d",beginHour, beginMinute);
                startButton.setText(String.format(beginTime));
            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, beginHour, beginMinute, true);

        timePickerDialog.setTitle("Select Start Time");
        timePickerDialog.show();
    }

    public void popEndTimePicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                finishHour = selectedHour;
                finishMinute = selectedMinute;
                finishTime=String.format(Locale.getDefault(), "%02d:%02d",finishHour, finishMinute);
                stopButton.setText(finishTime);
            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, finishHour, finishMinute, true);

        timePickerDialog.setTitle("Select Stop Time");
        timePickerDialog.show();
    }


    public void getfbData(){
        fbData.clear();

        ref.addListenerForSingleValueEvent(valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Event event = postSnapshot.getValue(Event.class);
                    TimePeriod p=new TimePeriod(event.getPeriod());

                    fbData.add(p);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(AddEventActivity.this,"Sorry,exit the page and try again ",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void checkInput() {

        //checking for null input
        String title=editTextTitle.getText().toString().trim();
        if (title.isEmpty()){
            Toast.makeText(AddEventActivity.this,"Title is required!",Toast.LENGTH_LONG).show();
            return;
        }
        if (beginTime==null) {
            Toast.makeText(AddEventActivity.this,"Start time is required!",Toast.LENGTH_LONG).show();
            return;
        }
        if (finishTime==null) {
            Toast.makeText(AddEventActivity.this,"Stop time is required!",Toast.LENGTH_LONG).show();
            return;
        }

        int beginTimeInt=new Time (beginTime).getIntTime();
        int finishTimeInt=new Time (finishTime).getIntTime();

        //checking that stop time input is not before start time input
        if (finishTimeInt<beginTimeInt){
            Toast.makeText(AddEventActivity.this,"Stop time cannot be earlier than Start time!",Toast.LENGTH_LONG).show();
            stopButton.setText("SELECT STOP TIME");
            return;
        }

        //now that beginTime and finishTime are not null and beginTime is before finishTime,
        //we can create a valid TimePeriod
        TimePeriod timePeriod=new TimePeriod(beginTime+" - "+finishTime);

        //check that during the new timePeriod created, there is no event already running
        //during that timePeriod
        //we get existing events from firebase and for each event' period we check if the
        //period overlaps with new timePeriod

        for (TimePeriod p:fbData){
            Log.i("data",p.toString());
            //for example:
            //p:09:00 - 10:00 => 900 - 1000
            //timePeriod:09:00 - 10:00 => 900-1000
            //         900<1000                                                      900<1000
            overlap = (p.startTime.getIntTime() < timePeriod.endTime.getIntTime()) && (timePeriod.startTime.getIntTime() < p.endTime.getIntTime());
            if (overlap==true){
                overlap=false;
                Log.i("Coincident",p.toString());
                Toast.makeText(AddEventActivity.this,"There is already an event during "+p.toString()+".Please select other times.",Toast.LENGTH_LONG).show();
                startButton.setText("SELECT START TIME");
                stopButton.setText("SELECT STOP TIME");
                return;
            }
        }

        //now that we confirmed that there is no other event running during that timePeriod, we can
        //proceed to create an event in addDatatoFirebase

        addDatatoFirebase(title,timePeriod.toString());


    }
    private void addDatatoFirebase(String title,String period)
    {

        String key = ref.push().getKey();
        Event event=new Event(title,period,key);
        //when we push the event object into the firebase, we need to keep track of the key that the event object
        //is attached to so that when we want to delete the event in the future, we can identify the event by its key
        //So, we make key as an attribute of event before pushing it into the firebase
        ref.child(key).setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                   Toast.makeText(AddEventActivity.this,"Event has been added successfully", Toast.LENGTH_LONG).show();

                }
                else {

                    Toast.makeText(AddEventActivity.this,"Failed to add event! "+task.getException().getMessage(),Toast.LENGTH_LONG).show();

                }
            }
        });

        startActivity(new Intent(this, TimetableActivity.class));




    }

}