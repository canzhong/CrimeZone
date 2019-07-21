package com.example.crimezone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class EventActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 1;
    private DatabaseReference mEventDatabase;
    private FirebaseUser mCurrentUser;

    private CircleImageView mEventImage;
    private TextView mEventName;
    private TextView mEventAddress;

    private Button mEditProfileBtn;
    private Button mImagebtn;

    private static final int GALLERY_PICK = 1;
    private static final int MAX_LENGTH = 10;

    //Firebase Storage
    private StorageReference mImageStorage;

    private ProgressDialog mProgressDialog;

    //Get Event Name
    private String eventId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        mEventImage = (CircleImageView) findViewById(R.id.event_image);
        mEventName = (TextView) findViewById(R.id.event_name);
        mEventAddress = (TextView) findViewById(R.id.event_address);

        mEditProfileBtn = (Button) findViewById(R.id.edit_event_btn);
        mImagebtn = (Button) findViewById(R.id.image_btn);

        mImageStorage = FirebaseStorage.getInstance().getReference();

        eventId = getIntent().getStringExtra("eventId");
        Log.i("Event ID", eventId);
        mEventDatabase = FirebaseDatabase.getInstance().getReference().child("Events").child(eventId);

        mEventDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                //String image = dataSnapshot.child("image").getValue().toString();
                String address = dataSnapshot.child("address").getValue().toString();

                mEventName.setText(name);
                mEventAddress.setText(address);

                //Picasso.get().load(image).into(mEventImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mEditProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editEventIntent = new Intent(EventActivity.this, EventSettingsActivity.class);
                editEventIntent.putExtra("eventId", mEventName.getText().toString());
                //the above line will be edited to get the event Id from the intent
                editEventIntent.putExtra("name", mEventName.getText().toString());
                editEventIntent.putExtra("address", mEventAddress.getText().toString());
                startActivity(editEventIntent);
            }
        });

        mImagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(this);
            //Toast.makeText(ProfileActivity.this, imageUri, Toast.LENGTH_LONG).show();
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mProgressDialog = new ProgressDialog(EventActivity.this);
                mProgressDialog.setTitle("Uploading Image...");
                mProgressDialog.setMessage("Please wait while we upload the image");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

                Uri resultUri = result.getUri();

                String currentUserId = mCurrentUser.getUid();

                final StorageReference filepath = mImageStorage.child("event_images").child(random() + ".jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            //String downloadUrl = filepath.getDownloadUrl().toString();
                            //String downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();
                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    final String downloadUrl = uri.toString();
                                    mEventDatabase.child("image").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                mProgressDialog.dismiss();
                                                Toast.makeText(EventActivity.this, "Success", Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    });
                                }
                            });
                        } else {
                            Toast.makeText(EventActivity.this, "Error in uploading", Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
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
