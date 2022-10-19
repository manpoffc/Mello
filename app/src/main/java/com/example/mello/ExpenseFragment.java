package com.example.mello;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private ArrayList<Expense> expenses;
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
        MyAdapter myAdapter = new MyAdapter(getContext(),expenses);
        recyclerView.setAdapter(myAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Expenses");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    System.out.println("KEY_________________"+ dataSnapshot.getKey());

                    System.out.println("VALUE_______________"+ dataSnapshot.toString());
                    Expense exp = new Expense(dataSnapshot.getKey(), Integer.valueOf(dataSnapshot.getValue().toString()));
                    expenses.add(exp);
                }

                myAdapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                Expense exp = new Expense(dataSnapshot.getKey(), Integer.valueOf(dataSnapshot.getValue().toString()));
                expenses.add(exp);
            }

        }


        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });

    }
}