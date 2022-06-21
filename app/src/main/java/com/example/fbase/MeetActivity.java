package com.example.fbase;

import static java.lang.String.valueOf;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MeetActivity extends AppCompatActivity implements View.OnClickListener{

    //dayButtons
    private Button MondayButton,TuesdayButton, WednesdayButton, ThursdayButton, FridayButton,BackButton;
    TextView meetingTitleTV, creatorNameTV,participantsTV;
    String meetingTitle;
    String meetingID;
    String creatorName;
    String creatorUid;
    String currentUser;

    ArrayList<String> participantsIDAL = new ArrayList<>();//arraylist that collects the current
                                                        //participants of the meeting
    DatabaseReference selectedUsersRef;

    RecyclerView recyclerView;

    ValueEventListener mondayVEL;
    ValueEventListener tuesdayVEL;
    ValueEventListener wednesdayVEL;
    ValueEventListener thursdayVEL;
    ValueEventListener fridayVEL;


    ValueEventListener valueEventListener2;

    //dayRefs
    DatabaseReference mondayRef;
    DatabaseReference tuesdayRef;
    DatabaseReference wednesdayRef;
    DatabaseReference thursdayRef;
    DatabaseReference fridayRef;

    //dayAdapters
    public MeetAdapter mondayAdapter;
    public MeetAdapter tuesdayAdapter;
    public MeetAdapter wednesdayAdapter;
    public MeetAdapter thursdayAdapter;
    public MeetAdapter fridayAdapter;

    //dayOccupied ArrayLists
    public ArrayList<String>mondayOccupied=new ArrayList<>();
    public ArrayList<String>tuesdayOccupied=new ArrayList<>();
    public ArrayList<String>wednesdayOccupied=new ArrayList<>();
    public ArrayList<String>thursdayOccupied=new ArrayList<>();
    public ArrayList<String>fridayOccupied=new ArrayList<>();


    //dayFree ArrayLists
    public List<MeetAdapter.timingData> mondayFree = new ArrayList<>();
    public List<MeetAdapter.timingData> tuesdayFree = new ArrayList<>();
    public List<MeetAdapter.timingData> wednesdayFree = new ArrayList<>();
    public List<MeetAdapter.timingData> thursdayFree = new ArrayList<>();
    public List<MeetAdapter.timingData> fridayFree = new ArrayList<>();



    TextView dayTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet);

        Intent intent=getIntent();

        currentUser=FirebaseAuth.getInstance().getCurrentUser().getUid();


        meetingTitle=intent.getStringExtra("meetingTitle");
        meetingID=intent.getStringExtra("meetingID");
        creatorName=intent.getStringExtra("creatorName");
        creatorUid=intent.getStringExtra("creatorUid");

        meetingTitleTV=(TextView) findViewById(R.id.meetingTitleTV);
        meetingTitleTV.setText(meetingTitle);

        creatorNameTV=(TextView) findViewById(R.id.creatorTV);
        creatorNameTV.setText("Creator: " +creatorName);

        participantsTV=(TextView) findViewById(R.id.participantsTV);
        getParticipants();



        MondayButton = (Button) findViewById(R.id.Monday);
        MondayButton.setOnClickListener(this);

        TuesdayButton = (Button) findViewById(R.id.Tuesday);
        TuesdayButton.setOnClickListener(this);

        WednesdayButton = (Button) findViewById(R.id.Wednesday);
        WednesdayButton.setOnClickListener(this);

        ThursdayButton = (Button) findViewById(R.id.Thursday);
        ThursdayButton.setOnClickListener(this);

        FridayButton = (Button) findViewById(R.id.Friday);
        FridayButton.setOnClickListener(this);

        mondayRef=FirebaseDatabase.getInstance().getReference().child("Monday");
        tuesdayRef=FirebaseDatabase.getInstance().getReference().child("Tuesday");
        wednesdayRef=FirebaseDatabase.getInstance().getReference().child("Wednesday");
        thursdayRef=FirebaseDatabase.getInstance().getReference().child("Thursday");
        fridayRef=FirebaseDatabase.getInstance().getReference().child("Friday");




        recyclerView
                = (RecyclerView)findViewById(
                R.id.recyclerView2);
        recyclerView.setLayoutManager(new LinearLayoutManager(MeetActivity.this));

        BackButton =(Button) findViewById(R.id.backToProfile);
        BackButton.setOnClickListener(this);


        mondayAdapter= new MeetAdapter(mondayFree, getApplication());
        tuesdayAdapter=new MeetAdapter(tuesdayFree,getApplication());
        wednesdayAdapter=new MeetAdapter(wednesdayFree,getApplication());
        thursdayAdapter=new MeetAdapter(thursdayFree,getApplication());
        fridayAdapter=new MeetAdapter(fridayFree,getApplication());







        dayTV=(TextView) findViewById(R.id.dayTV);
        dayTV.setText("Monday");
        recyclerView.setAdapter(mondayAdapter);

    }
    @Override
    protected void onStart() {
        super.onStart();
        /*setup(mondayRef,mondayOccupied,mondayFree,mondayAdapter);
        setup(tuesdayRef,tuesdayOccupied,tuesdayFree,tuesdayAdapter);
        setup(wednesdayRef,wednesdayOccupied,wednesdayFree,wednesdayAdapter);
        setup(thursdayRef,thursdayOccupied,thursdayFree,thursdayAdapter);
        setup(fridayRef,fridayOccupied,fridayFree,fridayAdapter);

         */
        mondaySetup();
        tuesdaySetup();
        wednesdaySetup();
        thursdaySetup();
        fridaySetup();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mondayRef.removeEventListener(mondayVEL);
        tuesdayRef.removeEventListener(tuesdayVEL);
        wednesdayRef.removeEventListener(wednesdayVEL);
        thursdayRef.removeEventListener(thursdayVEL);
        fridayRef.removeEventListener(fridayVEL);
        selectedUsersRef.removeEventListener(valueEventListener2);
    }



    //everytime we press a dayButton, we set dayTV to that specific day and swapAdapter of recyclerView to that
    //specific day
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Monday:

                dayTV.setText("Monday");
                recyclerView.swapAdapter(mondayAdapter,true);
                break;

            case R.id.Tuesday:

                dayTV.setText("Tuesday");
                recyclerView.swapAdapter(tuesdayAdapter,true);
                break;

            case R.id.Wednesday:

                dayTV.setText("Wednesday");
                recyclerView.swapAdapter(wednesdayAdapter,true);
                break;

            case R.id.Thursday:

                dayTV.setText("Thursday");
                recyclerView.swapAdapter(thursdayAdapter,true);
                break;

            case R.id.Friday:
                dayTV.setText("Friday");
                recyclerView.swapAdapter(fridayAdapter,true);
                break;

            case R.id.backToProfile:
                participantsIDAL.clear();
                startActivity(new Intent(this, ViewMeetingsActivity.class));
                break;

        }
    }

    //a method to update the participantsIDAL,the arraylist containing the current participants of the
    // meeting, with firebase database changes
    public void getParticipants(){
        selectedUsersRef= FirebaseDatabase.getInstance().getReference("Meetings").child(meetingID).child("selectedUsers");
        selectedUsersRef.addValueEventListener(valueEventListener2=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String selectedUsersStr = "Participants: ";
                participantsIDAL.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User u= postSnapshot.getValue(User.class);

                    participantsIDAL.add(u.getUid());

                    selectedUsersStr += u.getFullName();
                    selectedUsersStr+=", ";
                }
                selectedUsersStr=selectedUsersStr.substring(0,selectedUsersStr.length()-2);



                participantsTV.setText(selectedUsersStr);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(MeetActivity.this, "Database error! Please exit the page and try again!", Toast.LENGTH_LONG).show();
            }

        });


    }

    //getFreeSlots takes in a dayOccupied ArrayList with all the occupied period strings collected from firebase
    //it also takes in a dayFree ArrayList that will freshly populate it with free slots
    public void getFreeSlots(ArrayList dayOccupied,List dayFree){
        dayFree.clear();
        ArrayList DayAvailable = new ArrayList<String>();
        AvailableSlots AvailSlotDay = new AvailableSlots(dayOccupied);
        DayAvailable = AvailSlotDay.DailyAvailableSlots();

        for (Object t : DayAvailable) {
            String s = (String) t;
            dayFree.add(new MeetAdapter.timingData(s));
        }
    }

    //setup listens to firebase database changes at the specified database references
    //and collects participants' occupied timings in real time
    public void setup(DatabaseReference dayRef, ArrayList<String> dayOccupied, List<MeetAdapter.timingData> dayFree, MeetAdapter dayAdapter ){

        dayRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count=0;
                dayOccupied.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    Log.i("postSnapshot's key",postSnapshot.getKey());
                    Log.i("participantsIDAL",participantsIDAL.toString());
                    if (participantsIDAL.contains(postSnapshot.getKey())){
                        count+=1;
                        for (DataSnapshot eventShot:postSnapshot.getChildren()) {
                            Log.i("eventShot's value", String.valueOf(eventShot.getValue()));
                            Event event = eventShot.getValue(Event.class);
                            dayOccupied.add(event.getPeriod());
                        }
                    }
                    if (count==participantsIDAL.size()){
                        break;
                    }
                }
                getFreeSlots(dayOccupied,dayFree);
                dayAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void mondaySetup(){

        mondayRef.addValueEventListener(mondayVEL=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count=0;
                mondayOccupied.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    Log.i("postSnapshot's key",postSnapshot.getKey());
                    Log.i("participantsIDAL",participantsIDAL.toString());
                    if (participantsIDAL.contains(postSnapshot.getKey())){
                        count+=1;
                        for (DataSnapshot eventShot:postSnapshot.getChildren()) {
                            Log.i("eventShot's value", String.valueOf(eventShot.getValue()));
                            Event event = eventShot.getValue(Event.class);
                            mondayOccupied.add(event.getPeriod());
                        }
                    }
                    if (count==participantsIDAL.size()){
                        break;
                    }
                }
                getFreeSlots(mondayOccupied,mondayFree);
                mondayAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void tuesdaySetup(){

        tuesdayRef.addValueEventListener(tuesdayVEL=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count=0;
                tuesdayOccupied.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    Log.i("postSnapshot's key",postSnapshot.getKey());
                    Log.i("participantsIDAL",participantsIDAL.toString());
                    if (participantsIDAL.contains(postSnapshot.getKey())){
                        count+=1;
                        for (DataSnapshot eventShot:postSnapshot.getChildren()) {
                            Log.i("eventShot's value", String.valueOf(eventShot.getValue()));
                            Event event = eventShot.getValue(Event.class);
                            tuesdayOccupied.add(event.getPeriod());
                        }
                    }
                    if (count==participantsIDAL.size()){
                        break;
                    }
                }
                getFreeSlots(tuesdayOccupied,tuesdayFree);
                tuesdayAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void wednesdaySetup(){

        wednesdayRef.addValueEventListener(wednesdayVEL=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count=0;
                wednesdayOccupied.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    Log.i("postSnapshot's key",postSnapshot.getKey());
                    Log.i("participantsIDAL",participantsIDAL.toString());
                    if (participantsIDAL.contains(postSnapshot.getKey())){
                        count+=1;
                        for (DataSnapshot eventShot:postSnapshot.getChildren()) {
                            Log.i("eventShot's value", String.valueOf(eventShot.getValue()));
                            Event event = eventShot.getValue(Event.class);
                            wednesdayOccupied.add(event.getPeriod());
                        }
                    }
                    if (count==participantsIDAL.size()){
                        break;
                    }
                }
                getFreeSlots(wednesdayOccupied,wednesdayFree);
                wednesdayAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void thursdaySetup(){

        thursdayRef.addValueEventListener(thursdayVEL=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count=0;
                thursdayOccupied.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    Log.i("postSnapshot's key",postSnapshot.getKey());
                    Log.i("participantsIDAL",participantsIDAL.toString());
                    if (participantsIDAL.contains(postSnapshot.getKey())){
                        count+=1;
                        for (DataSnapshot eventShot:postSnapshot.getChildren()) {
                            Log.i("eventShot's value", String.valueOf(eventShot.getValue()));
                            Event event = eventShot.getValue(Event.class);
                            thursdayOccupied.add(event.getPeriod());
                        }
                    }
                    if (count==participantsIDAL.size()){
                        break;
                    }
                }
                getFreeSlots(thursdayOccupied,thursdayFree);
                thursdayAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void fridaySetup(){

        fridayRef.addValueEventListener(fridayVEL=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count=0;
                fridayOccupied.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    Log.i("postSnapshot's key",postSnapshot.getKey());
                    Log.i("participantsIDAL",participantsIDAL.toString());
                    if (participantsIDAL.contains(postSnapshot.getKey())){
                        count+=1;
                        for (DataSnapshot eventShot:postSnapshot.getChildren()) {
                            Log.i("eventShot's value", String.valueOf(eventShot.getValue()));
                            Event event = eventShot.getValue(Event.class);
                            fridayOccupied.add(event.getPeriod());
                        }
                    }
                    if (count==participantsIDAL.size()){
                        break;
                    }
                }
                getFreeSlots(fridayOccupied,fridayFree);
                fridayAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public void delete(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Confirm");
        if (currentUser.equals(creatorUid)){
            builder.setMessage("By deleting the meeting, you delete the meeting for all.");
        }
        else{
            builder.setMessage("Leave Meeting.");
        }
        builder.setCancelable(false);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (currentUser.equals(creatorUid)){
                    deleteForAll();
                }
                else{
                    leaveMeeting();
                }
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }


    public void leaveMeeting(){
        FirebaseDatabase.getInstance().getReference()
                .child("myMeetings").child(currentUser).child(meetingID).removeValue();

        FirebaseDatabase.getInstance().getReference("Meetings").child(meetingID).child("selectedUsers").child(currentUser).removeValue();

        startActivity(new Intent(this, ViewMeetingsActivity.class));
    }

    public void deleteForAll(){
        for (String p:participantsIDAL){
            FirebaseDatabase.getInstance().getReference()
                    .child("myMeetings").child(p).child(meetingID).removeValue();
        }

        FirebaseDatabase.getInstance().getReference("Meetings").child(meetingID).removeValue();
        startActivity(new Intent(this, ViewMeetingsActivity.class));
    }

}