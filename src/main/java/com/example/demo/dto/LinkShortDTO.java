package com.example.demo.dto;

public class LinkShortDTO {
    private String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = "http://localhost:8080/" + link;
    }
}
