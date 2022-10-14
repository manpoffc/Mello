package com.example.mello;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

        mAuth = FirebaseAuth.getInstance(); //
        loginDetails(); //
    };

    private void loginDetails(){
        emailAddress = findViewById(R.id.loginEmailAddress);
        password = findViewById(R.id.loginPassword);
        loginNow = findViewById(R.id.loginNowBtn);
        register = findViewById(R.id.loginRegisterBtn);

        String email = emailAddress.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            emailAddress.setError("Email Required");
            return;
        }
        if(TextUtils.isEmpty(pass)){
            password.setError("Password Required");
            return;
        }

        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }

            }
        });

        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
}