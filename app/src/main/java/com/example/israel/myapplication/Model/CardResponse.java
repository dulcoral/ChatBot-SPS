package com.example.israel.myapplication.Model;

import java.util.ArrayList;
import java.util.List;

public class CardResponse {
    String title;
    String url_img;
    String description;
    String text_btn;

    public CardResponse(String title, String url_img, String description, String text_btn) {
        this.title = title;
        this.url_img = url_img;
        this.description = description;
        this.text_btn = text_btn;
    }

    public CardResponse(){

    }

    public String getTitle() {
        return title;
    }

    public String getUrl_img() {
        return url_img;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getText_btn() {
        return text_btn;
    }

    public void setText_btn(String text_btn) {
        this.text_btn = text_btn;
    }
}

