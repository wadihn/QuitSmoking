package com.example.quitsmoking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.quitsmoking.databinding.ActivityDataBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class DataActivity extends AppCompatActivity {
    private ActivityDataBinding dataBinding;
    private int yearr, monthh, dayy, hourr, minutee;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = ActivityDataBinding.inflate(getLayoutInflater());
        setContentView(dataBinding.getRoot());

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Calendar calendar = Calendar.getInstance();
        yearr = calendar.get(Calendar.YEAR);
        monthh = calendar.get(Calendar.MONTH);
        dayy = calendar.get(Calendar.DAY_OF_MONTH);
        hourr = calendar.get(Calendar.HOUR_OF_DAY);
        minutee = calendar.get(Calendar.MINUTE);

        dataBinding.btnDate.setOnClickListener(v -> {
            DatePickerDialog pickerDialog = new DatePickerDialog(DataActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        yearr = year;
                        monthh = month;
                        dayy = dayOfMonth;
                        updateSelectedDate();
                    }
                }, yearr, monthh, dayy);
            pickerDialog.show();
        });

        dataBinding.btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(DataActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            hourr = hourOfDay;
                            minutee = minute;
                            updateSelectedTime();
                        }
                    }, hourr, minutee, false);
                timePickerDialog.show();
            }
        });

        dataBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dataBinding.btnDate.getText().equals("date"))
                    Toast.makeText(DataActivity.this, "Add date", Toast.LENGTH_SHORT).show();

                else if(dataBinding.btnDate.getText().equals("time"))
                    Toast.makeText(DataActivity.this, "add time", Toast.LENGTH_SHORT).show();

                else if(TextUtils.isEmpty(dataBinding.etNumber.getText()))
                    Toast.makeText(DataActivity.this, "add number of cigarette", Toast.LENGTH_SHORT).show();

                else if(TextUtils.isEmpty(dataBinding.etPrice.getText()))
                    Toast.makeText(DataActivity.this, "add average price of cigarette", Toast.LENGTH_SHORT).show();
                else
                    putQuitData();
            }
        });

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
//            startActivity(new Intent(DataActivity.this, MainActivity.class));
//            finish();
//        }
//    }

    private void putQuitData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Data");

        HashMap<String, Object> map = new HashMap<>();
        map.put("date", dataBinding.btnDate.getText().toString());
        map.put("time", dataBinding.btnTime.getText().toString());
        map.put("numOfCigar", dataBinding.etNumber.getText().toString());
        map.put("priceOfCigar", dataBinding.etPrice.getText().toString());

        reference.child(firebaseUser.getUid()).setValue(map)
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(DataActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DataActivity.this, MainActivity.class));
                    }
                    else
                        Toast.makeText(DataActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

    }

    private void updateSelectedTime() {
        dataBinding.btnTime.setText(hourr + ":" + minutee);
    }

    private void updateSelectedDate() {
        dataBinding.btnDate.setText(String.format("%d-%d-%d", dayy, monthh + 1 , yearr));
    }
}