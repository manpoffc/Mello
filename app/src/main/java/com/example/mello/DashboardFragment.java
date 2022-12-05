package com.example.mello;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DashboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatabaseReference mExpenseReference;
    private DatabaseReference mIncomeReference;

    FirebaseAuth firebaseAuth;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();

    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //Dashboard Income and Expense
    private TextView totalIncome;
    private TextView totalExpense;
    private TextView totalBalance;
    float IncomeSum = 0;
    float ExpenseSum = 0;

    public DashboardFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //myView
        mIncomeReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Income");
        mExpenseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Expenses");
        View myView = inflater.inflate(R.layout.fragment_dashboard, container,false);
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        //Total Income and Expense
        totalIncome = myView.findViewById(R.id.Income_result);
        totalExpense = myView.findViewById(R.id.Expense_result);
        totalBalance = myView.findViewById(R.id.Balance_result);


        //int balanceSum = (Integer.parseInt(String.valueOf(totalIncome)) - (Integer.parseInt(String.valueOf(totalExpense))));

        mIncomeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float totalsum = 0;
                for (DataSnapshot mysnap:snapshot.getChildren() ){
                    //System.out.println(mysnap.getValue());
                    IncomeData data = new IncomeData();
                    for (DataSnapshot ds:mysnap.getChildren()) {
                        //IncomeData data = mysnap.getValue(IncomeData.class);
                        if (ds.getKey().equals("amount")) {
                            totalsum += Integer.parseInt(String.valueOf(ds.getValue()));
                            //System.out.println("------"+(totalsum));
                            //totalIncome.setText(String.valueOf(totalsum));
                        }

                    }

                }
                IncomeSum = totalsum;
                totalIncome.setText(String.valueOf(totalsum));

            }

            //@Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        mExpenseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float totalexpense = 0;
                for (DataSnapshot mysnapshot : snapshot.getChildren()) {
                    //ExpenseData data = mysnapshot.getValue(ExpenseData.class);
                    for (DataSnapshot ds : mysnapshot.getChildren()) {
                        //IncomeData data = mysnap.getValue(IncomeData.class);
                        if (ds.getKey().equals("amount")) {
                            totalexpense += Integer.parseInt(String.valueOf(ds.getValue()));
                            //totalIncome.setText(String.valueOf(totalsum));
                        }

                    }

                }
                //System.out.println("------"+(totalexpense));
                ExpenseSum = totalexpense;
                totalExpense.setText(String.valueOf(totalexpense));
                float finalBalance = IncomeSum - ExpenseSum;
                System.out.println("------****"+(finalBalance));
                totalBalance.setText(String.valueOf(finalBalance));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        System.out.println("------"+(IncomeSum-ExpenseSum));
        //totalBalance.setText(String.valueOf(IncomeSum-ExpenseSum));

        return myView;
        //return inflater.inflate(R.layout.fragment_dashboard, container, false);
        //return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

}