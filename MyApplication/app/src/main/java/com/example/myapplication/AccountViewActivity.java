package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.MainActivity.pubPass;

public class AccountViewActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private ArrayList<Account> mAccounts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountview);
        bindAccountData();
        setUIRef();
    }

    private void setUIRef() {
        //Reference of RecyclerView
        mRecyclerView = findViewById(R.id.accountList);
        //Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AccountViewActivity.this, RecyclerView.VERTICAL, false);
        //Set Layout Manager to RecyclerView
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void bindAccountData() {
        ArrayList<Account> mAccounts = new ArrayList<>();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().getRoot().child("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String userid = dsp.getKey();
                    String username = dsp.child("username").getValue().toString();
                    Log.d("username", username);
                    String phone = dsp.child("phone").getValue().toString();
                    Log.d("phone", phone);
                    mAccounts.add(new Account(username, phone, userid));
                }
                AccountArrayAdapter myRecyclerViewAdapter = new AccountArrayAdapter(mAccounts, new AccountArrayAdapter.MyRecyclerViewItemClickListener() {
                    //Handling clicks
                    @Override
                    public void onItemClicked(Account Account) {
                        String userid = Account.getUserid();
                        String username = Account.getUsername();
                        Intent i = new Intent(AccountViewActivity.this, AddReminder.class);
                        i.putExtra("username", username);
                        i.putExtra("userid", userid);
                        startActivity(i);
                    }
                });
                //Set adapter to RecyclerView
                mRecyclerView.setAdapter(myRecyclerViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
