package com.example.remindme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private TextView register;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Intent i = new Intent(this, Activity_Login.class);
//        startActivity(i);
        setContentView(R.layout.activity_main);

        try {
            Toast.makeText(MainActivity.this, FirebaseAuth.getInstance().getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();
        } catch(Exception e) {
            Toast.makeText(MainActivity.this, "not logged in", Toast.LENGTH_LONG).show();
        }

    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnloginhome:
                startActivity(new Intent(MainActivity.this, Activity_Login.class));
                break;
            case R.id.btnLogout:
                try {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(this, "User Sign out!", Toast.LENGTH_SHORT).show();
                }catch (Exception e) {
                    Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btnEditPro:
                startActivity(new Intent(MainActivity.this, Activity_EditProfile.class));
                break;
        }
    }
}
