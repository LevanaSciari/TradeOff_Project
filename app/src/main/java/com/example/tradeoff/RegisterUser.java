package com.example.tradeoff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import static com.example.tradeoff.Profile.RESULT_LOAD_IMG;

public class RegisterUser extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText firstName;
    EditText lastName;
    EditText emailEditText;
    EditText password;
    EditText phone;
    Uri imageUri;
    String activity;
    String selectedRegion;
    String email_;
    FirebaseStorage storage;
    StorageReference storageReference;

    ArrayList<String> regionsArray = new ArrayList<String>();
    DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_user);

        databaseRef = FirebaseDatabase.getInstance().getReference();

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        firstName = (EditText) findViewById(R.id.FirstName);
        lastName = (EditText) findViewById(R.id.LastName);
        emailEditText = (EditText) findViewById(R.id.mail);
        password = (EditText) findViewById(R.id.Password);
        phone = (EditText) findViewById(R.id.Phone);

        //add all region from database to arrayRegion
        databaseRef.child("Region").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot currRegion : dataSnapshot.getChildren()) {
                    String regionName = currRegion.getValue(String.class);
                    regionsArray.add(regionName);
                }
                //init spinnerRegion
                Spinner spinnerRegion = findViewById(R.id.spineradress);
                spinnerRegion.setOnItemSelectedListener(RegisterUser.this);

                ArrayAdapter arrayAdapter
                        = new ArrayAdapter(
                        RegisterUser.this,
                        android.R.layout.simple_spinner_item,
                        regionsArray);
                spinnerRegion.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    //
    void addUserToDatabase(String mail, String pass) {
        if (firstName.getText().toString().isEmpty()) {
            Toast.makeText(RegisterUser.this, "First Name is empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (lastName.getText().toString().isEmpty()) {
            Toast.makeText(RegisterUser.this, "Last Name is empty", Toast.LENGTH_LONG).show();
            return;
        }
        String email=emailEditText.getText().toString();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
            return;
        }

        if (emailEditText.getText().toString().isEmpty()) {
            Toast.makeText(RegisterUser.this, "Email is empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (password.getText().toString().isEmpty()) {
            Toast.makeText(RegisterUser.this, "Pssseword is empty", Toast.LENGTH_LONG).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(RegisterUser.this, "Pssseword is shorter", Toast.LENGTH_LONG).show();
            return;
        }

        if (phone.getText().toString().isEmpty()) {
            Toast.makeText(RegisterUser.this, "Phone is empty", Toast.LENGTH_LONG).show();
            return;
        }

        if (phone.length() != 10) {
            Toast.makeText(RegisterUser.this, "Phone is not valid", Toast.LENGTH_LONG).show();
            return;
        }
        if (phone.getText().toString().charAt(0) != '0') {
            Toast.makeText(RegisterUser.this, "Phone is not valid", Toast.LENGTH_LONG).show();
            return;
        }
        if (phone.getText().toString().charAt(1) != '5') {
            Toast.makeText(RegisterUser.this, "Phone is not valid", Toast.LENGTH_LONG).show();
            return;
        }
        //add user to authentification
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(RegisterUser.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {



                    String emailUser = emailEditText.getText().toString().trim();
                    email_ = "";
                    email_= emailUser.replaceAll("\\.", "_");

                   email_= email_.toLowerCase();
                    uploadImage();

                    User newUser = new User(
                            firstName.getText().toString().trim(),
                            lastName.getText().toString().trim(),
                            email_.toLowerCase(),
                            password.getText().toString().trim(),
                            phone.getText().toString().trim(),
                            activity,
                            selectedRegion);

//add user to realtimedatabase
                    databaseRef.child("User").child( email_).setValue(newUser)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(RegisterUser.this, "Welcome To TradeOff", Toast.LENGTH_LONG).show();
                                    Intent act = new Intent(RegisterUser.this, Home.class);
                                    act.putExtra("email",  email_);
                                    startActivity(act);// Write was successful!
                                    finish();
                                    // ...
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterUser.this, "Register is not success", Toast.LENGTH_LONG).show();
                                    // Write failed
                                    // ...
                                }
                            });
                } else {
                    Toast.makeText(RegisterUser.this, "Register is not success", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //register user
    public void SignUp(View view) {
        addUserToDatabase(emailEditText.getText().toString().trim(), password.getText().toString().trim());//register to authentification
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spin = (Spinner) parent;
        if (spin.getId() == R.id.spineradress) {
            selectedRegion = regionsArray.get(position);

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    //open galery and pick the picture from galery
    public void UploadFromGalery(View view) {

        Intent galeryIntent = new Intent(Intent.ACTION_PICK);
        galeryIntent.setType("image/*");
        startActivityForResult(galeryIntent, RESULT_LOAD_IMG);
    }

    //save the selected picture in imageUri
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            imageUri = data.getData();
        }
    }


//   upload image to database
    private void uploadImage() {
        if (imageUri != null) {
            // Defining the child of storageReference
            //StorageReference ref = storageReference.child("images/");

            // adding listeners on upload
            // or failure of image
            storageReference.child("images/").child("User").child( email_).putFile(imageUri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    // Image uploaded successfully
                                    Toast.makeText(RegisterUser.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error, Image not uploaded
                            Toast.makeText(RegisterUser.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                }
                            });
        }
    }
}

