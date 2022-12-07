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
import android.widget.Toast;
import java.util.UUID;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

public class InsertIncomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // String[] categories = new String[]{"Food","Entertainment","Car","Fuel"};
    AutoCompleteTextView categoryAuto;
    TextInputLayout textInputLayout;
    String[] categories = {"Salary(Full-Time)","Salary(Part-Time)","Shares","Others"};

    ArrayAdapter<String> adapterItems;

    public String incomeId;
    private FirebaseAuth mAuth;

    private DatabaseReference mIncomeReference;
    FirebaseDatabase firebasedatabase;

    IncomeData income;

    EditText edtName;
    EditText edtAmount;
    EditText edtDate;
    EditText edtCategory;
    EditText edtComment;
    MaterialButton save;
    MaterialButton cancel;


    private String mParam1;
    private String mParam2;
    public InsertIncomeFragment() {

    }



    public static InsertIncomeFragment newInstance(String param1, String param2) {
        InsertIncomeFragment fragment = new InsertIncomeFragment();
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
        return inflater.inflate(R.layout.insert_income, container, false);
    }

    @SuppressLint("CutPasteId")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtName= view.findViewById(R.id.IncomeNameEditText);
        edtAmount = view.findViewById(R.id.AmountEditText);
        edtDate = view.findViewById(R.id.DateEditText);
        edtComment = view.findViewById(R.id.IncomenoteEditText);
        edtCategory = view.findViewById(R.id.category_income_autocomplete);

        categoryAuto = view.findViewById(R.id.category_income_autocomplete);
        textInputLayout=view.findViewById(R.id.CategoryInputLayout);

        adapterItems=new ArrayAdapter<>(getContext(),R.layout.list_categories,categories);

        categoryAuto.setAdapter(adapterItems);

        categoryAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });




        save = view.findViewById(R.id.save_button);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addIncomeData();
            }
        });

        cancel=view.findViewById(R.id.cancel_button);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transact = getParentFragmentManager().beginTransaction();
                transact.replace(getId(),DashboardFragment.class,null);
                System.out.println("After replace code");
                transact.commit();
            }
        });




    }

    public void addIncomeData(){

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
        incomeId= UUID.randomUUID().toString();

        IncomeData income = new IncomeData(name,amountInt,date,category,comment);
        income.setIncomeID(incomeId);
        mIncomeReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Income").child(incomeId);
        mIncomeReference.setValue(income).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(),"uploaded",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error!",Toast.LENGTH_SHORT).show();
            }
        });


    }
}
