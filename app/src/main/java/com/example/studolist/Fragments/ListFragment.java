package com.example.studolist.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.studolist.Adapters.EventAdapter;
import com.example.studolist.CallBack_FocusDate;
import com.example.studolist.EventActivity;
import com.example.studolist.Models.Event;
import com.example.studolist.Models.Task;
import com.example.studolist.R;
import com.example.studolist.RecyclerViewInterface;
import com.example.studolist.TaskAdapter;
import com.example.studolist.TaskDetailsActivity;
import com.example.studolist.Utility;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;


public class ListFragment extends Fragment implements RecyclerViewInterface {

    private TextView eventName;
    private TextView eventStatus;

    private CallBack_FocusDate callBack_focusDate;
    RecyclerView recyclerView;
    EventAdapter eventAdapter;
    private Query query;
    private  List<DocumentSnapshot> itemDocuments;
    private FirestoreRecyclerOptions<Event> options;


    public void setCallBack(CallBack_FocusDate callBack_focusDate)
    {
        this.callBack_focusDate = callBack_focusDate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(view);
        setupRecyclerView();
        return view;
    }





    private void findViews(@NonNull View view) {
        recyclerView = view.findViewById(R.id.main_LST_events);
    }

    @Override
    public void onItemClick(int position) {
        if(callBack_focusDate !=null)
        {
            Event event = options.getSnapshots().get(position);
            callBack_focusDate.focusDate(event.getmDate());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        eventAdapter.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        eventAdapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        eventAdapter.notifyDataSetChanged();
    }


    private void setupRecyclerView() {
        query = Utility.loadTaskFromDb_events().orderBy("mDate", Query.Direction.DESCENDING);
        options = new FirestoreRecyclerOptions.Builder<Event>()
                .setQuery(query, Event.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        eventAdapter = new EventAdapter(options, getContext(),this);
        new ItemTouchHelper(itemTouchCB).attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(eventAdapter);
    }

    ItemTouchHelper.SimpleCallback itemTouchCB = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            DocumentReference documentReference;
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        itemDocuments = querySnapshot.getDocuments();
                        DocumentSnapshot clickedDocument = itemDocuments.get(viewHolder.getAdapterPosition());
                        DocumentReference docId = clickedDocument.getReference();
                        //DocumentReference documentReference = Utility.loadTaskFromDb_events().document(docId);
                        docId.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                                if(task.isSuccessful()){
                                    //event is added
                                    Toast.makeText(getContext(),"Event deleted successfully",Toast.LENGTH_SHORT).show();
                                    eventAdapter.notifyDataSetChanged();

                                }else{
                                    Toast.makeText(getContext(),"Error occurred in deleting event",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } else {
                        // Handle any errors
                        Exception exception = task.getException();
                        Toast.makeText(getContext(),exception.toString(),Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }
    };


}
