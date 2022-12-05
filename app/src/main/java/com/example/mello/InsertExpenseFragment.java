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
import java.util.UUID;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InsertExpenseFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

   // String[] categories = new String[]{"Food","Entertainment","Car","Fuel"};
    AutoCompleteTextView categoryAuto;
    TextInputLayout textInputLayout;
    TextInputLayout groupInputLayout;
    String[] categories = {"Food","Entertainment","Car","Fuel"};
    ArrayAdapter<String> adapterItems;
    public String expenseId;

    private DatabaseReference mExpenseReference;

    ExpenseData expense;

    EditText edtName;
    EditText edtAmount;
    EditText edtDate;
    EditText edtCategory;
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

        categoryAuto = view.findViewById(R.id.category_autocomplete);
        textInputLayout=view.findViewById(R.id.CategoryInputLayout);
        groupRadioBtn = view.findViewById(R.id.group_button);
        groupInputLayout = view.findViewById(R.id.groupInputLayout);
        individualRadioBtn =view.findViewById(R.id.individual_button);

        adapterItems=new ArrayAdapter<>(getContext(),R.layout.list_categories, categories);

        categoryAuto.setAdapter(adapterItems);

        categoryAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        groupRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupInputLayout.setVisibility(0);
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
        expenseId=UUID.randomUUID().toString();
        expense.setExpenseID(expenseId);

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
