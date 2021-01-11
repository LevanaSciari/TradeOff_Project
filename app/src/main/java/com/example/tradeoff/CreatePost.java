package com.example.tradeoff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CreatePost extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button createPost;
    private String give;
    private String take;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private String currentUserID;
    private DatabaseReference databaseReference;
    private Spinner mySpinner_take;
    private Spinner mySpinner_give;

    ArrayList<String> activityArray = new ArrayList<String>();
    String email;
    String email_;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        createPost = findViewById(R.id.createPostbtn);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getInstance().getReference();
        mySpinner_take = (Spinner) findViewById(R.id.spinner_take);
        mySpinner_give = (Spinner) findViewById(R.id.spinner_give);
        Bundle extras = getIntent().getExtras();
        email = extras.getString("email");


        databaseReference.child("Active").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot activity : dataSnapshot.getChildren()) {
                    String activityValue = activity.getValue(String.class);
                    activityArray.add(activityValue);
                }
                Spinner take = findViewById(R.id.spinner_take);
                take.setOnItemSelectedListener(CreatePost.this);

                Spinner give = findViewById(R.id.spinner_give);
                give.setOnItemSelectedListener(CreatePost.this);
                // for spinner
                ArrayAdapter ad
                        = new ArrayAdapter(
                        CreatePost.this,
                        android.R.layout.simple_spinner_item,
                        activityArray);

                take.setAdapter(ad);

                ArrayAdapter ad1
                        = new ArrayAdapter(
                        CreatePost.this,
                        android.R.layout.simple_spinner_item,
                        activityArray);

                give.setAdapter(ad1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        createPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerPostToDataBase();
                Intent i = new Intent(CreatePost.this, Home.class);
                Bundle extras = getIntent().getExtras();
                i.putExtra("email", extras.getString("email"));
                startActivity(i);
                finish();
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        Spinner spin = (Spinner) parent;
        Spinner spin2 = (Spinner) parent;
        if (spin.getId() == R.id.spinner_take) {
            take = activityArray.get(position);
        }

        if (spin2.getId() == R.id.spinner_give) {
            give = activityArray.get(position);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //add post to database
    public void registerPostToDataBase() {

        email_ = email.replaceAll("\\.", "_");


        if (give.isEmpty()) {
            Toast.makeText(CreatePost.this, "Please select Give Option", Toast.LENGTH_LONG).show();
            return;
        }
        if (take.isEmpty()) {
            Toast.makeText(CreatePost.this, "Please select Take Option", Toast.LENGTH_LONG).show();
            return;
        } else {
            String postId = databaseReference.push().getKey();
            Post p = new Post(give, take, email_, postId);
            FirebaseDatabase.getInstance().getReference("Posts").child(postId).setValue(p);
        }
    }


    //for option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.return_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_return:
                Intent i = new Intent(CreatePost.this, Home.class);
                Bundle extras = getIntent().getExtras();
                i.putExtra("email", extras.getString("email"));
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
