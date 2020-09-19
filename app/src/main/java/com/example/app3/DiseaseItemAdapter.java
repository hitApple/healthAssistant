package com.example.app3;

import android.animation.ObjectAnimator;
import android.graphics.Paint;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DiseaseItemAdapter extends RecyclerView.Adapter<DiseaseItemAdapter.ViewHolder> {

    private List<Disase> mDiseaseList;

    class ViewHolder extends RecyclerView.ViewHolder{
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
        final ViewHolder holder = new ViewHolder(view);
        holder.contentTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final HomePage homePage = (HomePage) view.getContext();

                homePage.clickTittleTextView.setVisibility(View.VISIBLE);
                homePage.clickTittleTextView.setText(holder.tittleTextView.getText().toString());
                homePage.clickTextView.setVisibility(View.VISIBLE);
                homePage.clickTextView.setText("        " + holder.contentTextView.getText().toString());
                homePage.diseaseRecyclerView.setVisibility(View.GONE);
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return;
                        }

                        homePage.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Paint paint = new Paint();
                                paint.setTextSize(homePage.clickTittleTextView.getTextSize());
                                float size = paint.measureText(homePage.clickTittleTextView.getText().toString());
                                homePage.clickTittleTextView.clearAnimation();
                                ObjectAnimator.ofFloat(homePage.clickTittleTextView,
                                        "translationX",
                                        (float) (MainActivity.mScreenWidth / 2 - size / 2 - 5))
                                        .setDuration(500)
                                        .start();
                            }

                        });
                    }
                }.start();
            }
        });
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
