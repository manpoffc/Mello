package com.example.mello;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Analytics#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Analytics extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public ArrayList <GroupUsers> usersArrayList;
    private   ArrayList <PieEntry> data;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private PieChart pieChart;
    private TextView textView;
    private DatabaseReference databaseReference;
    public
    HashMap<String, Integer> values;
    public Analytics() {
        // Required empty public constructor


    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Analytics.
     */

    // TODO: Rename and change types and number of parameters
    public static Analytics newInstance(String param1, String param2) {
        Analytics fragment = new Analytics();
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
        View view= inflater.inflate(R.layout.fragment_analytics, container, false);
        pieChart= (PieChart) view.findViewById(R.id.pieChart);
        textView= (TextView) view.findViewById(R.id.tvchart);
        ArrayList <PieEntry>
                piechartData = new ArrayList<>();

        textView.setText("Herllo");


        System.out.println(usersArrayList);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Expenses");

        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){
                        DataSnapshot snapshot = task.getResult();

                        piechartData.clear();
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){

                            System.out.println(dataSnapshot.getKey().toString());
                            piechartData.add(new PieEntry(Integer.valueOf(dataSnapshot.getValue().toString()), dataSnapshot.getKey().toString()));

                        }

                        System.out.println(piechartData);
                        PieDataSet pieDataSet = new PieDataSet(piechartData,"EXPENSES");
                        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                        pieDataSet.setValueTextColor(Color.BLACK);
                        pieDataSet.setValueTextSize(16f);

                        PieData pieData = new PieData(pieDataSet);
                        pieChart.setData(pieData);
                        pieChart.getDescription().setEnabled(false);
                        pieChart.setCenterText("EXPENSES");


                }

            }}});

        pieChart.animate();
        return view;
    }
}