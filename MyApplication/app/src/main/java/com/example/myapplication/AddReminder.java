package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class AddReminder extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences prefs;
    private LinearLayout savebtn;
    private EditText titleinput, notesinput;
    //private DatabaseReference mDatabaseReference;
    private static final String TAG = "Reminder";
    public static final String reminderPref = "reminderPref";

    DatabaseReference reff;
    UserReminder reminder;

    private LinearLayout mDisplayDate;
    private TextView mDisplayDateText;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    long value = 0;

    String reminderTitle;
    String reminderNotes;
    String reminderDate;
    String reminderLocation;
    ImageButton btnAddParticipant;

    private TextView locationText;
    private TextView participantText;
    public static final String prefTitle = "titleKey";
    public static final String prefNotes = "notesKey";
    public static final String prefDate = "dateKey";
    public static final String prefLocation = "locationKey";
    public static final String prefParticipant = "participantKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = getSharedPreferences(reminderPref, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addreminder);

        mDisplayDateText = (TextView) findViewById(R.id.datepickertext);
        titleinput = (EditText) findViewById(R.id.title);
        notesinput = (EditText) findViewById(R.id.notes);
        locationText = (TextView) findViewById(R.id.locationtext);
        participantText = (TextView) findViewById(R.id.txtShareParticipants);
        btnAddParticipant = (ImageButton) findViewById(R.id.btnAddParticipant);


        prefs = getSharedPreferences(reminderPref, MODE_PRIVATE);
        String title = prefs.getString(prefTitle, "");
        Log.d("title", title);
        String notes = prefs.getString(prefNotes, "");
        Log.d("notes", notes);
        String date = prefs.getString(prefDate, "");
        Log.d("date", date);
        String participant = prefs.getString(prefParticipant, "");
        Log.d("participant", participant);
        Intent i=getIntent();
        String intentlocation=i.getStringExtra("location");
        String intentUsername = i.getStringExtra("username");
        String intentuserid = i.getStringExtra("userid");
        String location = prefs.getString(prefLocation, "");
        Log.d("location", location);
        if (!title.equals("")) {
            titleinput.setText(title);
        }
        if (!notes.equals("")) {
            notesinput.setText(notes);
        }
        if (!date.equals("")) {
            mDisplayDateText.setText(date);
        }
        if (!location.equals("")) {
            if(TextUtils.isEmpty(intentlocation)) {
                locationText.setText(location);
            }else {
                locationText.setText(intentlocation);
            }
        }
        if (!participant.equals("")) {
            if(TextUtils.isEmpty(intentUsername)) {
                participantText.setText(participant);
            }else {
                participantText.setText(intentUsername);
                btnAddParticipant.setVisibility(View.GONE);
            }
        }




        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Users");


        mDisplayDate = (LinearLayout) findViewById(R.id.datepickerbtn);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal =Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddReminder.this,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String newDay = ""+dayOfMonth;
                String newMonth = ""+(month+1);
                if(dayOfMonth < 10) {
                    newDay="0"+dayOfMonth;
                };
                if(month < 10) {
                    newMonth="0"+(month+1);
                };
                String date = newDay + "/" + newMonth + "/" + year;
                Log.d(TAG, "onDateSet: dd/mm/yyyy: " + date);

//                String date = dayOfMonth + "/" + month + "/" + year;
                mDisplayDateText.setText(date.toString());
            }
        };

        savebtn = (LinearLayout)findViewById(R.id.savebtn);
        ImageButton addparticipantbtn = (ImageButton)findViewById(R.id.btnAddParticipant);
        //mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        reminder = new UserReminder();
        reff = FirebaseDatabase.getInstance().getReference("Reminders");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    value=(dataSnapshot.getChildrenCount());
                    if (dataSnapshot.hasChild("reminder"+(value+1))) {
                        value++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        reff = FirebaseDatabase.getInstance().getReference().child("reminders");
//        int reminderID = 0;
//        reff.child("reminder "+ reminderID++);


        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reminderTitle = titleinput.getText().toString().trim();
                reminderNotes = notesinput.getText().toString().trim();
                reminderDate = mDisplayDateText.getText().toString().trim();
                reminderLocation = locationText.getText().toString().trim();
//                reminder.setTitle(titleinput.getText().toString().trim());
//                reminder.setNotes(notesinput.getText().toString().trim());
//                reminder.setDate(mDisplayDateText.getText().toString().trim());
                reff.child("reminder"+(value+1)).child("title").setValue(reminderTitle);
                reff.child("reminder"+(value+1)).child("notes").setValue(reminderNotes);
                reff.child("reminder"+(value+1)).child("date").setValue(reminderDate);
                reff.child("reminder"+(value+1)).child("location").setValue(reminderLocation);
                reff.child("reminder"+(value+1)).child("userid").setValue(userid);
                reff.child("reminder"+(value+1)).child("participantuserid").setValue(intentuserid);
                //reff.push().setValue(reminder);
                Toast.makeText(AddReminder.this, "Saved Reminder", Toast.LENGTH_LONG).show();
                startActivity(new Intent(AddReminder.this, HomeActivity.class));            }
        });

        addparticipantbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddReminder.this, AccountViewActivity.class));            }
        });
    }

    @Override
    public void onPause(){
        Log.d(msg, "The onPause() event");
        super.onPause();

        mDisplayDateText = (TextView) findViewById(R.id.datepickertext);
        titleinput = (EditText) findViewById(R.id.title);
        notesinput = (EditText) findViewById(R.id.notes);
        locationText = (TextView) findViewById(R.id.locationtext);
        participantText = (TextView) findViewById(R.id.txtShareParticipants);

        prefs = getSharedPreferences(reminderPref, MODE_PRIVATE);
        String title = titleinput.getText().toString();
        String notes = notesinput.getText().toString();
        String date = mDisplayDateText.getText().toString();
        String location = locationText.getText().toString();
        String participant = participantText.getText().toString();

        SharedPreferences.Editor editor = prefs.edit();

        // Store into the SharedPreferences
        editor.putString(prefTitle,title);
        editor.putString(prefNotes,notes);
        editor.putString(prefDate,date);
        editor.putString(prefLocation,location);
        editor.putString(prefParticipant,participant);
        editor.commit();


    }


    String msg = "Android : ";

    // Add the following code

    /** Called when the activity is about to become visible. */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(msg, "The onStart() event");
    }

    /** Called when the activity has become visible. */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(msg, "The onResume() event");
    }



    /** Called just before the activity is destroyed. */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(msg, "The onDestroy() event");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mapbtn:
                Intent i = new Intent(this, MapsActivity.class);
                startActivity(i);
                break;
//            case R.id.calendarbtn:
//                Intent j = new Intent(this, Calendar.class);
//                startActivity(j);
//                break;
//            case R.id.savebtn:
//                //UserReminders userReminders = new UserReminders(title.getText().toString(), notes.getText().toString());
//                //mDatabaseReference.child("reminders").push().setValue(userReminders);
//                basicReadWrite();
//                Intent k = new Intent(this, HomeScreen.class);
//                startActivity(k);
//                break;
        }

    }


    // End of Adding the code
}

