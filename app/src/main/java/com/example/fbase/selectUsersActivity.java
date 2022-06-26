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
|	  |	   |->creatorName:"bobby"
|	  |	   |->creatorUid:"uid"
|	  |	   |->meetingKey:"meetingKey"
|	  |	   |->meetingTitle:"abc"
|	  |	   |->selectedUsers
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
*/
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class selectUsersActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    selectUsersAdapter mainAdapter;

    RecyclerView selectedUsersRV;
    showSelectedUsersAdapter selectedUsersAdapter;

    TextView selectedUsersTV;

    String meetingKey;
    String meetingTitle;
    String creatorID;
    String creatorName;




    Button deleteMeetingButton,doneButton;

    DatabaseReference selectedUsersRef;

    meetingModel MeetingModel;

    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_users);

        Intent intent=getIntent();

        meetingKey=intent.getStringExtra("meetingKey");
        meetingTitle=intent.getStringExtra("meetingTitle");
        creatorID=intent.getStringExtra("creatorID");
        creatorName=intent.getStringExtra("creatorName");


        MeetingModel=new meetingModel(meetingKey,meetingTitle,creatorID,creatorName);





        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView=(SearchView) findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processsearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processsearch(s);
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mainAdapter.stopListening();
                return false;
            }
        });


        selectedUsersRV=(RecyclerView) findViewById(R.id.selectedUsersRV);
        selectedUsersRV.setLayoutManager(new LinearLayoutManager(this));


       FirebaseRecyclerOptions<User> selectedUsers =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Meetings").child(meetingKey).child("selectedUsers"), User.class)
                        .build();

       selectedUsersAdapter=new showSelectedUsersAdapter(selectedUsers,MeetingModel);
       selectedUsersRV.setAdapter(selectedUsersAdapter);





        selectedUsersRef=FirebaseDatabase.getInstance().getReference("Meetings").child(meetingKey).child("selectedUsers");

        selectedUsersTV=(TextView) findViewById(R.id.SelectedUsersStr);
        //showSelectedUsers();
        deleteMeetingButton=(Button)findViewById(R.id.cancelButton);
        doneButton=(Button)findViewById(R.id.doneButton);
        deleteMeetingButton.setOnClickListener(this);
        doneButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.cancelButton:
                deleteMeeting();
                break;

            case R.id.doneButton:
                linkMeeters();
                break;


        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        selectedUsersAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        selectedUsersAdapter.stopListening();
        mainAdapter.stopListening();
    }



    private void processsearch(String s)
    {
        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Members").orderByChild("fullName").startAt(s), User.class)
                        .build();

        mainAdapter = new selectUsersAdapter(options,MeetingModel);
        mainAdapter.startListening();
        recyclerView.setAdapter(mainAdapter);

    }


    public void deleteMeeting(){
        FirebaseDatabase.getInstance().getReference("Meetings").child(meetingKey).removeValue();
        Toast.makeText(selectUsersActivity.this, "The meeting has been successfully canceled!", Toast.LENGTH_LONG).show();
        startActivity(new Intent(selectUsersActivity.this, ViewMeetingsActivity.class));
    }

    public void linkMeeters(){

        selectedUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User u= postSnapshot.getValue(User.class);
                    FirebaseDatabase.getInstance().getReference()
                            .child("myMeetings").child(u.getUid()).child(meetingKey).setValue(MeetingModel);

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(selectUsersActivity.this, "Database error! Please exit the page and try again!", Toast.LENGTH_LONG).show();
            }

        });
        Toast.makeText(selectUsersActivity.this, "The meeting has been successfully created!", Toast.LENGTH_LONG).show();
        Intent toViewMeetings=new Intent(this, ViewMeetingsActivity.class);
        startActivity(toViewMeetings);

    }








}