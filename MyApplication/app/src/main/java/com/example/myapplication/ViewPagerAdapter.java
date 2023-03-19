package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    private List<UserReminder> mData;
    private LayoutInflater mInflater;
    private ViewPager2 viewPager2;


    ViewPagerAdapter(Context context, List<UserReminder> data, ViewPager2 viewPager2) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.viewPager2 = viewPager2;
        for(int i =0;i<mData.size();i++){
            Log.d("A",mData.get(i).getDate());
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.reminder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserReminder reminder = mData.get(position);
        holder.textViewDate.setText(mData.get(position).getDate());
        holder.textViewTitle.setText(mData.get(position).getTitle());
        holder.textViewNotes.setText(mData.get(position).getNotes());
        holder.linearLayout.setBackgroundResource(android.R.color.white);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        private TextView textViewDate;
        private TextView textViewTitle;
        private TextView textViewNotes;

        ViewHolder(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.container);
            textViewDate = itemView.findViewById(R.id.reminderDate);
            textViewTitle = itemView.findViewById(R.id.reminderTitle);
            textViewNotes = itemView.findViewById(R.id.reminderNotes);
        }
    }

}
