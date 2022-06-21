package com.example.fbase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

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


