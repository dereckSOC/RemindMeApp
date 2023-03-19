package com.example.remindme;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Activity_ForgotPwd extends AppCompatActivity {

    EditText mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pwd);

        mEmail = findViewById(R.id.txtResetPassEmail);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnResetPassSendLink:
                forgetPwd();
                break;
        }
    }

    public void forgetPwd() {
        String email = mEmail.getText().toString();

        // Check if user has entered email
        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Email is required.");
            return;
        }

        //Check if user has entered a valid email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Please provide a valid email.");
        }

        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(Activity_ForgotPwd.this, "Link has been sent to your registered email.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(Activity_ForgotPwd.this, "Error", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
