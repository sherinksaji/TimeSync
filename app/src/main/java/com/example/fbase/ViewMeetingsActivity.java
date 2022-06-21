package com.example.fbase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewMeetingsActivity extends AppCompatActivity implements View.OnClickListener {
    EditText meetingTitleEditText;
    Button addMeetingButton;
    Button backButton;
    DatabaseReference meetingsRef;
    DatabaseReference creatorsRef;
    ValueEventListener creatorVEL;
    User currentUser;
    String currentUserID;

    RecyclerView recyclerView;
    viewMeetingsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_meetings);

        meetingTitleEditText=findViewById(R.id.meetingTitleEditText);
        addMeetingButton=findViewById(R.id.addMeetingButton);
        addMeetingButton.setOnClickListener(this);
        backButton=findViewById(R.id.backButton);
        backButton.setOnClickListener(this);
        meetingsRef= FirebaseDatabase.getInstance().getReference("Meetings");

        currentUserID= FirebaseAuth.getInstance().getCurrentUser().getUid();



        getCreator(currentUserID);


        recyclerView = (RecyclerView)findViewById(R.id.yourMeetingsrv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //get myMeetings from firebase
        FirebaseRecyclerOptions<meetingModel> options =
                new FirebaseRecyclerOptions.Builder<meetingModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("myMeetings").child(currentUserID), meetingModel.class)
                        .build();
        adapter = new viewMeetingsAdapter(options);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.addMeetingButton:
               checkTitleInput();
                break;
            case R.id.backButton:
                startActivity(new Intent(this, HomeActivity.class));
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
        creatorsRef.removeEventListener(creatorVEL);
    }

    public void getCreator(String creatorID){
        creatorsRef= FirebaseDatabase.getInstance().getReference("Members").child(creatorID);

        creatorsRef.addValueEventListener(creatorVEL=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser= dataSnapshot.getValue(User.class);

                Log.i("creator name",currentUser.getFullName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(ViewMeetingsActivity.this,"Sorry,exit the page and try again ",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void checkTitleInput(){
        String meetingTitle=meetingTitleEditText.getText().toString().trim();
        if (meetingTitle.isEmpty()){
            Toast.makeText(ViewMeetingsActivity.this,"Meeting Title is required!",Toast.LENGTH_LONG).show();
            return;
        }
        else{
            addMeeting(meetingTitle);
        }
    }


    public void addMeeting(String meetingTitle){


        String meetingKey=meetingsRef.push().getKey();

        meetingModel MeetingModel=new meetingModel(meetingKey,meetingTitle,currentUserID,currentUser.getFullName());
        meetingsRef.child(meetingKey).setValue(MeetingModel);

        meetingsRef.child(meetingKey).child("selectedUsers").child(currentUserID).setValue(currentUser);

        Intent selectUsersIntent=new Intent(ViewMeetingsActivity.this, selectUsersActivity.class);
        selectUsersIntent.putExtra("meetingKey",meetingKey);
        selectUsersIntent.putExtra("meetingTitle",meetingTitle);
        selectUsersIntent.putExtra("creatorID",currentUserID);
        selectUsersIntent.putExtra("creatorName",currentUser.getFullName());
        startActivity(selectUsersIntent);


    }

}