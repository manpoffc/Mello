package com.example.mello;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Login extends AppCompatActivity {

    private EditText emailAddress, password;
    private Button loginNow;
    private TextView register;
    private FirebaseAuth mAuth; //



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance(); //
        loginDetails(); //
    };

    private void loginDetails(){
        emailAddress = findViewById(R.id.loginEmailAddress);
        password = findViewById(R.id.loginPassword);
        loginNow = findViewById(R.id.loginNowBtn);
        register = findViewById(R.id.loginRegisterBtn);

        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class)); // From here send it to dashboard add iy
                finish();
            }
        });


    }
//Check if login details are correct
    private void userLogin(){
        String email = emailAddress.getText().toString().trim();
        String pass = password.getText().toString().trim();
        System.out.println(pass+"*******");
        if(email.isEmpty()){
            emailAddress.setError("Email is Required");
            emailAddress.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            emailAddress.setError("Enter Valid E-mail");
            emailAddress.requestFocus();
            return;
        }
        if(pass.isEmpty()){

            password.setError("Password is Required");
            password.requestFocus();
            return;
        }
        if(pass.length()<6){

            password.setError("Password must be 6 characters long");
            password.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //redirect to user dashboard
                    startActivity(new Intent(Login.this,NavigationDrawerActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(Login.this,"Login Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}