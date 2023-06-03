package com.example.studolist.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studolist.Models.Event;
import com.example.studolist.R;
import com.example.studolist.Interfaces.RecyclerViewInterface;
import com.example.studolist.Utilities.Utility;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class EventAdapter extends FirestoreRecyclerAdapter<Event, EventAdapter.EventViewHolder> {

    Context context ;
    private final RecyclerViewInterface listener;

    public EventAdapter(@NonNull FirestoreRecyclerOptions<Event> options, Context context, RecyclerViewInterface listener) {
        super(options);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull Event event) {
        holder.eventName.setText(event.getmTitle());
        String format = Utility.formatDate(event.getmDate());
        holder.eventStatus.setText(format);


    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item,parent,false);
        return new EventAdapter.EventViewHolder(view, listener);
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder{

        TextView eventName, eventStatus;

        public EventViewHolder(@NonNull View itemView, RecyclerViewInterface listener) {
            super(itemView);

            eventName = itemView.findViewById(R.id.calendar_event_name);
            eventStatus = itemView.findViewById(R.id.calendar_event_status);

            if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
              eventName.setTextColor(Color.WHITE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION)
                           listener.onItemClick(pos);
                    }
                }
            });

        }
    }

}