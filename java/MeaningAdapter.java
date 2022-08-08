package com.example.dictionary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MeaningAdapter extends RecyclerView.Adapter<MeaningAdapter.ViewHolder> {
    private Context context;
    private ArrayList<MeaningModel> meaningRVModelArrayList;

    public MeaningAdapter(Context context, ArrayList<MeaningModel> meaningRVModelArrayList){
        this.context=context;
        this.meaningRVModelArrayList = meaningRVModelArrayList;
    }
    @NonNull
    @Override
    public MeaningAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_views, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MeaningModel model = meaningRVModelArrayList.get(position);
        holder.pos.setText("Parts of Speech : "+model.getPOS());
        holder.mean.setText("Meaning : "+model.getMean());
    }

    @Override
    public int getItemCount() {
        return meaningRVModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView pos, mean;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pos = itemView.findViewById(R.id.pos_rv);
            mean = itemView.findViewById(R.id.meaning_rv);

        }
    }
}
