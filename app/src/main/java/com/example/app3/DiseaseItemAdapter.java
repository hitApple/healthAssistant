package com.example.app3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DiseaseItemAdapter extends RecyclerView.Adapter<DiseaseItemAdapter.ViewHolder> {

    private List<Disase> mDiseaseList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tittleTextView;
        TextView contentTextView;

        public ViewHolder(View view){
            super(view);
            tittleTextView = (TextView) view.findViewById(R.id.disease_item_tittle);
            contentTextView = (TextView) view.findViewById(R.id.disease_item_content);
        }
    }

    public DiseaseItemAdapter(List<Disase> diseaseItemList){
        mDiseaseList = diseaseItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.disease_item, parent,
                false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Disase item = mDiseaseList.get(position);
        holder.tittleTextView.setText(item.getName());
        holder.contentTextView.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return mDiseaseList.size();
    }
}
