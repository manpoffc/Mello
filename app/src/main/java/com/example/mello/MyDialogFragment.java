package com.example.mello;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import java.util.UUID;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyDialogFragment extends DialogFragment {
    private EditText cats;
    String [] userNames;
    String [] userId;
    public IndividualGroup individualGroup;
    public String input;
    public UUID uuid;
    public String currentUserName ;
    public Integer current;
    public ArrayList <Integer> finalUsers;
    private Button onCancel, onAdd;
    public ArrayList<GroupUsers> usersArrayList;
    public ArrayList<GroupUsers> appendUserList, finalUserList;
    public boolean [] selectedUsers;
    private TextView selectUsers;
    public String groupUniqueID;
    public GroupUsers groupAccess= new GroupUsers();
    private DatabaseReference databaseReference, groupDatabase;
    public String text;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialogfragment,container,false);
        onCancel = view.findViewById(R.id.dialogCancel);
        onAdd = view.findViewById(R.id.dialogAdd);
        finalUsers = new ArrayList<>();
        usersArrayList = new ArrayList<>();
        appendUserList = new ArrayList<>();
        cats = view.findViewById(R.id.addText);
        selectUsers= view.findViewById(R.id.usersList);
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    GroupUsers groupUsers = new GroupUsers(dataSnapshot.child("first_name").getValue().toString(),dataSnapshot.getKey().toString());
                    usersArrayList.add(groupUsers);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        selectUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                  getContext()
                );
                builder.setTitle("Select Users");
                builder.setCancelable(false);

                System.out.println("HERHERHERHEHR"+usersArrayList);
                userNames = new String[usersArrayList.size()];

                userId = new String[usersArrayList.size()];
                for(int i =0; i<usersArrayList.size(); i++){

                    userNames[i]= usersArrayList.get(i).name;
                    userId[i]=usersArrayList.get(i).userID;
                }
                selectedUsers = new boolean[userNames.length];
                builder.setMultiChoiceItems(userNames, selectedUsers, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {

                        if(b){
                            finalUsers.add(i);
                        }
                        else{
                            finalUsers.remove(i);
                        }
                    }
                }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder text = new StringBuilder();
                        for(int x=0; x<finalUsers.size();x++){

                            appendUserList.add(usersArrayList.get(finalUsers.get(x)));
                            text.append(usersArrayList.get(finalUsers.get(x)).name);

                            if(x!=finalUsers.size()-1){

                                text.append(", ");
                            }

                            selectUsers.setText(text.toString());

                        }
                        System.out.println("phir se aagya"+appendUserList);
                        groupAccess.setGroupUsers(appendUserList);



                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.show();

            }

        });


        onCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                //System.out.println("Dialog Fragment" + groupAccess.getGroupUsers().get(0).userID);
                getDialog().dismiss();
            }
        });
        onAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            input = cats.getText().toString().trim();
            uuid=UUID.randomUUID();
            current=0;
           individualGroup = new IndividualGroup(input,current.toString());
            groupUniqueID = uuid.toString();

                groupDatabase = FirebaseDatabase.getInstance().getReference("Groups").child(groupUniqueID);
            String currentUserID= FirebaseAuth.getInstance().getCurrentUser().getUid();


                DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        currentUserName=snapshot.child("first_name").getValue().toString();

                        groupDatabase.child("Members").child(currentUserID).setValue(currentUserName);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                System.out.println("USERNAME%%%%%%%%"+currentUserName);
                System.out.println("USERID%%%%%%%"+currentUserID);
                databaseReference = FirebaseDatabase.getInstance().getReference("Users").
                        child(FirebaseAuth.getInstance().
                                getCurrentUser().getUid()).child("Group");
                databaseReference.child(groupUniqueID).setValue(individualGroup);


                finalUserList =groupAccess.getGroupUsers();
                DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("Users");
                for(int i=0;i<finalUserList.size();i++){

                    GroupUsers gp = new GroupUsers(finalUserList.get(i).name,finalUserList.get(i).userID);

                    groupDatabase.child("Members").child(gp.userID).setValue(gp.name);
                    dbr.child(gp.userID).child("Group").child(groupUniqueID).setValue(individualGroup);

                }
                groupDatabase.child("Members").child(currentUserID).setValue(currentUserName);

                groupDatabase.child("TotalAmount").setValue(0);
                getDialog().dismiss();
            }

        });

        return view;
    }
}
