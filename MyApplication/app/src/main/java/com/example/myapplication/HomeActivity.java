package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;
import java.util.Calendar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private Context context = this;
    ViewPager2 viewPager2;
    ImageButton btnNext;
    ImageButton btnPrev;
    ImageButton btnCalendar;
    ArrayList<String> dateList = new ArrayList<>();
    ArrayList<String> titleList = new ArrayList<>();
    ArrayList<String> notesList = new ArrayList<>();
    List<UserReminder> list = new ArrayList<>();
    private SharedPreferences prefs;
    public static final String reminderPref = "reminderPref";

    private int getItem(int i) {
        return viewPager2.getCurrentItem() + i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.txtReminders);

        prefs = getSharedPreferences(reminderPref, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();


        String currentuserid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Reminders");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot reminderData) {
                long count = reminderData.getChildrenCount();
                for (int z = 0; z < count; z++) {
                    try {
                        DataSnapshot reminder = reminderData.child("reminder" + (z + 1));
                        String userid = reminder.child("userid").getValue().toString();
                        String participantuserid = "";
                        if(reminder.hasChild("participantuserid")) {
                            participantuserid = reminder.child("participantuserid").getValue().toString();
                        }
                        if(currentuserid.equals(userid)) {
                            String date = reminder.child("date").getValue().toString();
                            String title = reminder.child("title").getValue().toString();
                            String notes = reminder.child("notes").getValue().toString();
                            list.add(new UserReminder(date, title, notes));
                            dateList.add(date);
                            titleList.add(title);
                            notesList.add(notes);
                        }
                        else if(currentuserid.equals(participantuserid)) {
                            String date = reminder.child("date").getValue().toString();
                            String title = reminder.child("title").getValue().toString();
                            String notes = reminder.child("notes").getValue().toString();
                            list.add(new UserReminder(date, title, notes));
                            dateList.add(date);
                            titleList.add(title);
                            notesList.add(notes);
                        }
                        else {}
                    } catch (Exception e) {
                        count++;
                    }
                }
                reff.removeEventListener(this);
                viewPager2.setAdapter(new ViewPagerAdapter(context, list, viewPager2));
                Log.d("dateList", dateList.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        TextView textViewDate = findViewById(R.id.txtDate);
        textViewDate.setText(currentDate);
    }
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.btnNext:
                viewPager2.setCurrentItem(getItem(+1), true);
                break;
            case R.id.btnPrevious:
                viewPager2.setCurrentItem(getItem(-1), true);
                break;
            case R.id.btnViewCalendar:
                i = new Intent(this, CalendarActivity.class);
                i.putStringArrayListExtra("dateList", dateList);
                i.putStringArrayListExtra("titleList", titleList);
                i.putStringArrayListExtra("notesList", notesList);
                startActivity(i);
                break;
            case R.id.btnViewProfile:
                i = new Intent(this, Activity_EditProfile.class);
                startActivity(i);
                break;
            case R.id.btnViewSettings:
                i = new Intent(this, Setting.class);
                startActivity(i);
                break;
        }
    }

}