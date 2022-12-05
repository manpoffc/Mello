package com.example.mello;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.UUID;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InsertExpenseFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    DataSnapshot dsp;
    // String[] categories = new String[]{"Food","Entertainment","Car","Fuel"};
    AutoCompleteTextView categoryAuto, groupAuto;
    TextInputLayout textInputLayout;
    TextInputLayout groupInputLayout;
    String[] categories = {"Food","Entertainment","Car","Fuel"};
    String[] groupCategories;
    ArrayAdapter<String> adapterItems,groupItems;
    public String expenseId;
    int amt;
    private DatabaseReference mExpenseReference,groupExpenseReference,userExpenseReference;
    String groupID;
    UserModel userModel, users;
    ArrayList<UserModel> userModels,usm;
    ExpenseData expense;
    GroupModel groupModel,groups;
    ArrayList<GroupModel> groupModelArrayList;
    EditText edtName;
    EditText edtAmount;
    EditText edtDate;
    EditText edtCategory, edtGroup;
    EditText edtComment;
    MaterialButton save;
    MaterialButton cancel;
    RadioButton groupRadioBtn;
    RadioButton individualRadioBtn;



    private String mParam1;
    private String mParam2;
    public InsertExpenseFragment() {

    }

    public static InsertExpenseFragment newInstance(String param1, String param2) {
        InsertExpenseFragment fragment = new InsertExpenseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.inser_expense, container, false);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtName= view.findViewById(R.id.NameEditText);
        edtAmount = view.findViewById(R.id.AmountEditText);
        edtDate = view.findViewById(R.id.DateEditText);
        edtComment = view.findViewById(R.id.noteEditText);
        edtCategory = view.findViewById(R.id.category_autocomplete);
        edtGroup = view.findViewById(R.id.group_dropdown);
        groupModelArrayList = new ArrayList<>();

        groups=new GroupModel();
        categoryAuto = view.findViewById(R.id.category_autocomplete);
        groupAuto = view.findViewById(R.id.group_dropdown);
        textInputLayout=view.findViewById(R.id.CategoryInputLayout);
        groupRadioBtn = view.findViewById(R.id.group_button);
        groupInputLayout = view.findViewById(R.id.groupInputLayout);
        individualRadioBtn =view.findViewById(R.id.individual_button);

        adapterItems=new ArrayAdapter<>(getContext(),R.layout.list_categories, categories);

        categoryAuto.setAdapter(adapterItems);

        groupExpenseReference=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Group");

        groupExpenseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    groupModel = new GroupModel();
                    System.out.println("&&&&&&&&&&&&&&&&&&"+dataSnapshot.getKey());
                    groupModel.setGroupId(dataSnapshot.getKey());
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        if(ds.getKey().equals("groupName")){
                            System.out.println("GROUPNAME#######"+ ds.getValue().toString());
                            groupModel.setGroupName(ds.getValue().toString());
                        }
                    }
                    groupModelArrayList.add(groupModel);
                }
                groups.setGroupModelArrayList(groupModelArrayList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        categoryAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        groupRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupInputLayout.setVisibility(0);
                System.out.println("GROUP EXPENSE LIST %%%%%%%%%%%" + groups.getGroupModelArrayList());
                groupCategories = new String[groups.getGroupModelArrayList().size()];
                for(int i=0; i<groups.getGroupModelArrayList().size();i++){
                    groupCategories[i]=groups.getGroupModelArrayList().get(i).getGroupName();
                }
                groupItems =new ArrayAdapter<>(getContext(),R.layout.list_categories, groupCategories);

                groupAuto.setAdapter(groupItems);
                System.out.println("GROUP CAT66666666666"+ groupAuto.getText().toString());
            }
        });

        individualRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(groupInputLayout.getVisibility()==0){
                    groupInputLayout.setVisibility(8);

                }
            }
        });

            System.out.println("GROUP  111111111111111111" + edtGroup.getText().toString());

        save = view.findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    addExpenseData();
            }
        });

        cancel=view.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(groupRadioBtn.isChecked()){
                    System.out.println("GROUP  111111111111111111" + edtGroup.getText().toString());

                }
                FragmentTransaction transact = getParentFragmentManager().beginTransaction();
                transact.replace(getId(),ExpenseFragment.class,null);
                System.out.println("After replace code");
                transact.commit();
            }
        });


    }

    public void addExpenseData(){

        String name = edtName.getText().toString().trim();
        String category = edtCategory.getText().toString().trim();
        String date = edtDate.getText().toString().trim();
        String comment = edtComment.getText().toString().trim();
        String amount = edtAmount.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            edtName.setHint("Required field");
            return;
        }
        if(TextUtils.isEmpty(amount)){
            edtAmount.setHint("Required field");
            return;
        }

        int amountInt = Integer.parseInt(amount);

        ExpenseData expense = new ExpenseData(name,amountInt,date,category,comment);
        ExpenseData exp = new ExpenseData(name,amountInt,date,category,comment);
        expenseId=UUID.randomUUID().toString();
        expense.setExpenseID(expenseId);
        users= new UserModel();
        userModels= new ArrayList<>();
        usm= new ArrayList<>();
        if(groupRadioBtn.isChecked()){

            for(int i=0;i<groups.getGroupModelArrayList().size();i++){
                if(groups.getGroupModelArrayList().get(i).groupName.equals(edtGroup.getText().toString())){
                    groupID= groups.getGroupModelArrayList().get(i).groupId;
                }
            }
            userExpenseReference= FirebaseDatabase.getInstance().getReference("Groups").child(groupID).child("TotalAmount");
           userExpenseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
               @Override
               public void onComplete(@NonNull Task<DataSnapshot> task) {
                   if(task.isSuccessful()){
                        DataSnapshot ds= task.getResult();
                       userExpenseReference.setValue(Integer.valueOf(edtAmount.getText().toString())+Integer.valueOf(ds.getValue().toString()));
                   }
               }
           });

            System.out.println("GROUPS ID@@@@@@@@@@@@@@@"+ groupID);

            FirebaseDatabase.getInstance().getReference("Groups").child(groupID).child("Members").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        dsp=task.getResult();

                        for(DataSnapshot ds: dsp.getChildren()){
                            userModel = new UserModel(ds.getKey().toString(),ds.getValue().toString());
                            usm.add(userModel);
                        }
                        amt = Integer.valueOf(edtAmount.getText().toString())/(usm.size());
                        expense.setAmount(Integer.valueOf(edtAmount.getText().toString())/(usm.size()));
                        for(int i=0;i<usm.size();i++){
                            if(usm.get(i).userID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){

                                int TotalAmount= Integer.valueOf(edtAmount.getText().toString());
                                int totalMembers= usm.size()-1;
                                int FinalAmount = (TotalAmount*totalMembers)/usm.size();
                                exp.setAmount(-FinalAmount);
                                System.out.println("FINAL AMOUNT&&&&&&&&"+ FinalAmount);
                                FirebaseDatabase.getInstance().getReference("Users").child(usm.get(i).userID).child("Expenses").child(expenseId)
                                        .setValue(exp);
                                userExpenseReference=FirebaseDatabase.getInstance().getReference("Users").child(usm.get(i).userID).child("Group").child(groupID).child("currentAmount");

                                userExpenseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DataSnapshot ds= task.getResult();
                                            userExpenseReference.setValue(FinalAmount+Integer.valueOf(ds.getValue().toString()));
                                        }
                                    }
                                });
                            }
                            else {
                                userExpenseReference=FirebaseDatabase.getInstance().getReference("Users").child(usm.get(i).userID).child("Group").child(groupID).child("currentAmount");
                                userExpenseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if(task.isSuccessful()){
                                            int currentValue= (Integer.valueOf(edtAmount.getText().toString()) / (usm.size()));
                                            DataSnapshot ds= task.getResult();
                                            System.out.println(ds.getValue().toString()+"####################");
                                            userExpenseReference.setValue(currentValue+Integer.valueOf(ds.getValue().toString()));
                                        }
                                    }
                                });
                                FirebaseDatabase.getInstance().getReference("Users").child(usm.get(i).userID).child("Expenses").child(expenseId)
                                        .setValue(expense);
                            }
                        }



                    }
                }
            });
            System.out.println("SIZE of ARRAY"+ usm.size());
                       /* userExpenseReference= FirebaseDatabase.getInstance().getReference("Groups").child(groupID);
            userExpenseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    System.out.println("USERSMODELS**************"+ snapshot.getValue().toString());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            usm= users.getUserModelArrayList();
            System.out.println("USERSMODEL////////" + usm);
            //amt= amt/users.getUserModelArrayList().size();
            for (int i=0; i<users.getUserModelArrayList().size();i++){

                FirebaseDatabase.getInstance().getReference("Users").child(users.getUserModelArrayList().get(i).userID).child("Group").child(groupID).child("currentAmount").setValue(amt);
            }
           // amt = Integer.valueOf(edtAmount.getText().toString())/(users.getUserModelArrayList().size()-1);
            */
            FragmentTransaction transact = getParentFragmentManager().beginTransaction();
            transact.replace(getId(),ExpenseFragment.class,null);
            System.out.println("After replace code");
            transact.commit();
        }
        else{

            mExpenseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Expenses").child(expenseId);
            mExpenseReference.setValue(expense).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getContext(),"uploaded",Toast.LENGTH_SHORT).show();
                    FragmentTransaction transact = getParentFragmentManager().beginTransaction();
                    transact.replace(getId(),ExpenseFragment.class,null);
                    System.out.println("After replace code");
                    transact.commit();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),"Error!",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
