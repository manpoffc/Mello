package com.example.mello;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private FloatingActionButton sortbutton;

    private FloatingActionButton filterButton;
    private RadioButton foodbutton,fuelbutton,entertainmentbutton,carbutton,clothesbtn,servicebtn,giftsbtn,billsbtn,educationbtn,liquorbtn,rentbtn,othersbtn;
    private String selectedFilter;

   // private ArrayList<Expense> expenses;
    private  ArrayList<ExpenseData> expenses;
    private  ArrayList<ExpenseData> sortexpenses;

    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;

    //private SearchView searchView;
    private SearchView searchView;
    Toolbar toolbar;
    public ExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
       // super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.filter_menu,menu);
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

                expenses.clear();


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

        onSearchExpense();

        sortbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortExpenses(myAdapter);
            }
        });

        filterButton=view.findViewById(R.id.filterbutton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                filterMethod();

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

    private void sortExpenses(MyAdapter myAdapter){
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

    private void onSearchExpense(){
        searchView = (SearchView) getView().findViewById(R.id.search_expense);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("inside query listener"+newText);
                ArrayList<ExpenseData> expenseDataList =new ArrayList<ExpenseData>();

                for(ExpenseData expenseObj :expenses){

                    if(expenseObj.getName().toLowerCase().contains(newText.toLowerCase())||expenseObj.getCategory().toLowerCase().contains(newText.toLowerCase())||expenseObj.getComment().toLowerCase().contains(newText.toLowerCase())||expenseObj.getAmount().toString().contains(newText.toLowerCase())){

                        expenseDataList.add(expenseObj);
                    }
                }
                System.out.println("arraylist items"+expenseDataList.toString());
                MyAdapter searchAdapter = new MyAdapter(getContext(),expenseDataList);
                recyclerView.setAdapter(searchAdapter);
                searchAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    public  void filterList(String status){
        System.out.println("inside filterlist method");

        selectedFilter=status;
        ArrayList<ExpenseData> filteredExpenseList =new ArrayList<ExpenseData>();

        for(ExpenseData expenseObj :expenses){
            if(expenseObj.getCategory().toLowerCase().contains(status)){
                filteredExpenseList.add(expenseObj);
            }
        }
        MyAdapter searchAdapter = new MyAdapter(getContext(),filteredExpenseList);
        recyclerView.setAdapter(searchAdapter);
        searchAdapter.notifyDataSetChanged();

    }

    public void filterMethod(){
        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =LayoutInflater.from(getActivity());
        View myView = inflater.inflate(R.layout.filter_options,null);
        mydialog.setView(myView);

        AlertDialog dialog = mydialog.create();

        foodbutton= myView.findViewById(R.id.food_radio);
        carbutton=myView.findViewById(R.id.car_radio);
        fuelbutton=myView.findViewById(R.id.fuel_radio);
        entertainmentbutton=myView.findViewById(R.id.entertainment_radio);
        clothesbtn=myView.findViewById(R.id.clothes_radio);
        servicebtn=myView.findViewById(R.id.services_radio);
        giftsbtn=myView.findViewById(R.id.gifts_radio);
        billsbtn=myView.findViewById(R.id.Bills_radio);
        educationbtn=myView.findViewById(R.id.education_radio);
        liquorbtn=myView.findViewById(R.id.liquor_radio);
        rentbtn=myView.findViewById(R.id.rent_radio);
        othersbtn=myView.findViewById(R.id.rent_radio);

        foodbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterList("food");
            }
        });

        fuelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterList("fuel");

            }
        });

        carbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterList("car");

            }
        });

        entertainmentbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterList("entertainment");

            }
        });

        clothesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterList("clothes");
            }
        });

        servicebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterList("services");
            }
        });

        giftsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterList("gifts");
            }
        });

        billsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterList("bill");
            }
        });

        educationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterList("education");
            }
        });

        liquorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterList("liquor");
            }
        });

        rentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterList("rent");
            }
        });

        othersbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterList("others");
            }
        });
        dialog.show();


    }
}