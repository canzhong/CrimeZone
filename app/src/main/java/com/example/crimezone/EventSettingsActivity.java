package com.example.crimezone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventSettingsActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar mToolbar;

    private TextInputLayout mNameInput;
    private TextInputLayout mAddressInput;

    private Button mSaveBtn;

    //Firebase Code
    private DatabaseReference mSettingsDatabase;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_settings);


        mToolbar = (Toolbar) findViewById(R.id.settings_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Firebase
        String eventId = getIntent().getStringExtra("eventId");

        mSettingsDatabase = FirebaseDatabase.getInstance().getReference().child("Events").child(eventId);


        //retrieving values from the intent
        String nameStr = getIntent().getStringExtra("name");
        String addressStr = getIntent().getStringExtra("address");

        mNameInput = (TextInputLayout) findViewById(R.id.event_input);
        mAddressInput = (TextInputLayout) findViewById(R.id.address_input);

        mNameInput.getEditText().setText(nameStr);
        mAddressInput.getEditText().setText(addressStr);

        mSaveBtn = (Button) findViewById(R.id.save_btn);

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = mNameInput.getEditText().getText().toString();
                String address = mAddressInput.getEditText().getText().toString();

                mSettingsDatabase.child("name").setValue(name);
                mSettingsDatabase.child("address").setValue(address);

                Toast.makeText(EventSettingsActivity.this, "Profile updated", Toast.LENGTH_LONG).show();

            }
        });

    }
}
