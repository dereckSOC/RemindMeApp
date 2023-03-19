package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Setting extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logoutbtn:
                try {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(this, "User Sign out!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                    break;
                }catch (Exception e) {
                    Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
                }
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
}
