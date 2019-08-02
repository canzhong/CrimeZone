package com.example.crimezone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.InputStream;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;


public class ViewProfile extends AppCompatActivity {

    private User currentUser;

    private static final String LOG_TAG = ViewProfile.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        Intent intent = getIntent();
        currentUser = (User)intent.getSerializableExtra("currentUser");

        new DownloadImageFromInternet((ImageView) findViewById(R.id.imageDisplay))
                .execute("https://pbs.twimg.com/profile_images/630285593268752384/iD1MkFQ0.png");


        TextView textfname = (TextView) findViewById(R.id.text_fname);
        textfname.setText(currentUser.getfname());

        TextView textlname = (TextView) findViewById(R.id.text_lname);
        textlname.setText(currentUser.getlname());

        TextView textemail = (TextView) findViewById(R.id.text_email);
        textemail.setText(currentUser.getEmail());

        TextView textphone = (TextView) findViewById(R.id.text_phone);
        textphone.setText(currentUser.getPhone());

    }

    public void launchEditProfile(View view) {

        Log.d(LOG_TAG, "Edit Profile Clicked!");

        Intent intent = new Intent(this, EditProfile.class);
        intent.putExtra("currentUser", currentUser);

        startActivity(intent);


    }



    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

}

