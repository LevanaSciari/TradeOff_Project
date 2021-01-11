package com.example.tradeoff;

//delete
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Delete_Post_Admin extends AppCompatActivity {

    LinearLayout linearLayout;
    ArrayList<Post> allPosts = new ArrayList<Post>();

    ArrayList<Post> myPosts = new ArrayList<Post>();
    Post postName;
    User user;
    String userCurrentEmail;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete__admin);
        Bundle extras = getIntent().getExtras();
        userCurrentEmail = extras.getString("email");
        linearLayout = (LinearLayout) findViewById(R.id.listofAllPosts);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        printUserPosts();

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
                Intent i = new Intent(Delete_Post_Admin.this, Administrator.class);
                Bundle extras = getIntent().getExtras();
                i.putExtra("email", extras.getString("email"));
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    int count = 0;

    public void printUserPosts() {
        linearLayout.removeAllViews();
        databaseReference.child("Posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postCurr : dataSnapshot.getChildren()) {
                    allPosts.clear();
                    postName = postCurr.getValue(Post.class);
                    allPosts.add(postName);
                    count = 0;
                    for (final Post post : allPosts) {

                        databaseReference.child("User").child(post.getMail()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Bundle extras = getIntent().getExtras();
                                String userCurrentEmail = extras.getString("email");

                                user = dataSnapshot.getValue(User.class);


                                    LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                    //You can use this view to findViewById, setOnClickListener, setText etc;
                                    final View view = layoutInflater.inflate(R.layout.coulmn_row, null, false);

                                    //FindView using inflated view
                                    TextView firstName = view.findViewById(R.id.Fname_card);
                                    TextView keyPost = view.findViewById(R.id.mail_card);
                                    ImageView phone = view.findViewById(R.id.phone_card);
                                    TextView region = view.findViewById(R.id.adress_card);
                                    TextView give = view.findViewById(R.id.give_card);
                                    TextView take = view.findViewById(R.id.take_card);
                                    firstName.setText("Name: "+user.getFirstName());
                                    keyPost.setText("Number of Post:"+ Integer.toString(count));
                                    myPosts.add(count, post);
                                    count++;
                                region.setText("Region: " + user.getRegion());
                                give.setText("Give: " + post.getGive());
                                take.setText("Take: " + post.getTake());
                                View button = view.findViewById(R.id.like);
                                button.setVisibility(View.GONE);

                                    //button to phone
                                    final Uri uri = Uri.fromParts("tel", user.getPhone(), null);
                                    phone.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(new Intent(Intent.ACTION_DIAL, uri));
                                        }
                                    });

                                    //upload image
                                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                                    final StorageReference imgRef = storageReference.child("images/").child("User/" + user.getEmail());

                                    imgRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            android.widget.ImageView picture = view.findViewById(R.id.image_card);
                                            if (task.isSuccessful()) {
                                                Uri downUri = task.getResult();
                                                String imageUri = downUri.toString();

                                                if (picture != null) {
                                                    Picasso.get().load(imageUri).into(picture);
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

    public void remove(View view) {

        final EditText keyPost = (EditText) findViewById(R.id.Num);
        final int num = Integer.parseInt(keyPost.getText().toString());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query Query = databaseReference.child("Posts");
        Query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot currPost : dataSnapshot.getChildren()) {
                    Post post = currPost.getValue(Post.class);
                    if (post.getKeyPost().equals(myPosts.get(num).getKeyPost()))
                        currPost.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("no posts");
            }
        });




        Intent i = new Intent(this, Administrator.class);
        Bundle extras = getIntent().getExtras();
        i.putExtra("email", extras.getString("email"));
        startActivity(i);

    }


    public void back(View view) {
        Intent i = new Intent(this, Administrator.class);
        Bundle extras = getIntent().getExtras();
        i.putExtra("email", extras.getString("email"));
        startActivity(i);
    }
}