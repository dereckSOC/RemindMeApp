package com.example.myapplication;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.os.Debug.waitForDebugger;


public class Activity_CreateAccount extends AppCompatActivity implements View.OnClickListener {
    EditText mUsername, mPassword, mPasswordCfm, mEmail, mPhone;
    Button mRegisterBtn;
    FirebaseAuth mAuth;
    ImageView profileImg;
    Button profilePicUpload;
    Uri imageURI;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mUsername = findViewById(R.id.txtRegisterUsername);
        mPassword = findViewById(R.id.txtRegisterPassword);
        mPasswordCfm = findViewById(R.id.txtRegisterConfirmPassword);
        mEmail = findViewById(R.id.txtRegisterEmail);
        mPhone = findViewById(R.id.txtRegisterPhone);
        mRegisterBtn = findViewById(R.id.btnSignUp);
        mAuth = FirebaseAuth.getInstance();
        profileImg = findViewById(R.id.profilePic);
        profilePicUpload = findViewById(R.id.editProfilePic);
        storageReference = FirebaseStorage.getInstance().getReference();

        profilePicUpload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 1000);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000) {
            if(resultCode == Activity.RESULT_OK) {
                imageURI = data.getData();
                profileImg.setImageURI(imageURI);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageURI, String uid) {
        StorageReference fileRef = storageReference.child(uid);
        fileRef.putFile(imageURI);
//.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                fileRef.getDownloadUrl()
//                Toast.makeText(Activity_CreateAccount.this, "Image successfully uploaded", Toast.LENGTH_LONG).show();
//            }
// });
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignUp:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        final String email = mEmail.getText().toString();
        final String username = mUsername.getText().toString();
        final String password = mPassword.getText().toString();
        String password2 = mPasswordCfm.getText().toString();
        final String phone = mPhone.getText().toString();

        //Validation
        //Check if user has entered username
        if (TextUtils.isEmpty(username)) {
            mUsername.setError("Username is required.");
            return;
        }

        //Check if user has entered password
        if (TextUtils.isEmpty(password)) {
            mPassword.setError("Password is required.");
            return;
        }

        //Check if password length is min. 6
        if (password.length() < 6) {
            mPassword.setError("Password has to be more than 5 characters long!");
        }

        //Check if user has re-entered password
        if (TextUtils.isEmpty(password2)) {
            mPasswordCfm.setError("Please re-enter password.");
            return;
        }


        //Check if user has re-entered the correct password
        if (!password.equals(password2)) {
            mPasswordCfm.setError("Passwords do not match!");
            return;
        }

        // Check if user has entered email
        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Email is required.");
            return;
        }

        //Check if user has entered a valid email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError("Please provide a valid email.");
        }

        //Check if user has entered phone number
        if (TextUtils.isEmpty(phone)) {
            mPhone.setError("Phone number is required.");
        }

        //Check if user has entered a valid phone number with 8 characters
        if (phone.length() != 8) {
            mPhone.setError("Phone number is required.");
        }

        //Firebase authentication
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
//                            UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
//                                    .setPhotoUri(imageURI)
//                                    .build();
//                            FirebaseAuth.getInstance().getCurrentUser().updateProfile(userProfileChangeRequest);
                            uploadImageToFirebase(imageURI, mAuth.getCurrentUser().getUid());


                            Toast.makeText(Activity_CreateAccount.this, "User has been registered", Toast.LENGTH_LONG).show();
                            Account user = new Account(username, phone);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(Activity_CreateAccount.this, "User has been registered", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(Activity_CreateAccount.this, "Failed to insert to RealTime DB", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            task.getException().printStackTrace();
                            Toast.makeText(Activity_CreateAccount.this, "Failed to register", Toast.LENGTH_LONG).show();
                        }
                    }

                });

    }
}



