package com.example.tradeoff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Profile extends AppCompatActivity {
    public static final int RESULT_LOAD_IMG = 1;
    android.widget.ImageView picture;

    String email;
    TextView phone;
    TextView mailTextView;
    TextView firstName;
    TextView lastName;
    TextView region;
    String email_;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        picture = (ImageView) findViewById(R.id.imageView);
        phone = (TextView) findViewById(R.id.phone_number);
        mailTextView = (TextView) findViewById(R.id.mail);
        firstName = (TextView) findViewById(R.id.fname);
        lastName = (TextView) findViewById(R.id.lname);
        region = (TextView) findViewById(R.id.adress);
        Bundle extras = getIntent().getExtras();
        email = extras.getString("email");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        getDataFromDatabase(email);
        loadpicture();

    }

    //for option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profie_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove_post:
                Intent act = new Intent(new Intent(Profile.this, MyPost.class));
                act.putExtra("email", email_);
                startActivity(act);
                return true;
            case R.id.editprofile:
                startActivity(new Intent(Profile.this, EditProfile.class));
                return true;
            case R.id.myNotification:
                Intent act1 = new Intent(new Intent(Profile.this, MyNotificationActivity.class));
                act1.putExtra("email", email_);
                startActivity(act1);
                return true;
            case R.id.exit:
                Intent Exit = new Intent(Profile.this, MainActivity.class);
                startActivity(Exit);
                finish();
                return true;
            case R.id.btn_return:
                Intent i = new Intent(Profile.this, Home.class);
                Bundle extras = getIntent().getExtras();
                i.putExtra("email", extras.getString("email"));
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    //pass data from database to the XMLfile
    void getDataFromDatabase(final String email) {
        email_ = email.replaceAll("\\.", "_");

        databaseReference.child("User").child(email_).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User userCurrent = dataSnapshot.getValue(User.class);
                mailTextView.setText(email);
                firstName.setText(userCurrent.getFirstName());
                lastName.setText(userCurrent.getLastName());
                phone.setText(userCurrent.getPhone());
                region.setText(userCurrent.getRegion());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }


    void loadpicture() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference = storageReference.child("images/").child("User/" + email_);

        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                picture.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "Unsuccessful uploading image", Toast.LENGTH_LONG).show();
            }
        });
    }

}