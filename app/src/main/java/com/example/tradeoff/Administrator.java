package com.example.tradeoff;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Administrator extends AppCompatActivity {
    Button home;
    String currentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Bundle extras = getIntent().getExtras();
        currentEmail = extras.getString("email");
        home = (Button) findViewById(R.id.HomePage);
        home.setText("Home");
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
                Intent i = new Intent(Administrator.this, MainActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void HomePage(View view) {
        Intent i = new Intent(Administrator.this, Home.class);
        Bundle extras = getIntent().getExtras();
        i.putExtra("email", currentEmail);
        startActivity(i);

    }

    public void Change(View view) {
        Intent i = new Intent(Administrator.this, changeCategory.class);
        Bundle extras = getIntent().getExtras();
        i.putExtra("email", currentEmail);
        startActivity(i);
    }

    public void delete_post(View view) {
        Intent i = new Intent(Administrator.this, Delete_Post_Admin.class);
        Bundle extras = getIntent().getExtras();
        i.putExtra("email", currentEmail);
        startActivity(i);
    }

    public void diagramRegion(View view) {
        Intent i = new Intent(Administrator.this, DiagramRegion.class);
        Bundle extras = getIntent().getExtras();
        i.putExtra("email", currentEmail);
        startActivity(i);
    }

    public void diagramTake(View view) {

        Intent i = new Intent(Administrator.this, DiagramTake.class);
        Bundle extras = getIntent().getExtras();
        i.putExtra("email", currentEmail);
        
        startActivity(i);
    }

    public void diagramGive(View view) {
        Intent i = new Intent(Administrator.this, DiagramGive.class);
        Bundle extras = getIntent().getExtras();
        i.putExtra("email", currentEmail);
        startActivity(i);
    }

}
