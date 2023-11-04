package com.example.quitsmoking.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.quitsmoking.Model.QuitData;
import com.example.quitsmoking.databinding.FragmentHomeBinding;
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

public class HomeFragment extends Fragment {
    private FragmentHomeBinding homeBinding;
    private FirebaseUser firebaseUser;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        homeBinding = FragmentHomeBinding.inflate(getLayoutInflater(), container, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        getQuitData();



        return homeBinding.getRoot();
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
                        String money = quitData.getPriceOfCigar();
                        String numOfCigar = quitData.getNumOfCigar();
                        calculateDaysAndTimeDifference(firebaseDate, firebaseTime, money, numOfCigar);
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

    private void calculateDaysAndTimeDifference(String date, String time, String money, String numOfCigar) {
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

            // Calculate time difference (in milliseconds)
            long timeDifferenceInMillis = differenceInMillis % TimeUnit.DAYS.toMillis(1);
            long hoursDifference = TimeUnit.MILLISECONDS.toHours(timeDifferenceInMillis);
            long minutesDifference = TimeUnit.MILLISECONDS.toMinutes(timeDifferenceInMillis % TimeUnit.HOURS.toMillis(1));
            long secondsDifference = TimeUnit.MILLISECONDS.toSeconds(timeDifferenceInMillis % TimeUnit.MINUTES.toMillis(1));

            // Format the date and time separately
            SimpleDateFormat dateOnlyFormat = new SimpleDateFormat("dd-MM-yyyy");
            SimpleDateFormat timeOnlyFormat = new SimpleDateFormat("HH:mm");

            String formattedDate = dateOnlyFormat.format(firebaseDateTime);
            String formattedTime = timeOnlyFormat.format(firebaseDateTime);

            // Set the date and time in their respective TextViews
            homeBinding.tvDays.setText(String.valueOf(daysDifference));
            homeBinding.tvHours.setText(String.valueOf(hoursDifference));
            int num = Integer.parseInt(String.valueOf(numOfCigar));
            int day = Integer.parseInt(String.valueOf(daysDifference));
            double mon = Double.parseDouble(String.valueOf(money));
            homeBinding.tvNotSmoked.setText(String.valueOf(num * day));
            homeBinding.tvMoney.setText(String.valueOf(mon*num*day));
        }

        catch (ParseException e) {
            e.printStackTrace();
            // Handle the parse error appropriately
        }
    }
}