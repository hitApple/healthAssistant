package com.example.app3;

import org.litepal.crud.LitePalSupport;

public class Disase extends LitePalSupport {

    private String body;
    private String name;
    private String description;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
