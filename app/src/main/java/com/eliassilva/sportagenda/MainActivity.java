package com.eliassilva.sportagenda;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements EventAdapter.EventAdapterOnClickHandler {
    @BindView(R.id.new_event_fab)
    FloatingActionButton mNewEventFab;
    @BindView(R.id.events_list_recycler_view)
    RecyclerView mEventsRecyclerView;
    List<Event> mEvents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mEventsRecyclerView.setLayoutManager(layoutManager);
        mEventsRecyclerView.setHasFixedSize(true);
        mEventsRecyclerView.setNestedScrollingEnabled(false);

        final EventAdapter mAdapter = new EventAdapter(mEvents, this);
        mEventsRecyclerView.setAdapter(mAdapter);

        // Get a reference to our posts
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("events");

        ChildEventListener eventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Event newEvent = dataSnapshot.getValue(Event.class);
                mEvents.add(newEvent);
                mAdapter.setEventData(mEvents);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        ref.addChildEventListener(eventListener);

        mNewEventFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewEditEvent.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(Event event) {
        //Event eventToSend = new Event(event.getSports(), event.getDate(), event.getTime(), event.getLocal(), event.getParticipants());
        Intent intent = new Intent(MainActivity.this, NewEditEvent.class);
        intent.putExtra("event", event);
        System.out.println("event: " + event);
        startActivity(intent);
    }
}
