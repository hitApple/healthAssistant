package com.example.app3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class FriendView extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_view);
        RecyclerView friendViewRecycler = findViewById(R.id.friend_view_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        friendViewRecycler.setLayoutManager(layoutManager);
        Friend friend = new Friend();
        friend.setName("我滴好朋友");
        friend.setTel("15689712036");
        Friend friend2 = new Friend();
        friend2.setName("我滴好医生");
        friend2.setTel("15600000000");

        List<Friend> friendList = new ArrayList<>();
        friendList.add(friend);
        friendList.add(friend2);
        friendViewRecycler.setAdapter(new FriendAdapter(friendList));

    }
}