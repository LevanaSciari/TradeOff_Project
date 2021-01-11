package com.example.tradeoff;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

// Import the required libraries
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;

public class DiagramTake extends AppCompatActivity {


    private DatabaseReference databaseReference;
    private ArrayList<String> ArrActivity = new ArrayList<String>();
    private ArrayList<Post> posts = new ArrayList<Post>();
    double[] count;
    int start =0;
    int end = 4;
    // Create the object of TextView
    // and PieChart class
    TextView percent1, percent2, percent3, percent4;
    TextView activity1, activity2, activity3, activity4;
    TextView act1, act2, act3, act4;
    Button next;
    PieChart pieChart;

    String  userCurrentEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagram_take);
        Bundle extras = getIntent().getExtras();
        userCurrentEmail = extras.getString("email");
        //init data base
        databaseReference = FirebaseDatabase.getInstance().getReference();
        // Link those objects with their
        // respective id's that
        // we have given in .XML file
        percent1 = findViewById(R.id.a1);
        percent2 = findViewById(R.id.a2);
        percent3 = findViewById(R.id.a3);
        percent4 = findViewById(R.id.a4);

        activity1 = findViewById(R.id.activity1);
        activity2 = findViewById(R.id.activity2);
        activity3 = findViewById(R.id.activity3);
        activity4 = findViewById(R.id.activity4);


        act1 = findViewById(R.id.act1);
        act2 = findViewById(R.id.act2);
        act3 = findViewById(R.id.act3);
        act4 = findViewById(R.id.act4);

        next= findViewById(R.id.next);

        pieChart = findViewById(R.id.piechart);

        // Creating a method setData()
        // to set the text in text view and pie chart
        setData();
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
                Intent i = new Intent(DiagramTake.this, Administrator.class);
                i.putExtra("email", userCurrentEmail);
                startActivity(i);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void setData()
    {

        databaseReference.child("Active").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot statis : dataSnapshot.getChildren()) {
                    String activity = statis.getValue(String.class);
                    ArrActivity.add(activity);
                }
                databaseReference.child("Posts").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot statis : dataSnapshot.getChildren()) {
                            Post p= statis.getValue(Post.class);

                            //User user = statis.getValue(User.class);
                            posts.add(p);
                        }


                        next.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                pieChart.clearChart();
                                count=new double[ArrActivity.size()];
                                for(int i = start; i<end&&i< ArrActivity.size(); i++) {
                                    String activity = ArrActivity.get(i);
                                    for (Post j : posts) {
                                        if(activity.equals(j.getTake())){
                                            count[i]+=1;
                                        }
                                    }
                                }

                                //regio get city from fire base
                                // set to text view
                                //
                                // Set the percentage of language used
                                for(int i=start;i<count.length;i++){
                                    double d= (count[i]/ posts.size())*100;
                                    int ans=(int)d;
                                    if(i==start){
                                        percent1.setText(Integer.toString( ans));
                                        activity1.setText(ArrActivity.get(i));
                                        act1.setText(ArrActivity.get(i));
                                    }else if(i==start+1){
                                        percent2.setText(Integer.toString( ans));
                                        activity2.setText(ArrActivity.get(i));
                                        act2.setText(ArrActivity.get(i));
                                    }else if(i==start+2){
                                        percent3.setText(Integer.toString( ans));
                                        activity3.setText(ArrActivity.get(i));
                                        act3.setText(ArrActivity.get(i));
                                    }else if(i==start+3){
                                        percent4.setText(Integer.toString( ans));
                                        activity4.setText(ArrActivity.get(i));
                                        act4.setText(ArrActivity.get(i));
                                    }
                                }

                                // Set the data and color to the pie chart
                                pieChart.addPieSlice(
                                        new PieModel(
                                                "Region 1",
                                                Integer.parseInt(percent1.getText().toString()),
                                                Color.parseColor("#FFA726")));
                                pieChart.addPieSlice(
                                        new PieModel(
                                                "Region 2",
                                                Integer.parseInt(percent2.getText().toString()),
                                                Color.parseColor("#66BB6A")));
                                pieChart.addPieSlice(
                                        new PieModel(
                                                "Region 3",
                                                Integer.parseInt(percent3.getText().toString()),
                                                Color.parseColor("#EF5350")));
                                pieChart.addPieSlice(
                                        new PieModel(
                                                "Region 4",
                                                Integer.parseInt(percent4.getText().toString()),
                                                Color.parseColor("#29B6F6")));


                                // To animate the pie chart
                                pieChart.startAnimation();
                                start+=4;
                                end+=4;
                            }
                        });
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
