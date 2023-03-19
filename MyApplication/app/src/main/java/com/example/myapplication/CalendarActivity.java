package com.example.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {
    List<EventDay> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        Intent intent = getIntent();
        ArrayList<String> dateList = intent.getStringArrayListExtra("dateList");
        ArrayList<String> titleList = intent.getStringArrayListExtra("titleList");
        ArrayList<String> notesList = intent.getStringArrayListExtra("notesList");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for(int i = 0; i < dateList.size(); i++) {
            String date = dateList.get(i);
            Date dateObj = null;
            try {
                dateObj = sdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateObj);
            events.add(new EventDay(cal, R.drawable.greycircle));
        }
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setEvents(events);

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                Date date = clickedDayCalendar.getTime();
                String strDate = sdf.format(date);
                Log.d("strDate", strDate);
                Log.d("1stDate", dateList.get(0));
                Log.d("2ndDate", dateList.get(1));
                for (int i = 0; i < dateList.size(); i++) {
                    if(strDate.equals(dateList.get(i))) {
                        Toast.makeText(getApplicationContext(), "Date: " + dateList.get(i) + "\tTitle: " + titleList.get(i) + "\tNotes: " + notesList.get(i), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.btnCalendarNewReminder:
                i = new Intent(this, AddReminder.class);
                startActivity(i);
                break;
            case R.id.btnConfirm2:
                i = new Intent(this, HomeActivity.class);
                startActivity(i);
                break;
        }
    }
}

