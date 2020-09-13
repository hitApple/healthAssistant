package com.example.app3;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.litepal.LitePal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

    private Timer timer;

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
                if (friendTelString.equals("")){
                    Toast.makeText(FriendView.this, "您还没有填写朋友的电话信息！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < friendTelString.length(); i++){
                    if (!(friendTelString.charAt(i) >= '0' && friendTelString.charAt(i) <= '9')){
                        Toast.makeText(FriendView.this, "电话信息的格式不正确！",
                                Toast.LENGTH_SHORT).show();
                        friendTelText.setText("");
                        return;
                    }
                }
                String friendNameString = friendNameText.getText().toString();
                if (friendNameString.equals("")){
                    Toast.makeText(FriendView.this, "您还没有填写朋友的名称信息！",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

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

        ImageView backImage = findViewById(R.id.friend_view_back);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer = new Timer();
        timer.schedule(new checkLoginTimer(friendList, friendViewRecycler), 0L, 10000L);
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

        Friend friend;
        for (FriendContact contact : myContacts){
            friend = new Friend();
            friend.setName(contact.getFriendName());
            friend.setTel(contact.getFriendTel());
            contactList.add(friend);
        }

        return contactList;
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
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

    class checkLoginTimer extends TimerTask {

        List<Friend> friendList;
        RecyclerView recyclerView;

        public checkLoginTimer(List<Friend> friendList, RecyclerView recyclerView){
            this.friendList = friendList;
            this.recyclerView = recyclerView;
        }

        @Override
        public void run() {
            try {
                Socket socket = new Socket("39.96.71.56", 8003);
                socket.setSoTimeout(500);
//                Socket socket = new Socket("10.0.2.2", 8000);
                OutputStream outputStream = socket.getOutputStream();
                StringBuilder builder = new StringBuilder();
                for (Friend friend : friendList){
                    builder.append(friend.getTel().substring(4));
                    builder.append("\n");
                }
                outputStream.write(builder.toString().getBytes("UTF-8"));
                outputStream.flush();
                InputStream is = socket.getInputStream();
                byte[] bytes = new byte[1024];
                for (int i = 0; i < 3; i++) {
                    int n = -1;
                    try {
                        n = is.read(bytes);
                    } catch (Exception e) {
                        continue;
                    }
                    if(n != -1) {
                        try {
                            boolean changed = false;
                            char[] loginStatuses = new String(bytes, 0, n,
                                    "UTF-8").toCharArray();
                            for (int j = 0; j < friendList.size(); j++){
                                Friend friend = friendList.get(j);
                                if (friend.getStatus().equals("离线") && loginStatuses[j] != '0'){
                                    friend.setStatus("在线");
                                    friendList.set(j, friend);
                                    changed = true;
                                } else if (friend.getStatus().equals("在线") && loginStatuses[j] == '0'){
                                    friend.setStatus("离线");
                                    friendList.set(j, friend);
                                    changed = true;
                                }
                            }
                            if (changed){
                                FriendView.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        recyclerView.setAdapter(new FriendAdapter(friendList));
                                    }
                                });
                            }

                            break;
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            return;
                        } catch (ArrayIndexOutOfBoundsException e){
                            e.printStackTrace();
                            return;
                        } catch (Exception e){
                            e.printStackTrace();
                            return;
                        }
                    }

                }
                is.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}