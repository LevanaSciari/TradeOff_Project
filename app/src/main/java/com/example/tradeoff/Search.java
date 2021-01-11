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

import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Search extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner region_spinner;
    private Spinner activity_spinner;

    DatabaseReference databaseReference;
    String activity_search;
    String region_search;

    ArrayList<String> regionArray =new ArrayList<String>();
    ArrayList<String> activeArray =new ArrayList<String>();

    LinearLayout linearLayout;
    ArrayList<Post> postArrayList =new ArrayList<Post>();
    Post postValues;
    User user;
    String userCurrentEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Bundle extras = getIntent().getExtras();
         userCurrentEmail = extras.getString("email");
        System.out.println(userCurrentEmail+"tesssss");
        databaseReference = FirebaseDatabase.getInstance().getReference();

//**********************spinner*************************


        databaseReference.child("Region").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot currRegion : dataSnapshot.getChildren()) {
                    String regionName = currRegion.getValue(String.class);
                    regionArray.add(regionName);
                }
                region_spinner = (Spinner) findViewById(R.id.city_spinner);
                region_spinner.setOnItemSelectedListener(Search.this);

                ArrayAdapter ad
                        = new ArrayAdapter(
                        Search.this,
                        android.R.layout.simple_spinner_item,
                        regionArray);

                region_spinner.setAdapter(ad);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference.child("Active").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot activeCurr : dataSnapshot.getChildren()) {
                    String activeName = activeCurr.getValue(String.class);
                    activeArray.add(activeName);
                }

                activity_spinner = (Spinner) findViewById(R.id.work_spinner);
                activity_spinner.setOnItemSelectedListener(Search.this);

                ArrayAdapter ad1
                        = new ArrayAdapter(
                        Search.this,
                        android.R.layout.simple_spinner_item,
                        activeArray);

                activity_spinner.setAdapter(ad1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        linearLayout = (LinearLayout) findViewById(R.id.listofMyPosts);


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Spinner spin = (Spinner)parent;
        Spinner spin2 = (Spinner)parent;
        if(spin.getId() == R.id.work_spinner)
        {
            activity_search = activeArray.get(position);
        }

        if(spin2.getId() == R.id.city_spinner)
        {
            region_search = regionArray.get(position);
        }


    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void search_click(View view) {
        linearLayout.removeAllViews();
        databaseReference.child("Posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot currPost : dataSnapshot.getChildren())
                {
                    postArrayList.clear();
                    postValues = currPost.getValue(Post.class);

                    postArrayList.add(postValues);
                    for(final Post post: postArrayList){
                        databaseReference.child("User").child(post.getMail()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                user = dataSnapshot.getValue(User.class);
                                if(region_search.equals(user.getRegion())&& activity_search.equals(post.getGive())) {
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
                                    mail.setText(  user.getEmail());
                                    region.setText("Region: " + user.getRegion());
                                    give.setText("Give: " + post.getGive());
                                    take.setText("Take: " + post.getTake());


                                    //Phone
                                    final Uri uri=Uri.fromParts("tel", user.getPhone(), null);
                                    phone.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(new Intent(Intent.ACTION_DIAL, uri));
                                        }
                                    });

                                    //like button
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
                                    //like

                                    //like
                                    like.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            databaseReference.child("Like").child(mail_final).child(my_mail_final).child(post.getKeyPost()).setValue("You have new notification");
                                        }
                                    });
                                    //image
                                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                                    final StorageReference imgRef= storageReference.child("images/").child("User/"+ user.getEmail());

                                    imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            android.widget.ImageView iv= view.findViewById(R.id.image_card);
                                            if (task.isSuccessful()) {
                                                Uri downUri = task.getResult();
                                                String imageUrl = downUri.toString();
                                                if(iv!=null) {
                                                    Picasso.get().load(imageUrl).into(iv);
                                                }
                                            }else{
                                                System.out.println("Eror");

                                            }
                                        }
                                    });

                                    linearLayout.addView(view);

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
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
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
                Intent i = new Intent(Search.this, Home.class);
                i.putExtra("email", userCurrentEmail);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
