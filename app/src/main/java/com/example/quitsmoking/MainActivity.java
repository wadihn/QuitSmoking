package com.example.quitsmoking;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.quitsmoking.Fragments.AchievementFragment;
import com.example.quitsmoking.Fragments.HealthFragment;
import com.example.quitsmoking.Fragments.HomeFragment;
import com.example.quitsmoking.Model.QuitData;
import com.example.quitsmoking.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/*
// Log out the user
    FirebaseAuth.getInstance().signOut();

// Redirect to the login screen
    Intent intent = new Intent(CurrentActivity.this, LoginActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clears the back stack
    startActivity(intent);
*/

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;
    private FirebaseUser firebaseUser;
    private Fragment selectorFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        mainBinding.mainBottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.nav_home)
                    selectorFragment = new HomeFragment();
                else if(id == R.id.nav_health)
                    selectorFragment = new HealthFragment();
                else
                    selectorFragment = new AchievementFragment();

                if(selectorFragment != null)
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectorFragment).commit();

                return true;
            }
        });

        mainBinding.ivSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DataActivity.class));
            }
        });


//        getQuitData();

    }

    private void getQuitData() {
        FirebaseDatabase.getInstance().getReference().child("Data").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
//                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        QuitData quitData = snapshot.getValue(QuitData.class);
                        if (quitData != null) {
//                            mainBinding.textView3.setText(quitData.getDate());
//                            mainBinding.textView5.setText(quitData.getNumOfCigar());
//                            mainBinding.textView4.setText(quitData.getTime());
//                            mainBinding.textView6.setText(quitData.getPriceOfCigar());
                        }
//                    }
                } else {
                    Toast.makeText(MainActivity.this, "doesn't exist", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}