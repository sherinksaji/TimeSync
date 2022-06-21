package com.example.fbase;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class viewMeetingsAdapter extends FirebaseRecyclerAdapter<meetingModel, viewMeetingsAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public viewMeetingsAdapter(@NonNull FirebaseRecyclerOptions<meetingModel> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull meetingModel meeting) {

        holder.meetingTitle.setText(meeting.getMeetingTitle());
        holder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MeetOn=new Intent(holder.context, MeetActivity.class);
                MeetOn.putExtra("meetingTitle",meeting.getMeetingTitle());
                MeetOn.putExtra("meetingID",meeting.getMeetingKey());
                MeetOn.putExtra("creatorName",meeting.getCreatorName());
                MeetOn.putExtra("creatorUid",meeting.getCreatorUid());
                holder.context.startActivity(MeetOn);
            }
        });

    }



    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_meetings_item,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        TextView meetingTitle;
        Button viewButton;
        Context context;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            meetingTitle=(TextView)itemView.findViewById(R.id.meetingTitleTV);
            viewButton=(Button)itemView.findViewById(R.id.viewButton);
            context= itemView.getContext();
        }
    }
}
