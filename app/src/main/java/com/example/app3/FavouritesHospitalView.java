package com.example.app3;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class FavouritesHospitalView extends BaseActivity {

    private ListView myHospitalListView;
    private List<HospitalFavourites> favouritesList;
    private List<Hospital> hospitalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_hospital_view);
        myHospitalListView = findViewById(R.id.hospital_favourites_list_view);
        favouritesList = LitePal.where("ownerTel = ?",
                MainActivity.mPhone)
                .find(HospitalFavourites.class);
        hospitalList = turnToHospitalList(favouritesList);
        HospitalAdapter adapter = new HospitalAdapter(FavouritesHospitalView.this,
                R.layout.hospital_item, hospitalList);
        myHospitalListView.setAdapter(adapter);
        myHospitalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(FavouritesHospitalView.this, HospitalWebView.class);
                intent.putExtra("医院名称", hospitalList.get(i).getName());
                intent.putExtra("医院电话", hospitalList.get(i).getTel());
                intent.putExtra("医院地址", hospitalList.get(i).getAddress());
                intent.putExtra("医院距离", String.valueOf(hospitalList.get(i).getDistance()));
                intent.putExtra("医院时间", hospitalList.get(i).getTime());

                startActivity(intent);
            }
        });
        ImageView backImage = findViewById(R.id.hospital_favourites_view_back);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FavouritesHospitalView.this, me.class));
            }
        });
    }

    private List<Hospital> turnToHospitalList(List<HospitalFavourites> favouritesList){
        Hospital hospital;
        List<Hospital> hospitalList = new ArrayList<>();
        for (HospitalFavourites favourites : favouritesList){
            hospital = new Hospital();
            hospital.setName(favourites.getHospitalName());
            hospital.setTel(favourites.getHospitalTel());
            hospital.setAddress(favourites.getHospitalAddress());
            if (favourites.getHospitalDistance() != null){
                hospital.setDistance(Integer.parseInt(favourites.getHospitalDistance()));
            } else {
                hospital.setDistance(0);
            }
            hospital.setTime(favourites.getHospitalTime());
            hospitalList.add(hospital);
        }
        return hospitalList;
    }

}