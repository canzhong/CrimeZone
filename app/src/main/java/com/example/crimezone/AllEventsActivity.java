package com.example.crimezone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class AllEventsActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mEventsList;

    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events);

        mToolbar = (Toolbar) findViewById(R.id.all_events_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEventsList = (RecyclerView) findViewById(R.id.events_list);
        mEventsList.setHasFixedSize(true);
        mEventsList.setLayoutManager(new LinearLayoutManager(this));

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Events")
                .limitToLast(50);

        FirebaseRecyclerOptions<Events> options =
                new FirebaseRecyclerOptions.Builder<Events>()
                        .setQuery(query, Events.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Events, EventsViewHolder>(options) {
            @Override
            public EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.events_single_layout, parent, false);

                return new EventsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(EventsViewHolder holder, int position, Events model) {
                // Bind the Chat object to the ChatHolder
                // ...
                holder.setName(model.getName());
                holder.setAddress(model.getAddress());

                final String eventId = getRef(position).getKey();

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent eventIntent = new Intent(AllEventsActivity.this, EventActivity.class);
                        eventIntent.putExtra("eventId", eventId);
                        startActivity(eventIntent);
                    }
                });
            }

        };

        mEventsList.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }

    public static class EventsViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public EventsViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }


        public void setName(String name) {
            TextView eventNameTextView = mView.findViewById(R.id.event_single_name);
            eventNameTextView.setText(name);
        }

        public void setAddress(String address) {
            TextView eventAddressTextView = mView.findViewById(R.id.event_single_address);
            eventAddressTextView.setText(address);
        }
    }
}
