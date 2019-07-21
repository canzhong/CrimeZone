package com.example.crimezone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class EventRegisterActivity extends AppCompatActivity {

    private static final int MAX_LENGTH = 10;
    private androidx.appcompat.widget.Toolbar mToolbar;

    private TextInputLayout mNameInput;
    private TextInputLayout mAddressInput;

    private Button mSaveBtn;

    //Firebase Code
    private DatabaseReference mEventsDatabase;
    private DatabaseReference mEventSettingsDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_register);

        mToolbar = (Toolbar) findViewById(R.id.settings_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Firebase

        mNameInput = (TextInputLayout) findViewById(R.id.event_input);
        mAddressInput = (TextInputLayout) findViewById(R.id.address_input);

        //Creating Firebase Event Instance with event's unique identifier (input event name and unique string)
//        String name = mNameInput.getEditText().getText().toString();
//        String eventId = name + random();
//        mEventsDatabase = FirebaseDatabase.getInstance().getReference().child("Events");
//        mEventsDatabase.child("eventId").setValue(eventId);

//        mEventSettingsDatabase = FirebaseDatabase.getInstance().getReference().child("Events").child(eventId);

        mSaveBtn = (Button) findViewById(R.id.save_btn);

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = mNameInput.getEditText().getText().toString();
                String address = mAddressInput.getEditText().getText().toString();


                String eventId = name;
                mEventsDatabase = FirebaseDatabase.getInstance().getReference().child("Events").child(eventId);

                mEventsDatabase.child("name").setValue(name);
                mEventsDatabase.child("address").setValue(address);

                Toast.makeText(EventRegisterActivity.this, "Event created", Toast.LENGTH_LONG).show();

            }
        });
    }

    public static String random(){
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for(int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
