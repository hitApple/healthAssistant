package com.example.app3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class FriendView extends BaseActivity {

    private LinearLayout greyBackground;
    private ConstraintLayout centerLayout;
    private ImageView addFriend;
    private Button submitFriendTel;
    private Button quitAddFriend;
    private EditText friendTelText;
    private EditText friendNameText;
    private List<Friend> friendList;
    private RecyclerView friendViewRecycler;
    private FriendAdapter friendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_view);
        friendViewRecycler = findViewById(R.id.friend_view_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        friendViewRecycler.setLayoutManager(layoutManager);

        List<FriendContact> myContacts = LitePal.select("friendName", "friendTel")
                                         .where("ownerTel = ?", MainActivity.mPhone)
                                         .find(FriendContact.class);

        friendList = changeToFriend(myContacts);

//        LitePal.deleteAll(FriendContact.class);

        friendAdapter = new FriendAdapter(friendList);
        friendViewRecycler.setAdapter(friendAdapter);

        greyBackground = findViewById(R.id.grey_background);
        centerLayout = findViewById(R.id.center_layout);
        addFriend = findViewById(R.id.add_friend);
        submitFriendTel = findViewById(R.id.submit_friend_phone);
        quitAddFriend = findViewById(R.id.quit_add_friend);
        friendTelText = findViewById(R.id.friend_tel_text);
        friendNameText = findViewById(R.id.friend_name_text);

        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                greyBackground.setVisibility(View.VISIBLE);
                centerLayout.setVisibility(View.VISIBLE);
                friendViewRecycler.setEnabled(false);
            }
        });

        submitFriendTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String friendTelString = friendTelText.getText().toString();
                String friendNameString = friendNameText.getText().toString();

                Friend friend1 = new Friend();
                friend1.setTel(friendTelString);
                friend1.setName(friendNameString);

                FriendContact contact = new FriendContact();
                contact.setOwnerTel(MainActivity.mPhone);
                contact.setFriendName(friendNameString);
                contact.setFriendTel(friendTelString);
                contact.save();

                friendList.add(friend1);
                friendAdapter.notifyItemInserted(friendList.size() - 1);
                greyBackground.setVisibility(View.GONE);
                centerLayout.setVisibility(View.GONE);
                friendViewRecycler.setEnabled(true);
                closeKeyboard();
            }
        });

        quitAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                greyBackground.setVisibility(View.GONE);
                centerLayout.setVisibility(View.GONE);
                friendViewRecycler.setEnabled(true);
            }
        });

    }

    private List<Friend> changeToFriend(List<FriendContact> myContacts){
        List<Friend> contactList = new ArrayList<>();

        Friend friendTest = new Friend();
        friendTest.setName("测试选项");
        friendTest.setTel("15600000000");
        contactList.add(friendTest);

        if (myContacts == null || myContacts.size() == 0){
            return contactList;
        }

        Friend friend = new Friend();
        for (FriendContact contact : myContacts){
            friend.setName(contact.getFriendName());
            friend.setTel(contact.getFriendTel());
            contactList.add(friend);
        }

        return contactList;
    }

    /**
     * 调用之后可以关闭键盘
     */
    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}