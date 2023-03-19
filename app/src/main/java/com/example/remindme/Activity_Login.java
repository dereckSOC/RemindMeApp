package com.example.remindme;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Activity_Login extends AppCompatActivity implements View.OnClickListener {

    Intent i;
    FirebaseAuth mAuth;
    EditText mEmail, mPassword;
    public static String pubPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail = findViewById(R.id.txtloginEmail);
        mPassword = findViewById(R.id.txtloginPassword);
    }

    //[Code to validate user account and login]

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                i = new Intent(this, Activity_CreateAccount.class);
                startActivity(i);
                break;

            case R.id.btnForgetPass:
                i = new Intent(this, Activity_ForgotPwd.class);
                startActivity(i);
                break;

            case R.id.btnLogin:
                userLogin();
                break;
        }
    }

    private void userLogin() {
        String email = mEmail.getText().toString();
        final String password = mPassword.getText().toString();

        //Validation
        if(email.isEmpty()) {
            mEmail.setError("Email is required!");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Please provide a valid email.");
        }

        if(password.isEmpty()) {
            mPassword.setError("Password is required!");
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        //change mainactivity.class to homepage
                        //Account loginUser = new Account()
                        pubPass = password;
                        startActivity(new Intent(Activity_Login.this, MainActivity.class));
                    } else {
                        Toast.makeText(Activity_Login.this, "Failed to login", Toast.LENGTH_LONG).show();
                    }
                }
        });
    }

}
