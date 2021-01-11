package com.example.tradeoff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class EditProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button save;
    private EditText newFirstName, newLastName, newPhone;
    private DatabaseReference databaseReference;
    private FirebaseUser current_user;
    DatabaseReference mDatabase;
    ArrayList<String> regionsArray = new ArrayList<String>();
    String selectedRegion;
    String email_ = "";
    Uri imageUri;
    //FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        //add alll region from database to regionArray
        mDatabase.child("Region").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot currRegion : dataSnapshot.getChildren()) {
                    String regionName = currRegion.getValue(String.class);
                    regionsArray.add(regionName);
                }

                //init spinner
                Spinner spinnerRegions = findViewById(R.id.spinner_regions);
                spinnerRegions.setOnItemSelectedListener(EditProfile.this);
                ArrayAdapter adapter
                        = new ArrayAdapter(
                        EditProfile.this,
                        android.R.layout.simple_spinner_item,
                        regionsArray);

                spinnerRegions.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //storage = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        newFirstName = findViewById(R.id.edit_first_name);
        newPhone = findViewById(R.id.edit_phone);
        newLastName = findViewById(R.id.edit_last_name);

        current_user = FirebaseAuth.getInstance().getCurrentUser();
        String emailUser = current_user.getEmail().toString().trim();

        email_=emailUser.replaceAll("\\.", "_");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User").child(email_);

        save = (Button) findViewById(R.id.btn_save_profile_changes);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String changeFirstname = newFirstName.getText().toString();
                final String changeLastname = newLastName.getText().toString();

                final String changePhone = newPhone.getText().toString();


                if (changeFirstname.isEmpty() == false) {
                    databaseReference.child("firstName").setValue(changeFirstname);
                }
                if (changeLastname.isEmpty() == false) {
                    databaseReference.child("lastName").setValue(changeLastname);
                }

                if (changePhone.isEmpty() == false) {
                    if (changePhone.length() != 10) {
                        Toast.makeText(EditProfile.this, "NotValidPhone", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (changePhone.charAt(0) != '0') {
                        Toast.makeText(EditProfile.this, "NotValidPhone", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (changePhone.charAt(1) != '5') {
                        Toast.makeText(EditProfile.this, "NotValidPhone", Toast.LENGTH_LONG).show();
                        return;
                    }
                    databaseReference.child("phone").setValue(changePhone);
                }
                databaseReference.child("region").setValue(selectedRegion);
                Intent act = new Intent(new Intent(EditProfile.this, Profile.class));
                act.putExtra("email", email_);
                startActivity(act);// Write was successful!
            }
        });
    }
    //open galery and pick the picture from galery
    public void uploadFromGalery(View view) {
        //pass galery
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    //save the selected picture in imageUri
    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
//check if upload image sucess
        if (resultCode == RESULT_OK) {
            // מה אני עושה עם התמונה הזאת
            imageUri = data.getData();
            uploadImage();

        }
    }

    // Upload Image to dataBase
    private void uploadImage() {
        if (imageUri != null) {
            // Defining the child of storageReference
            StorageReference ref = storageReference.child("images/");

            // adding listeners on upload
            // or failure of image
            ref.child("User").child(email_).putFile(imageUri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    // Image uploaded successfully
                                    Toast.makeText(EditProfile.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error, Image not uploaded
                            Toast.makeText(EditProfile.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.spinner_regions) {
            selectedRegion = regionsArray.get(position);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                Intent act = new Intent(new Intent(EditProfile.this, Profile.class));
                act.putExtra("email", email_);
                startActivity(act);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}