package com.example.mello;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private EditText firstName, lastName, phone, emailId, password;
    private Button registerNow;
    private TextView loginNow;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        phone = findViewById(R.id.phone);
        emailId = findViewById(R.id.registerEmailAddress);
        password = findViewById(R.id.registerPassword);
        registerNow = findViewById(R.id.registerNowBtn);
        loginNow = findViewById(R.id.registerLoginBtn);

        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this,Login.class));
                finish();
            }
        });
        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fName = firstName.getText().toString().trim();
                String lName = lastName.getText().toString().trim();
                String email = emailId.getText().toString().trim();
                String pwd = password.getText().toString().trim();
                String contact = phone.getText().toString().trim();


                if (lName.isEmpty()){
                    lastName.setError("required");
                    lastName.requestFocus();
                    return;
                }
                if (fName.isEmpty()){
                    firstName.setError("required");
                    firstName.requestFocus();
                    return;
                }

                if (contact.isEmpty()){
                    phone.setError("required");
                    phone.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailId.setError("Invalid Email ID");
                    emailId.requestFocus();
                    return;
                }
                if (email.isEmpty()){
                    emailId.setError("required");
                    emailId.requestFocus();
                    return;
                }
                if (pwd.isEmpty()){
                    password.setError("required");
                    password.requestFocus();
                    return;
                }
                if (pwd.length()<6){
                    password.setError("Password should be at least 6 characters long");
                    password.requestFocus();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email,pwd)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                System.out.println("##########USER##########");
                                if (task.isSuccessful()){
                                    User user = new User(fName, lName, email, pwd, contact);
                                    System.out.println(user + "##########USER##########");

                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(Register.this, "User Registered!", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                        Toast.makeText(Register.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                }
                                else{
                                    Toast.makeText(Register.this, "Unsuccessful", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });


            }
        });
    }
}