package com.example.crimezone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.example.crimezone.APITesting;
import com.example.crimezone.User;


public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG =
            MainActivity.class.getSimpleName();

    private static final APITesting testAPI = new APITesting();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    public void launchViewProfile(View view) {

        User user = testAPI.getUser();

        Log.d(LOG_TAG, "View Profile clicked!");

        Intent intent = new Intent(this, ViewProfile.class);
        intent.putExtra("currentUser", user);

        startActivity(intent);
    }
}
