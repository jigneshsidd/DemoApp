package com.example.testnimap.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.testnimap.R;
import com.example.testnimap.models.Record;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestNimapAdapter extends RecyclerView.Adapter<TestNimapAdapter.RecyclerViewHolder> {
    private ArrayList<Record> mRecordList;
    public TestNimapAdapter(List<Record> records) {
        mRecordList = (ArrayList<Record>) records;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_bind_data,parent,false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Record mRecord = mRecordList.get(position);
        holder.mFundValue.setText("\u20B9".concat(String.valueOf(mRecord.getTotalValue())));
        holder.mGoalsValue.setText("\u20B9".concat(String.valueOf(mRecord.getCollectedValue())));

        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
        String dateBeforeString = mRecord.getStartDate().replace("/"," ");
        String dateAfterString = mRecord.getEndDate().replace("/"," ");
        try {
            Date dateBefore = myFormat.parse(dateBeforeString);
            Date dateAfter = myFormat.parse(dateAfterString);
            long difference = dateAfter.getTime() - dateBefore.getTime();
            int daysBetween = (int) (difference / (1000*60*60*24));
            holder.mTimeValue.setText(String.valueOf(daysBetween));

        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.mDescription.setText(mRecord.getShortDescription());
        holder.mTitle.setText(mRecord.getTitle());
        Glide.with(holder.itemView.getContext()).load(mRecord.getMainImageURL()).diskCacheStrategy(DiskCacheStrategy.DATA).into(holder.mBannerImageView);
    }

    @Override
    public int getItemCount() {
        return mRecordList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView mFundValue, mGoalsValue, mTimeValue, mDescription, mTitle;
        ImageView mBannerImageView;
        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            mFundValue = itemView.findViewById(R.id.fundedValue);
            mGoalsValue = itemView.findViewById(R.id.goalsValue);
            mTimeValue = itemView.findViewById(R.id.timeValue);
            mTitle = itemView.findViewById(R.id.title);
            mDescription = itemView.findViewById(R.id.description);
            mBannerImageView = itemView.findViewById(R.id.imageView);
        }
    }
}
