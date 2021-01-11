package com.example.tradeoff;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class changeCategory extends AppCompatActivity {

    private EditText AddRegion;
    private EditText AddActivity;
    private EditText DeleteActivity;
    String userCurrentEmail;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        Bundle extras = getIntent().getExtras();
        userCurrentEmail = extras.getString("email");
        AddRegion = (EditText)findViewById(R.id.add_region);
        AddActivity = (EditText)findViewById(R.id.add_activity);
        DeleteActivity=(EditText)findViewById(R.id.delete_activity);
        databaseReference = FirebaseDatabase.getInstance().getReference();
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
                Intent i = new Intent(this, Administrator.class);
                i.putExtra("email", userCurrentEmail);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    public void add_region(View view) {
        push("Region");
    }

    public void add_activity(View view) {
        push("Active");
    }

    public void delete_activity(View view) {
        DeleteActivity("Active");
    }



    private void DeleteActivity(final String c){
        databaseReference.child(c).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String s = DeleteActivity.getText().toString().trim().toLowerCase();
                for (DataSnapshot statis : dataSnapshot.getChildren()) {
                    String getFDB = statis.getValue(String.class);
                    if (getFDB.equals(s)) {
                        statis.getRef().removeValue();
                        Toast.makeText(changeCategory.this, "Delete Activity successful", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //remove post with activity which deleted
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query Query = databaseReference.child("Posts");
        Query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String s = DeleteActivity.getText().toString().trim().toLowerCase();
                for (DataSnapshot currPost : dataSnapshot.getChildren()) {
                    Post post = currPost.getValue(Post.class);
                    if (post.getGive().equals(s)||post.getTake().equals(s))
                        currPost.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("no posts");
            }
        });

    }

    private void push(final String typeToPush){

        databaseReference.child(typeToPush).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String s="";
                if(typeToPush=="Region"){
                    s=AddRegion.getText().toString().trim().toLowerCase();
                }
                else{
                    s=AddActivity.getText().toString().trim().toLowerCase();
                }
                boolean flag=true;
                for (DataSnapshot curretnType : dataSnapshot.getChildren()) {
                    String typeName=curretnType.getValue(String.class);
                            if(typeName.equals(s)){
                                flag=false;
                            }
                }
                if(!s.equals("")&&flag==true) {
                    String postId = databaseReference.push().getKey();
                    databaseReference.child(typeToPush).child(postId).setValue(s)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(changeCategory.this, "Success", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(changeCategory.this, "not success", Toast.LENGTH_LONG).show();

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}