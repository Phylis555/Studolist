package com.example.studolist.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studolist.Models.Event;
import com.example.studolist.R;
import com.example.studolist.RecyclerViewInterface;
import com.example.studolist.TaskAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class EventAdapter extends FirestoreRecyclerAdapter<Event, EventAdapter.EventViewHolder> {

    Context context ;
    private final RecyclerViewInterface recyclerViewInterface;

    public EventAdapter(@NonNull FirestoreRecyclerOptions<Event> options, Context context, RecyclerViewInterface recyclerViewInterface) {
        super(options);
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @Override
    protected void onBindViewHolder(@NonNull EventViewHolder holder, int position, @NonNull Event event) {
        holder.eventName.setText(event.getmTitle());
        holder.eventStatus.setText(event.getmDate().toString());
        recyclerViewInterface.onItemClick(position);

    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item,parent,false);
        return new EventAdapter.EventViewHolder(view);
    }

    class EventViewHolder extends RecyclerView.ViewHolder{

        TextView eventName, eventStatus;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.calendar_event_name);
            eventStatus = itemView.findViewById(R.id.calendar_event_status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface != null)
                    {
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION)
                            recyclerViewInterface.onItemClick(pos);
                    }
                }
            });

        }
    }

}
