package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Map;

import static com.example.myapplication.MainActivity.pubPass;

public class Activity_EditProfile extends AppCompatActivity {
    private static final String TAG = "Activity_EditProfile";
    private String CurrentUserID;
    String displayUsername, displayPhone, holderUsername, holderPhone;
    private DatabaseReference databaseReference;
    EditText mUsername, mPassword, mEmail, mPhone;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    String pwd = MainActivity.pubPass;

    Uri profilePicUri;
    ImageView profilePic;
    Button picUpload;
    StorageReference storageReference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mUsername = findViewById(R.id.txtproEditUsername);
        mPassword = findViewById(R.id.txtprofEditPassword);
        mEmail = findViewById(R.id.txtprofEditEmail);
        mPhone = findViewById(R.id.txtprofEditPhone);
        profilePic = findViewById(R.id.profilePicEdit);
        picUpload = findViewById(R.id.uploadProfilePic);
        storageReference = FirebaseStorage.getInstance().getReference();

        final EditText editUsername = (EditText) findViewById(R.id.txtproEditUsername);
        final EditText editPassword = (EditText) findViewById(R.id.txtprofEditPassword);
        final EditText editEmail = (EditText) findViewById(R.id.txtprofEditEmail);
        final EditText editPhone = (EditText) findViewById(R.id.txtprofEditPhone);

        if (user != null) {
            Log.d(TAG, "Msg: " + user.getEmail());
            CurrentUserID = user.getUid();
        }

        //Retrieve username & phone from firebase
        databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Users").child(CurrentUserID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                displayUsername = dataSnapshot.child("username").getValue().toString();
                displayPhone = dataSnapshot.child("phone").getValue().toString();
                holderUsername = dataSnapshot.child("username").getValue().toString();
                holderPhone = dataSnapshot.child("phone").getValue().toString();
                //Display text
                editEmail.setText(user.getEmail());
                editUsername.setText(displayUsername);
                editPhone.setText(displayPhone);
                editPassword.setText(pubPass);
                StorageReference a = storageReference.child(user.getUid());
                a.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profilePic);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //upload image
        picUpload.setOnClickListener(new View.OnClickListener() {
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
                profilePicUri = data.getData();
                profilePic.setImageURI(profilePicUri);

                //uploadImageToFirebase(imageURI);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageURI, String uid) {
        StorageReference fileRef = storageReference.child(uid);
        fileRef.putFile(imageURI);
    }

    /*
     * Retrieve account object from db
     * Display on editpage
     * Make changes
     *
     * */

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnConfirm1:
                updateProfile();
                break;
        }
    }

    private void updateProfile() {
        String username = mUsername.getText().toString();
        final String password = mPassword.getText().toString();
        final String email = mEmail.getText().toString();
        String phone = mPhone.getText().toString();

        // Get auth credentials from the user for re-authentication
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), pubPass); // Current Login Credentials \\
        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "User re-authenticated.");
                        //----------------Code for Changing Email Address----------\\
                        if (!user.getEmail().equals(email)) {
                            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                mEmail.setError("Please provide a valid email.");
                            } else {
                                user.updateEmail(email)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "User email address updated.");
                                                    Toast.makeText(Activity_EditProfile.this, "Email Address has been updated!", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            }
                        }

                        if (!pubPass.equals(password)) {
                            if (password.length() < 6 ) {
                                mPassword.setError("Password has to be more than 5 characters long!");
                            } else {
                                user.updatePassword(password)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d(TAG, "User password updated.");
                                                    Toast.makeText(Activity_EditProfile.this, "Password has been updated!", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            }
                        }

                        try {
                            uploadImageToFirebase(profilePicUri, mAuth.getCurrentUser().getUid());
                            Toast.makeText(Activity_EditProfile.this, "Profile has been updated", Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.d("err", "err" + profilePicUri);
                        }
                    }
                });

        if (!username.equals(holderUsername) || !phone.equals(holderPhone)) {

            if (phone.length() < 8) {
                Toast.makeText(Activity_EditProfile.this, "Phone number must be more than 8 digits!", Toast.LENGTH_LONG).show();
            } else {
                Account updatedAcc = new Account(username, phone);
                databaseReference.setValue(updatedAcc);
                Toast.makeText(Activity_EditProfile.this, "Profile updated successfully!", Toast.LENGTH_LONG).show();
            }
        }



    }


}