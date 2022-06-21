package com.example.fbase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity{
    private Button logout;
    private Button MyMeetings;
    private Button MyTimetable,addButton;
    private TextView textViewName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent=getIntent();
        String userName=intent.getStringExtra("name");
        String uid=intent.getStringExtra("uid");
        textViewName=(TextView)findViewById(R.id.textName);
        textViewName.setText(userName);
        MyTimetable=(Button)findViewById(R.id.MyTimetable);
        logout=(Button)findViewById(R.id.signOut);

        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });
        MyTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent timeTableIntent=new Intent(HomeActivity.this, TimetableActivity.class);
               timeTableIntent.putExtra("name",userName);
               timeTableIntent.putExtra("uid",uid);
               startActivity(timeTableIntent);
            }
        });




        MyMeetings=(Button)findViewById(R.id.MyMeetings);
        MyMeetings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(HomeActivity.this, ViewMeetingsActivity.class));
            }
        });

    }

}