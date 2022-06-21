package com.example.fbase;

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
