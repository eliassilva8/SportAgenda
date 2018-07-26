package com.eliassilva.sportagenda;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements EventAdapter.EventAdapterOnClickHandler {
    @BindView(R.id.new_event_fab)
    FloatingActionButton mNewEventFab;
    @BindView(R.id.events_list_recycler_view)
    RecyclerView mEventsRecyclerView;
    List<Event> mEvents = new ArrayList<>();
    FirebaseUser mUser;
    ArrayList<Long> dates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mEventsRecyclerView.setLayoutManager(layoutManager);
        mEventsRecyclerView.setHasFixedSize(true);
        mEventsRecyclerView.setNestedScrollingEnabled(false);

        final EventAdapter mAdapter = new EventAdapter(mEvents, this);
        mEventsRecyclerView.setAdapter(mAdapter);

        FirebaseDatabase database = FirebaseDatabaseSingleton.getInstance();
        final DatabaseReference events = database.getReference(getString(R.string.db_events));
        DatabaseReference userUid = events.child(mUser.getUid());
        Query orderByDate = userUid.orderByChild(getString(R.string.db_date_in_milliseconds));

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
        orderByDate.addChildEventListener(eventListener);

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
        Intent intent = new Intent(MainActivity.this, NewEditEvent.class);
        intent.putExtra("event", event);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_pop_up_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.log_out) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });
            return true;
        } else {
            return false;
        }
    }
}
