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
|	  |	  |->creatorName:"bobby"
|	  |	  |->creatorUid:"uid"
|	  |	  |->meetingKey:"meetingKey"
|	  |	  |->meetingTitle:"abc"
|	  |	  |->selectedUsers
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
TimetableActivity shows the individual timetable of the user.The TimeSync app only intends to
display and sync timetables on a Monday to Friday basis given that the firebase database is limited.

In the onCreate method, we set the layout of the activity as activity_timetable.xml.Next, we
find out who the current user's user identification and assign it to myUID.
We assign Button member variables buttonMon,buttonTues,buttonWed,buttonThu,buttonFri to resource ids
buttonMonday, buttonTuesday,buttonWednesday,buttonThursday and buttonFriday of the XML file.
We assign TextView member variable day to resource id textviewDay1.
We assign Button member variable addButton to resource id addButton of the XML file.
We assign Button member variable backButton to resource id backToProfile of the XML file.
We assign Recyclerview member variable recyclerView to resource id timetableRV of the XML file.
For each day,
firstly,we create a FirebaseRecycler<Event> options:
FirebaseRecycler<Event> options=new FirebaseRecyclerOptions.Builder<Event>()
                    .setQuery(FirebaseDatabase.getInstance().getReference()
                    .child("<day>").child(myUID).orderByChild("startTimeInt"), Event.class)
                    .build();
<Event> of FirebaseRecycler<Event> options just means that the elements inside options are Event
class objects.
secondly, we create TimetableAdapter object by passing in the respective day's options,
myUID (current user's user id) and the respective day as a string (e.g."Monday").
The TimetableAdapter class is an extension of of the FirebaseRecyclerAdapter. The TimetableAdapter
will always be aware of any changes to the firebase realtime database once it starts listening to
firebase realtime database.

After that, we set the recyclerView's adapter as monAdapter and set the day textview as "Monday".
By doing this settings, we make monday timetable the default view that the user sees.

In the onStart method, we make all the adapters start listening to firebase database changes.
This means that if the firebase database changes, then the adapters will also change from the
start of the activity's lifecycle.

In the onStop method, we make all the adapters stop listening to firebase database changes at the
stop of the activity's lifecycle.


In the onClick method, we write cases of what needs to happen when the user clicks the buttons.
If user clicks addButton, an intent to the AddEventActivity must take place and information of which
day to add the event to must be passed into AddEventActivity. The information can just be taken from
the TextView day as we use TextView day to keep track of what day it is (further explained in the
day buttons section).
If the user clicks backButton(R.id backToProfile), then an intent to HomeActivity must take place.
To explain the cases of the day buttons, lets take the example of R.id.buttonMonday:
When the user clicks button with R.id.buttonMonday:
Firstly, TextView day is set to "Monday". So in essence, whenever user clicks a day button, TextView
is set to that particular day. So the TextView can be used to keep track of what day it is whenever
user needs to add new event.
Secondly, recyclerView's adapter is swapped to monAdapter. So in essence, whenever user clicks a
day button, recyclerView's adapter is swapped to the adapter of that particular day to show the
events of that particular day.



*/

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class TimetableActivity extends AppCompatActivity implements View.OnClickListener{

    RecyclerView recyclerView;
    TimetableAdapter monAdapter;
    TimetableAdapter tueAdapter;
    TimetableAdapter wedAdapter;
    TimetableAdapter thursAdapter;
    TimetableAdapter friAdapter;
    Button buttonMon,buttonTues,buttonWed,buttonThu,buttonFri,addButton,backButton;

    String myUID;
    TextView day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        myUID= FirebaseAuth.getInstance ().getCurrentUser ().getUid ();

        buttonMon=(Button)findViewById(R.id.buttonMonday);
        buttonMon.setOnClickListener(this);
        buttonTues=(Button)findViewById(R.id.buttonTuesday);
        buttonTues.setOnClickListener(this);
        buttonWed=(Button)findViewById(R.id.buttonWednesday);
        buttonWed.setOnClickListener(this);
        buttonThu=(Button)findViewById(R.id.buttonThursday);
        buttonThu.setOnClickListener(this);
        buttonFri=(Button)findViewById(R.id.buttonFriday);
        buttonFri.setOnClickListener(this);


        
        day=findViewById(R.id.textViewDay1);
        day.setText("Monday");

        addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(this);

        backButton = (Button) findViewById(R.id.backToProfile);
        backButton.setOnClickListener(this);

        recyclerView = (RecyclerView)findViewById(R.id.timetableRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //just get the data directly from firebase
        FirebaseRecyclerOptions<Event> monOptions =
                new FirebaseRecyclerOptions.Builder<Event>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Monday").child(myUID).orderByChild("startTimeInt"), Event.class)
                        .build();

        FirebaseRecyclerOptions<Event> tueOptions =
                new FirebaseRecyclerOptions.Builder<Event>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Tuesday").child(myUID).orderByChild("startTimeInt"), Event.class)
                        .build();

        FirebaseRecyclerOptions<Event> wedOptions =
                new FirebaseRecyclerOptions.Builder<Event>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Wednesday").child(myUID).orderByChild("startTimeInt"), Event.class)
                        .build();

        FirebaseRecyclerOptions<Event> thursOptions =
                new FirebaseRecyclerOptions.Builder<Event>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Thursday").child(myUID).orderByChild("startTimeInt"), Event.class)
                        .build();

        FirebaseRecyclerOptions<Event> friOptions =
                new FirebaseRecyclerOptions.Builder<Event>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Friday").child(myUID).orderByChild("startTimeInt"), Event.class)
                        .build();


        monAdapter = new TimetableAdapter(monOptions,myUID,"Monday");
        tueAdapter = new TimetableAdapter(tueOptions,myUID,"Tuesday");
        wedAdapter = new TimetableAdapter(wedOptions,myUID,"Wednesday");
        thursAdapter = new TimetableAdapter(thursOptions,myUID,"Thursday");
        friAdapter = new TimetableAdapter(friOptions,myUID,"Friday");

        recyclerView.setAdapter(monAdapter);
    }
    @Override
    protected void onStart() {
        super.onStart();
        monAdapter.startListening();
        tueAdapter.startListening();
        wedAdapter.startListening();
        thursAdapter.startListening();
        friAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        monAdapter.stopListening();
        tueAdapter.stopListening();
        wedAdapter.stopListening();
        thursAdapter.stopListening();
        friAdapter.stopListening();
    }



    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {

            case R.id.addButton:
                Intent toAddEvent=new Intent(this, AddEventActivity.class);
                toAddEvent.putExtra("day",day.getText().toString().trim());
                toAddEvent.putExtra("uid",myUID);
                startActivity(toAddEvent);
                break;

            case R.id.backToProfile:
                startActivity(new Intent(this, HomeActivity.class));
                break;

            case R.id.buttonMonday:
                day.setText("Monday");
                recyclerView.swapAdapter(monAdapter,true);
                break;


            case R.id.buttonTuesday:
                day.setText("Tuesday");
                recyclerView.swapAdapter(tueAdapter,true);
                break;

            case R.id.buttonWednesday:
                day.setText("Wednesday");
                recyclerView.swapAdapter(wedAdapter,true);
                break;

            case R.id.buttonThursday:
                day.setText("Thursday");
                recyclerView.swapAdapter(thursAdapter,true);
                break;

            case R.id.buttonFriday:
                day.setText("Friday");
                recyclerView.swapAdapter(friAdapter,true);
                break;
        }
    }




}