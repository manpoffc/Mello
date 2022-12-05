package com.example.mello;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpenseFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText edtname,edtamount,edtdate,edtcomment;
    private AutoCompleteTextView edtcategory;
    private Button updatebutton;
    private Button deletebutton;
    private Button sortbutton;

   // private ArrayList<Expense> expenses;
    private  ArrayList<ExpenseData> expenses;
    private  ArrayList<ExpenseData> sortexpenses;

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    public ExpenseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExpenseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExpenseFragment newInstance(String param1, String param2) {
        ExpenseFragment fragment = new ExpenseFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expense, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        expenses= new ArrayList<>();
        sortexpenses= new ArrayList<>();
        MyAdapter myAdapter = new MyAdapter(getContext(),expenses);
        recyclerView.setAdapter(myAdapter);
        sortbutton = view.findViewById(R.id.sortbutton);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Expenses");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    System.out.println("KEY_________________"+ dataSnapshot.getKey());

                    System.out.println("VALUE_______________"+ dataSnapshot.toString());
                    //Expense exp = new Expense(dataSnapshot.getKey(), Integer.valueOf(dataSnapshot.getValue().toString()));
                   // ExpenseData exp = dataSnapshot.getValue(ExpenseData.class);
                  //  ExpenseData exp1 = new ExpenseData(exp.getName(),exp.getAmount(),exp.getDate(),exp.getCategory(),exp.getComment());
                    String n = dataSnapshot.child("name").getValue(String.class);
                    Integer a = dataSnapshot.child("amount").getValue(Integer.class);
                    String c = dataSnapshot.child("category").getValue(String.class);
                    String c1 = dataSnapshot.child("comment").getValue(String.class);
                    String d = dataSnapshot.child("date").getValue(String.class);
                    String key = dataSnapshot.getKey().toString();

                    ExpenseData exp1 = new ExpenseData(n,a,d,c,c1);
                    exp1.setExpenseID(key);
                    expenses.add(exp1);
                }

                myAdapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sortbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("inside sort on click");
                databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Expenses");
                Query sortquery = databaseReference.orderByChild("amount");
                sortquery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        expenses.clear();

                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                            System.out.println("KEY_________________"+ dataSnapshot.getKey());

                            System.out.println("VALUE_______________"+ dataSnapshot.toString());
                            String n = dataSnapshot.child("name").getValue(String.class);
                            Integer a = dataSnapshot.child("amount").getValue(Integer.class);
                            String c = dataSnapshot.child("category").getValue(String.class);
                            String c1 = dataSnapshot.child("comment").getValue(String.class);
                            String d = dataSnapshot.child("date").getValue(String.class);
                            System.out.println(n+a.toString()+c+c1+d);
                            ExpenseData exp1 = new ExpenseData(n,a,d,c,c1);
                            expenses.add(exp1);
                        }

                        myAdapter.notifyDataSetChanged();

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });



    }

    private void dataInitialize() {

    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Expenses");
    expenses= new ArrayList<>();
    databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {



            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                System.out.println("KEY_________________"+ dataSnapshot.getKey());

                System.out.println("VALUE_______________"+ dataSnapshot.toString());
               // Expense exp = new Expense(dataSnapshot.getKey(), Integer.valueOf(dataSnapshot.getValue().toString()));
               // ExpenseData exp = dataSnapshot.getValue(ExpenseData.class);
               //ExpenseData exp = dataSnapshot.getValue(ExpenseData.class);
               String n = dataSnapshot.child("name").getValue(String.class);
               Integer a = dataSnapshot.child("amount").getValue(Integer.class);
               String c = dataSnapshot.child("category").getValue(String.class);
               String c1 = dataSnapshot.child("comment").getValue(String.class);
               String d = dataSnapshot.child("date").getValue(String.class);
                System.out.println(n+a.toString()+c+c1+d);
               ExpenseData exp1 = new ExpenseData(n,a,d,c,c1);
                expenses.add(exp1);
            }

        }


        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });

    }

    private void updateExpenseItem(){
        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =LayoutInflater.from(getActivity());
        View myView = inflater.inflate(R.layout.update_expense,null);
        System.out.println("inflated view");
        mydialog.setView(myView);
        System.out.println("dialog set view");

        edtname=myView.findViewById(R.id.editname);
        edtamount=myView.findViewById(R.id.editamount);
        edtdate=myView.findViewById(R.id.editdate);
        edtcategory=myView.findViewById(R.id.editcategory);
        edtcomment=myView.findViewById(R.id.editcomment);

        updatebutton=myView.findViewById(R.id.update_button);
        deletebutton=myView.findViewById(R.id.delete_button);

        AlertDialog dialog = mydialog.create();

        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });


        dialog.show();


    }
}