package com.example.fbase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;
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
|	  |	    |->creatorName:"bobby"
|	  |	    |->creatorUid:"uid"
|	  |	    |->meetingKey:"meetingKey"
|	  |	    |->meetingTitle:"abc"
|	  |	    |->selectedUsers
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
public class MeetAdapter extends RecyclerView.Adapter<MeetAdapter.ViewHolderTiming>{
    List<timingData> list
            = Collections.emptyList();

    Context context;


    public MeetAdapter(List<timingData> list,
                       Context context)
    {
        this.list = list;
        this.context = context;

    }

    @Override
    public ViewHolderTiming
    onCreateViewHolder(ViewGroup parent,
                       int viewType)
    {

        Context context
                = parent.getContext();
        LayoutInflater inflater
                = LayoutInflater.from(context);

        // Inflate the layout

        View photoView
                = inflater
                .inflate(R.layout.meet_item,
                        parent, false);

        ViewHolderTiming viewHolder
                = new ViewHolderTiming(photoView);
        return viewHolder;
    }

    @Override
    public void
    onBindViewHolder(final ViewHolderTiming viewHolder,
                     final int position)
    {
        final int index = viewHolder.getBindingAdapterPosition();
        viewHolder.freeTime
                .setText(list.get(position).timing);


    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(
            RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public static class ViewHolderTiming extends RecyclerView.ViewHolder{

            TextView freeTime;
            View view;

            ViewHolderTiming(View itemView)
            {
                super(itemView);
                freeTime
                        = (TextView)itemView
                        .findViewById(R.id.freeTime);

                view  = itemView;
            }

    }

    public static class timingData {
        String timing;

        timingData(String timing)
        {

            this.timing= timing;
        }
    }
}


