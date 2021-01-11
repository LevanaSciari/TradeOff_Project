package com.example.tradeoff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyNotificationActivity extends AppCompatActivity {
    String keyPost;
    Post p;
    User user;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    DatabaseReference databaseReference3;
    LinearLayout linearLayout;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String email;
    ArrayList<String> keyPostArrayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notification);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference2 = FirebaseDatabase.getInstance().getReference();
        databaseReference3 = FirebaseDatabase.getInstance().getReference();
        linearLayout = (LinearLayout) findViewById(R.id.listofMyNotoficaton);
        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Bundle extras = getIntent().getExtras();
        email = extras.getString("email");
        getData();
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
                Intent act = new Intent(new Intent(MyNotificationActivity.this, Profile.class));
                act.putExtra("email", email);
                startActivity(act);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void getData() {
        linearLayout.removeAllViews();
        databaseReference.child("Like").child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot currPost : dataSnapshot.getChildren()) {
                    keyPostArrayList.clear();
                    keyPost = currPost.getKey();

                    keyPostArrayList.add(keyPost);

                    for (final String s : keyPostArrayList) {
                        databaseReference.child("Like").child(email).child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot currPost : dataSnapshot.getChildren()) {
                                    final String key = currPost.getKey();
                                    databaseReference2.child("Posts").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            p=dataSnapshot.getValue(Post.class);
                                            databaseReference3.child("User").child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    user = dataSnapshot.getValue(User.class);
                                                    LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                                    final View view = layoutInflater.inflate(R.layout.column_row2, null, false);

                                                    //FindView using inflated view
                                                    TextView firstName = view.findViewById(R.id.Fname_card);
                                                    TextView mail = view.findViewById(R.id.mail_card);
                                                    ImageView phone = view.findViewById(R.id.phone_card);
                                                    TextView give = view.findViewById(R.id.give_card);
                                                    TextView take = view.findViewById(R.id.take_card);


                                                    firstName.setText("Name: " + user.getFirstName());
                                                    mail.setText(s);

                                                    give.setText("Give: " + p.getGive());
                                                    take.setText("Take: " + p.getTake());

                                                    //Phone
                                                    final Uri uri = Uri.fromParts("tel", user.getPhone(), null);
                                                    phone.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            startActivity(new Intent(Intent.ACTION_DIAL, uri));
                                                        }
                                                    });
                                                    linearLayout.addView(view);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }

                                    });
                                }
                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("The read failed: " + databaseError.getCode());
                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}



