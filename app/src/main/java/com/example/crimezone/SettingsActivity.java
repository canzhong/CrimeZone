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

public class SettingsActivity extends AppCompatActivity {

    private androidx.appcompat.widget.Toolbar mToolbar;

    private TextInputLayout mPhoneInput;
    private TextInputLayout mAddressInput;

    private Button mSaveBtn;

    //Firebase Code
    private DatabaseReference mSettingsDatabase;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Firebase
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUid = mCurrentUser.getUid();

        mSettingsDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid);

        mToolbar = (Toolbar) findViewById(R.id.settings_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Account Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //retrieving values from the intent
        String phoneStr = getIntent().getStringExtra("number");
        String addressStr = getIntent().getStringExtra("address");

        mPhoneInput = (TextInputLayout) findViewById(R.id.phone_input);
        mAddressInput = (TextInputLayout) findViewById(R.id.address_input);

        mPhoneInput.getEditText().setText(phoneStr);
        mAddressInput.getEditText().setText(addressStr);

        mSaveBtn = (Button) findViewById(R.id.save_btn);

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phone = mPhoneInput.getEditText().getText().toString();
                String address = mAddressInput.getEditText().getText().toString();

                mSettingsDatabase.child("phone").setValue(phone);
                mSettingsDatabase.child("address").setValue(address);

                Toast.makeText(SettingsActivity.this, "Profile updated", Toast.LENGTH_LONG).show();

            }
        });

    }
}
