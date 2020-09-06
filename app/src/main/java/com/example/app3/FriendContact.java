package com.example.app3;

import org.litepal.crud.LitePalSupport;

public class FriendContact extends LitePalSupport {

    private String ownerTel;
    private String friendName;
    private String friendTel;

    public String getOwnerTel() {
        return ownerTel;
    }

    public void setOwnerTel(String ownerTel) {
        this.ownerTel = ownerTel;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendTel() {
        return friendTel;
    }

    public void setFriendTel(String friendTel) {
        this.friendTel = friendTel;
    }
}
