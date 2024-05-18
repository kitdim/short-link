package com.example.demo.dto;

public class LinkDTO {
    private int rank;

    private String origin;

    private String shorName;

    private Long count;

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getShorName() {
        return shorName;
    }

    public void setShorName(String shorName) {
        this.shorName = "http://localhost:8080/" + shorName;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
