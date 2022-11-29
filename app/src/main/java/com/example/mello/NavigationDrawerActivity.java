package com.example.mello;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

//import com.github.mikephil.charting.data.PieEntry;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NavigationDrawerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation_drawer);
        //setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                item.setChecked(true);


                drawerLayout.closeDrawer(GravityCompat.START);

                switch (id){

                    case R.id.navigation_dashboard:

                        replaceFragment(new DashboardFragment());
                        break;
                    case R.id.navigation_expense:

                        replaceFragment(new ExpenseFragment());
                        break;

                    case R.id.navigation_income:

                        replaceFragment(new IncomeFragment());
                        break;
                    case R.id.navigation_logOut:

                        Toast.makeText(NavigationDrawerActivity.this, "Logout",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_analytics:

                        replaceFragment(new Analytics());
                        break;
                    case R.id.navigation_group_expense:

                        replaceFragment(new GroupFragment());
                        break;
                    case R.id.add_expense:
                        replaceFragment(new InsertExpenseFragment());
                        break;
                    default:
                        replaceFragment(new DashboardFragment());

                }

                return true;
            }
        });

    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);

        fragmentTransaction.commit();



    }

}