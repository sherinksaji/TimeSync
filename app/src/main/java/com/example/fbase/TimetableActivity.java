package com.example.fbase;

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
    ImageButton upButton,downButton;
    String myUID;
    TextView day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        Intent intent = getIntent();
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

        recyclerView = (RecyclerView)findViewById(R.id.mondayTimetableRV);
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