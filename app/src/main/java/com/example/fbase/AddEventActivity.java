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
        Intent intentFromProfile = getIntent();
        myUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        day = intentFromProfile.getStringExtra("day");
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
        checkInput();
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