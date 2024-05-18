package com.example.demo.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "links")
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String origin;

    private String shortName;

    private Long count;

    public Link() {
        count = 0L;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getOrigin() {
        return origin;
    }

    public Long getCount() {
        return count;
    }
    public String getShortName() {
        return shortName;
    }
}
