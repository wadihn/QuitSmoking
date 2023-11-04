package com.example.quitsmoking.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.quitsmoking.Model.QuitData;
import com.example.quitsmoking.R;
import com.example.quitsmoking.databinding.FragmentAchievementBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AchievementFragment extends Fragment {

    private FragmentAchievementBinding achievementBinding;
    private FirebaseUser firebaseUser;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AchievementFragment() {
        // Required empty public constructor
    }

    public static AchievementFragment newInstance(String param1, String param2) {
        AchievementFragment fragment = new AchievementFragment();
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
        achievementBinding = FragmentAchievementBinding.inflate(getLayoutInflater(), container, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        getQuitData();
        return achievementBinding.getRoot();
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

            if (daysDifference == 1)
                achievementBinding.imageView.setBackgroundResource(R.drawable.achievement);
            else if (daysDifference == 2)
                achievementBinding.imageView.setImageResource(R.drawable.achievement);
            else if (daysDifference == 3)
                achievementBinding.imageView.setBackgroundResource(R.drawable.achievement);
            else
                achievementBinding.imageView.setBackgroundResource(R.drawable.achievement);

        }

        catch (ParseException e) {
            e.printStackTrace();
            // Handle the parse error appropriately
        }
    }

}