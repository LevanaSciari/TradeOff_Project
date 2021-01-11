package com.example.tradeoff;


import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class Forgot_Password extends AppCompatActivity {

    private EditText emailEditText;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_code);
        auth = FirebaseAuth.getInstance();
        emailEditText = (EditText) findViewById(R.id.Email);
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


    public void Reset(View view) {
        //מה קורה שלוחצים על reset password
        String email = emailEditText.getText().toString().trim();

        if (email.isEmpty()) {

            Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
            return;
        }
        //check if the email is valid
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_LONG).show();
        }
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Forgot_Password.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Forgot_Password.this, MainActivity.class));
                            finish();
                        } else {
//                            Toast.makeText(Forgot_Password.this, "Failed to send reset email!" , Toast.LENGTH_SHORT).show();
//                        }
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                Toast.makeText(getApplicationContext(), "Failed to send reset email! /n" + e.getMessage(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }
}
