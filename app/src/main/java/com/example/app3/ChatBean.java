package com.example.app3;

class ChatBean {

    private String name;
    private String say;
    private int icon;

    public ChatBean() {}

    public ChatBean(String name,int icon,String say) {
        this.name = name;
        this.say  = say;
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return  name;
    }

    public String getSay() {
        return  say;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSay(String say) {
        this.say = say;
    }

    public Boolean isMe() {
        return name.toLowerCase().equals("kotlin");
    }
}