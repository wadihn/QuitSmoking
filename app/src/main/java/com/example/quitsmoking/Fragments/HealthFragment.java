package com.example.quitsmoking.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.quitsmoking.Model.QuitData;
import com.example.quitsmoking.databinding.FragmentHealthBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class HealthFragment extends Fragment {
    private FragmentHealthBinding healthBinding;
    private FirebaseUser firebaseUser;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public HealthFragment() {
        // Required empty public constructor
    }

    public static HealthFragment newInstance(String param1, String param2) {
        HealthFragment fragment = new HealthFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        healthBinding = FragmentHealthBinding.inflate(getLayoutInflater(), container,false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        getQuitData();
        return healthBinding.getRoot();
    }

    private void getQuitData() {
        FirebaseDatabase.getInstance().getReference().child("Data").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    QuitData quitData = snapshot.getValue(QuitData.class);
                    if (quitData != null) {
                        String firebaseDate = quitData.getDate();
                        String firebaseTime = quitData.getTime();
                        calculateDaysAndTimeDifference(firebaseDate, firebaseTime);
                    }

                } else {
                    Toast.makeText(getContext(), "doesn't exist", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void calculateDaysAndTimeDifference(String date, String time) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

            // Combine date and time strings into a single date-time string
            String firebaseDateTimeString = date + " " + time;

            Date firebaseDateTime = dateFormat.parse(firebaseDateTimeString);

            // Get current date and time
            Date currentDateTime = new Date();

            // Calculate the difference in milliseconds
            long differenceInMillis = currentDateTime.getTime() - firebaseDateTime.getTime();

            // Calculate days difference
            long daysDifference = TimeUnit.DAYS.convert(differenceInMillis, TimeUnit.MILLISECONDS);

            // Set the date and time in their respective TextViews
            if(daysDifference == 0)
                healthBinding.progressBar.setProgress(0);
            else if (daysDifference == 1)
                healthBinding.progressBar.setProgress(25);
            else if (daysDifference == 2)
                healthBinding.progressBar.setProgress(50);
            else if (daysDifference == 3)
                healthBinding.progressBar.setProgress(75);
            else
                healthBinding.progressBar.setProgress(100);

        }

        catch (ParseException e) {
            e.printStackTrace();
            // Handle the parse error appropriately
        }
    }


}