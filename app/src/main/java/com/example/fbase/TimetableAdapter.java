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
TimetableAdapter extends FirebaseRecyclerAdapter<Event,TimetableAdapter.myViewHolder> which is the
parent class.
TimetableAdapter constructor  requires parameters FirebaseRecyclerOptions<Event> options,
String myUID, String day.
We pass options into the parent class constructor.
We assign parameter String myUID to class variable this.myUID and we assign String day to class
variable this.day.

First we explain the inner class myViewHolder (at the bottom this file). In myViewHolder TextViews
tvTitle and tvPeriod  and Image Button deleteButton are assigned to their respective resource ids
of the XML file.

Then, the  protected void onBindViewHolder requires parameters @NonNull myViewHolder holder,
int position and @NonNull Event model. holder.tvTitle is set to Event model's title and
holder.tvPeriod is set to Event model's period. We set an on click listener on holder.deleteButton
and we pass view and model into deleteDialog method.

In deleteDialog (View view, Event model), we ask the user whether they are sure they want to delete
the event by setting a message with the event's title in it.
Then, we set negative button ("Cancel")that user can press to dismiss the dialog straight away.
Then, we set positive button ("Confirm")that user can press to confirm that they want to delete the
 event.
Once the positive button is pressed, Event model is passed to the delete(Event model method) and
the event get deleted.
Then, there is a Toast message that says that the event has been deleted by using view.getContext()
as the context.
Then, the dialog is dismissed.

Inside the delete(Event model) method, we get the key of the Event model and we use this.day,
this.myUID and key to navigate to the node containing the key in firebase and remove the value of
the key which is the event object. Once the value is removed, the key automatically ceases to exist.
For example, node friday (and every other day )in our firebase database looks like this:
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
In myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType), we make
R.layout.timetable_item as the layout for each item in the recycler view.



*/

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class TimetableAdapter extends FirebaseRecyclerAdapter<Event, TimetableAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    String myUID,day;
    public TimetableAdapter(@NonNull FirebaseRecyclerOptions<Event> options, String myUID, String day) {
        super(options);
        this.myUID=myUID;
        this.day=day;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Event model){
        holder.tvTitle.setText(model.getTitle());
        holder.tvPeriod.setText(model.getPeriod());
        holder.deleteButton.setOnClickListener(view ->deleteDialog(view,model) );
    }

    public void deleteDialog(View view,Event model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Confirm");
        builder.setMessage("Delete Event: "+model.getTitle()+"?");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               delete(model);
                Toast.makeText(view.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
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

    public void delete(Event model){
        String key=model.getKey();
        FirebaseDatabase.getInstance().getReference().child(this.day)
                .child(this.myUID).child(key).removeValue();

    }



    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_item,parent,false);
        return new myViewHolder(view);
    }



    class myViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvPeriod;
        ImageButton deleteButton;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            tvPeriod = (TextView)itemView.findViewById(R.id.tvPeriod);
            deleteButton=itemView.findViewById(R.id.deleteButton);


        }
    }
}
