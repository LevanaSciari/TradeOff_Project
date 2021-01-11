package com.example.tradeoff;
//levana sciari

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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


public class Home extends AppCompatActivity {

    ArrayList<Post> postArrayList = new ArrayList<Post>();
    Post postValues;
    User user;
    DatabaseReference databaseReference;
    LinearLayout linearLayout;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        linearLayout = (LinearLayout) findViewById(R.id.listofMyPosts_home);
        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        getData();
    }

    //for option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.myprofile:
                Intent tent = new Intent(this, Profile.class);
                Bundle extras = getIntent().getExtras();
                tent.putExtra("email", extras.getString("email"));
                startActivity(tent);
                return true;
            case R.id.search:
                Intent s = new Intent(this, Search.class);
                Bundle search = getIntent().getExtras();
                s.putExtra("email", search.getString("email"));
                startActivity(s);
                return true;
            case R.id.newpost:
                Intent forgot = new Intent(Home.this, CreatePost.class);
                Bundle extra = getIntent().getExtras();
                forgot.putExtra("email", extra.getString("email"));
                startActivity(forgot);
                finish();
                return true;
            case R.id.exit:
                String currentUserID = "9A9TvNlwCsNesULmMAUSQWQcgO62";
                if (auth.getCurrentUser().getUid().equals(currentUserID)) {
                    Intent Exit = new Intent(Home.this, Administrator.class);
                    Bundle extra_exit = getIntent().getExtras();
                    Exit.putExtra("email", extra_exit.getString("email"));
                    startActivity(Exit);
                    finish();
                } else {
                    Intent Exit = new Intent(Home.this, MainActivity.class);
                    startActivity(Exit);
                    finish();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void getData() {
        linearLayout.removeAllViews();
        databaseReference.child("Posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot currPost : dataSnapshot.getChildren()) {
                    postArrayList.clear();
                    postValues = currPost.getValue(Post.class);

                    postArrayList.add(postValues);
                    for (final Post post : postArrayList) {
                        databaseReference.child("User").child(post.getMail()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                user = dataSnapshot.getValue(User.class);

                                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                final View view = layoutInflater.inflate(R.layout.coulmn_row, null, false);

                                //FindView using inflated view
                                TextView firstName = view.findViewById(R.id.Fname_card);
                                TextView mail = view.findViewById(R.id.mail_card);
                                ImageView phone = view.findViewById(R.id.phone_card);
                                TextView region = view.findViewById(R.id.adress_card);
                                TextView give = view.findViewById(R.id.give_card);
                                TextView take = view.findViewById(R.id.take_card);


                                firstName.setText("Name: " + user.getFirstName());
                                mail.setText( user.getEmail());
                                region.setText("Region: " + user.getRegion());
                                give.setText("Give: " + post.getGive());
                                take.setText("Take: " + post.getTake());
                                //Phone
                                final Uri uri = Uri.fromParts("tel", user.getPhone(), null);
                                phone.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(Intent.ACTION_DIAL, uri));
                                    }
                                });

                                ImageView like = view.findViewById(R.id.like);
                                Bundle extra_exit = getIntent().getExtras();

                                String temp=extra_exit.getString("email");
                                String temp2="";
                                for(int i = 0 ; i <temp.length() ; i++){
                                    if(temp.charAt(i)=='.'){
                                        temp2+='_';
                                    }else{
                                        temp2+=temp.charAt(i);
                                    }
                                }
                                final String my_mail_final =temp2;

                                final String mail_final =user.getEmail();
                                final String take_final =post.getTake();
                                final String give_final =post.getGive();
                                final String postId = databaseReference.push().getKey();
                                //like
                                like.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        databaseReference.child("Like").child(mail_final).child(my_mail_final).child(post.getKeyPost()).setValue("You have new notification");
                                    }
                                });
                                //image
                                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                                final StorageReference imgRef = storageReference.child("images/").child("User/" + user.getEmail());

                                imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        android.widget.ImageView picture = view.findViewById(R.id.image_card);
                                        if (task.isSuccessful()) {
                                            Uri downUri = task.getResult();
                                            String imageUrl = downUri.toString();
                                            if (picture != null) {
                                                Picasso.get().load(imageUrl).into(picture);
                                            }
                                        } else {
                                            System.out.println("Eror");

                                        }
                                    }
                                });
                                linearLayout.addView(view);
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
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }



}

