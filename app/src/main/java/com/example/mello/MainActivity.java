package com.example.mello;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

//        mAuth = FirebaseAuth.getInstance();

//        if(mAuth.getCurrentUser() !=null) {
//            if(true) {
//            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//        }

        mDialog = new ProgressDialog(this);

//        loginDetails();
    }


}