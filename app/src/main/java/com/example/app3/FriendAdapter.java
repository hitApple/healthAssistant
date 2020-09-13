package com.example.app3;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private List<Friend> mFriendsList;

    public FriendAdapter(List<Friend> friendList){
        this.mFriendsList = friendList;
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        View friendView;
        TextView friendName;
        TextView friendTel;
        TextView friendLogin;

        public ViewHolder(View view){
            super(view);
            friendView = view;
            friendName = view.findViewById(R.id.friend_name);
            friendTel = view.findViewById(R.id.friend_tel);
            friendLogin = view.findViewById(R.id.friend_login_status);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent,
                false);
        final ViewHolder holder = new ViewHolder(view);
        holder.friendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), VideoCall.class);
                intent.putExtra("user", MainActivity.mPhone);
                intent.putExtra("room", "room_" + MainActivity.mPhone);
                intent.putExtra("stream1", "stream_" + MainActivity.mPhone);
                intent.putExtra("stream2", "stream_" +
                        holder.friendTel.getText().toString().substring(4));
                intent.putExtra("recv", false);
                view.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Friend friend = mFriendsList.get(position);
        holder.friendName.setText(friend.getName());
        holder.friendTel.setText(friend.getTel());
        holder.friendLogin.setText(friend.getStatus());

    }

    @Override
    public int getItemCount() {
        return mFriendsList.size();
    }

}
