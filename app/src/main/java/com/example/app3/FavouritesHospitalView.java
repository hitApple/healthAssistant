package com.example.app3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class FavouritesHospitalView extends BaseActivity {

    private RecyclerView myHospitalRecycler;
    private List<HospitalFavourites> favouritesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_hospital_view);
        myHospitalRecycler = findViewById(R.id.hospital_favourites_view_recycler);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        myHospitalRecycler.setLayoutManager(manager);
        favouritesList = LitePal.where("ownerTel = ?",
                MainActivity.mPhone)
                .find(HospitalFavourites.class);
    }
}