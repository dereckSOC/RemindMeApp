package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AccountArrayAdapter extends RecyclerView.Adapter<AccountArrayAdapter.MyViewHolder> {

    private ArrayList<Account> mAccounts;
    private MyRecyclerViewItemClickListener mItemClickListener;

    public AccountArrayAdapter(ArrayList<Account> accounts, MyRecyclerViewItemClickListener itemClickListener) {
        this.mAccounts = accounts;
        this.mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_accountitem, parent, false);

        //Create View Holder
        final MyViewHolder myViewHolder = new MyViewHolder(view);

        //Item Clicks
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClicked(mAccounts.get(myViewHolder.getLayoutPosition()));
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewUsername.setText(mAccounts.get(position).getUsername());
        holder.textViewPhone.setText(mAccounts.get(position).getPhone());

    }

    @Override
    public int getItemCount() {
        return mAccounts.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //RecyclerView View Holder
    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewUsername;
        private TextView textViewPhone;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUsername = itemView.findViewById(R.id.accountUsername);
            textViewPhone = itemView.findViewById(R.id.accountPhone);
        }
    }

    //RecyclerView Click Listener
    public interface MyRecyclerViewItemClickListener {
        void onItemClicked(Account account);
    }
}
