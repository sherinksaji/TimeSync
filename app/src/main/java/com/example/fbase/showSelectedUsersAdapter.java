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
*/
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class showSelectedUsersAdapter extends FirebaseRecyclerAdapter<User, showSelectedUsersAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    meetingModel meeting;
    public showSelectedUsersAdapter(@NonNull FirebaseRecyclerOptions<User> options, meetingModel meeting) {
        super(options);
        this.meeting=meeting;

    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull User user){
        DatabaseReference selectedUsersRef=FirebaseDatabase.getInstance().getReference("Meetings").child(this.meeting.getMeetingKey()).child("selectedUsers");
        holder.name.setText(user.getFullName());
        holder.email.setText(user.getEmail());
        if (user.getUid().equals(meeting.getCreatorUid())){
            holder.deleteButton.setVisibility(View.GONE);
        }
        else {
            holder.deleteButton.setOnClickListener(view -> delete(user));
        }
    }

    public void delete(User user){
        DatabaseReference selectedUsersRef=FirebaseDatabase.getInstance().getReference("Meetings").child(this.meeting.getMeetingKey()).child("selectedUsers");
        selectedUsersRef.child(user.getUid()).removeValue();
    }



    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_selected_users_item,parent,false);
        return new myViewHolder(view);
    }



    class myViewHolder extends RecyclerView.ViewHolder{
        TextView email, name;
        ImageButton deleteButton;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.nametext);
            email = (TextView)itemView.findViewById(R.id.emailtext);
            deleteButton= (ImageButton) itemView.findViewById(R.id.deleteButton);


        }
    }

}
