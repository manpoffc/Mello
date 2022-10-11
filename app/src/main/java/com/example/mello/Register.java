package com.example.mello;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://loginmello-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText firstName, lastName, email, password, confirmPassoword, mobile;
        final Button registerNow;
        final TextView loginNow;
        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        password = findViewById(R.id.password);
        confirmPassoword = findViewById(R.id.Confirmpassword);
        registerNow = findViewById(R.id.registerbtn);
        loginNow = findViewById(R.id.loginNow);

        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fName= firstName.getText().toString();
                String lName= lastName.getText().toString();
                String emailAddress= email.getText().toString();
                String pwd= password.getText().toString();
                String con_pwd= confirmPassoword.getText().toString();
                String phone= mobile.getText().toString();


                if(fName.isEmpty() || lName.isEmpty() || emailAddress.isEmpty() || pwd.isEmpty() || con_pwd.isEmpty() || phone.isEmpty()){

                    Toast.makeText(Register.this,"Please Enter all the credentials",Toast.LENGTH_SHORT).show();

                }
                else if(!pwd.equals(con_pwd)){
                    Toast.makeText(Register.this,"Password do not match",Toast.LENGTH_SHORT).show();
                }
                else{

                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(snapshot.hasChild(phone)){
                                Toast.makeText(Register.this,"User already exists!",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                databaseReference.child("users").child(phone).child("FullName").setValue(fName + " " + lName);
                                databaseReference.child("users").child(phone).child("Email").setValue(emailAddress);
                                databaseReference.child("users").child(phone).child("password").setValue(pwd);
                                Toast.makeText(Register.this,"Successfully Registered",Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

            }
        });

        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}