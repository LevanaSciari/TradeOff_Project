package com.example.tradeoff;
//levana dayan

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;


public class MainActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.Email);
        password = (EditText) findViewById(R.id.Pass);


    }

    public void connect(View view) {
        final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "Please wait...", "Processing...", true);
        final String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            progressDialog.dismiss();
            Toast.makeText(MainActivity.this, "Email or Password empty", Toast.LENGTH_LONG).show();
            return;
        }

        auth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    //Administrator Id
                    String administratorID = "9A9TvNlwCsNesULmMAUSQWQcgO62";
                    //cheking if the user is administrator
                    if (auth.getCurrentUser().getUid().equals(administratorID)) {
                        Intent administratorIntent = new Intent(MainActivity.this, Administrator.class);
                        administratorIntent.putExtra("email", email.getText().toString().trim());
                        Toast.makeText(MainActivity.this, "Welcome to TradeOff", Toast.LENGTH_LONG).show();

                        Intent service = new Intent(MainActivity.this, notification.class);
                        service.putExtra("email", email.getText().toString().trim());
                        service.putExtra("connection",false);
                        startService(service);

                        startActivity(administratorIntent);
                        finish();


                    } else {
                        Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(MainActivity.this, Home.class);
                        i.putExtra("email", email.getText().toString().trim());

                        Intent service = new Intent(MainActivity.this, notification.class);
                        service.putExtra("email", email.getText().toString().trim());
                        service.putExtra("connection",false);
                        startService(service);

                        startActivity(i);
                        finish();
                    }
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthException e) {
                        Toast.makeText(getApplicationContext(), "Invalid Email or Password", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }


    public void Forgot(View view) {
        startActivity(new Intent(this, Forgot_Password.class));
    }

    public void NotAccount(View view) {
        startActivity(new Intent(this, RegisterUser.class));
    }
}
